/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ranger.authorization.kafka.authorizer;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.curator.test.TestingServer;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.Assert;
import org.junit.Test;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

/**
 * A simple test that starts a Kafka broker, creates "test" and "dev" topics, sends a message to them and consumes it. We also plug in a 
 * CustomAuthorizer that enforces some authorization rules:
 * 
 *  - The "IT" group can do anything
 *  - The "public" group can only "read/describe" on the "test" topic, not "write".
 * 
 * Policies available from admin via:
 * 
 * http://localhost:6080/service/plugins/policies/download/KafkaTest
 */
public class KafkaRangerAuthorizerTest {
    
    private static KafkaServerStartable kafkaServer;
    private static TestingServer zkServer;
    private static int port;
    private static String serviceKeystorePath;
    private static String clientKeystorePath;
    private static String truststorePath;
    
    @org.junit.BeforeClass
    public static void setup() throws Exception {
    	// Create keys
    	String serviceDN = "CN=Service,O=Apache,L=Dublin,ST=Leinster,C=IE";
    	String clientDN = "CN=Client,O=Apache,L=Dublin,ST=Leinster,C=IE";
    	
    	// Create a truststore
    	KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    	keystore.load(null, "security".toCharArray());
    	
    	serviceKeystorePath = createAndStoreKey(serviceDN, serviceDN, BigInteger.valueOf(30), "sspass", "myservicekey", "skpass", keystore);
    	clientKeystorePath = createAndStoreKey(clientDN, clientDN, BigInteger.valueOf(31), "cspass", "myclientkey", "ckpass", keystore);
    	
    	File truststoreFile = File.createTempFile("kafkatruststore", ".jks");
    	keystore.store(new FileOutputStream(truststoreFile), "security".toCharArray());
    	truststorePath = truststoreFile.getPath();
    			
        zkServer = new TestingServer();
        
        // Get a random port
        ServerSocket serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        serverSocket.close();
        
        final Properties props = new Properties();
        props.put("broker.id", 1);
        props.put("host.name", "localhost");
        props.put("port", port);
        props.put("log.dir", "/tmp/kafka");
        props.put("zookeeper.connect", zkServer.getConnectString());
        props.put("replica.socket.timeout.ms", "1500");
        props.put("controlled.shutdown.enable", Boolean.TRUE.toString());
        // Enable SSL
        props.put("listeners", "SSL://localhost:" + port);
        props.put("ssl.keystore.location", serviceKeystorePath);
        props.put("ssl.keystore.password", "sspass");
        props.put("ssl.key.password", "skpass");
        props.put("ssl.truststore.location", truststorePath);
        props.put("ssl.truststore.password", "security");
        props.put("security.inter.broker.protocol", "SSL");
        props.put("ssl.client.auth", "required");
        
        // Plug in Apache Ranger authorizer
        props.put("authorizer.class.name", "org.apache.ranger.authorization.kafka.authorizer.RangerKafkaAuthorizer");
        
        // Create users for testing
        UserGroupInformation.createUserForTesting(clientDN, new String[] {"public"});
        UserGroupInformation.createUserForTesting(serviceDN, new String[] {"IT"});
        
        KafkaConfig config = new KafkaConfig(props);
        kafkaServer = new KafkaServerStartable(config);
        kafkaServer.startup();

        // Create some topics
        ZkClient zkClient = new ZkClient(zkServer.getConnectString(), 30000, 30000, ZKStringSerializer$.MODULE$);

        final ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zkServer.getConnectString()), false);
        AdminUtils.createTopic(zkUtils, "test", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
        AdminUtils.createTopic(zkUtils, "dev", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
    }
    
    @org.junit.AfterClass
    public static void cleanup() throws Exception {
        if (kafkaServer != null) {
            kafkaServer.shutdown();
        }
        if (zkServer != null) {
            zkServer.stop();
        }
        
        File clientKeystoreFile = new File(clientKeystorePath);
        if (clientKeystoreFile.exists()) {
            clientKeystoreFile.delete();
        }
        File serviceKeystoreFile = new File(serviceKeystorePath);
        if (serviceKeystoreFile.exists()) {
            serviceKeystoreFile.delete();
        }
        File truststoreFile = new File(truststorePath);
        if (truststoreFile.exists()) {
            truststoreFile.delete();
        }
    }
    
    private static String createAndStoreKey(String subjectName, String issuerName, BigInteger serial, String keystorePassword,
    		String keystoreAlias, String keyPassword, KeyStore trustStore) throws Exception {
    	
    	// Create KeyPair
    	KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    	keyPairGenerator.initialize(2048, new SecureRandom());
    	KeyPair keyPair = keyPairGenerator.generateKeyPair();
    	
    	Date currentDate = new Date();
    	Date expiryDate = new Date(currentDate.getTime() + 365L * 24L * 60L * 60L * 1000L);
    	
    	// Create X509Certificate
    	X509v3CertificateBuilder certBuilder =
    			new X509v3CertificateBuilder(new X500Name(RFC4519Style.INSTANCE, issuerName), serial, currentDate, expiryDate, 
    					new X500Name(RFC4519Style.INSTANCE, subjectName), SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
    	ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.getPrivate());
    	X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
    	
    	// Store Private Key + Certificate in Keystore
    	KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    	keystore.load(null, keystorePassword.toCharArray());
    	keystore.setKeyEntry(keystoreAlias, keyPair.getPrivate(), keyPassword.toCharArray(), new Certificate[] {certificate});
    	
    	File keystoreFile = File.createTempFile("kafkakeystore", ".jks");
    	keystore.store(new FileOutputStream(keystoreFile), keystorePassword.toCharArray());
    	
    	// Now store the Certificate in the truststore
    	trustStore.setCertificateEntry(keystoreAlias, certificate);
    	
    	return keystoreFile.getPath();
    	
    }
    
    // The "public" group can read from "test"
    @Test
    public void testAuthorizedRead() throws Exception {
        // Create the Producer
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:" + port);
        producerProps.put("acks", "all");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        producerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, serviceKeystorePath);
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "sspass");
        producerProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "skpass");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "security");
        
        final Producer<String, String> producer = new KafkaProducer<>(producerProps);
        
        // Create the Consumer
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:" + port);
        consumerProps.put("group.id", "test");
        consumerProps.put("enable.auto.commit", "true");
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("auto.commit.interval.ms", "1000");
        consumerProps.put("session.timeout.ms", "30000");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        consumerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        consumerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, clientKeystorePath);
        consumerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "cspass");
        consumerProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "ckpass");
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "security");
        
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList("test"));
        
        // Send a message
        producer.send(new ProducerRecord<String, String>("test", "somekey", "somevalue"));
        producer.flush();
        
        // Poll until we consume it
        
        ConsumerRecord<String, String> record = null;
        for (int i = 0; i < 1000; i++) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            if (records.count() > 0) {
                record = records.iterator().next();
                break;
            }
            Thread.sleep(1000);
        }

        Assert.assertNotNull(record);
        Assert.assertEquals("somevalue", record.value());

        producer.close();
        consumer.close();
    }
    
    // The "IT" group can write to any topic
    @Test
    public void testAuthorizedWrite() throws Exception {
        // Create the Producer
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:" + port);
        producerProps.put("acks", "all");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        producerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, serviceKeystorePath);
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "sspass");
        producerProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "skpass");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "security");
        
        final Producer<String, String> producer = new KafkaProducer<>(producerProps);
        
        // Send a message
        Future<RecordMetadata> record = 
            producer.send(new ProducerRecord<String, String>("dev", "somekey", "somevalue"));
        producer.flush();
        record.get();

        producer.close();
    }
    
    // The "public" group can't write to "test" or "dev"
    @Test
    public void testUnauthorizedWrite() throws Exception {
        // Create the Producer
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:" + port);
        producerProps.put("acks", "all");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        producerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, clientKeystorePath);
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "cspass");
        producerProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "ckpass");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "security");
        
        final Producer<String, String> producer = new KafkaProducer<>(producerProps);
        
        // Send a message
        try {
            Future<RecordMetadata> record = 
                producer.send(new ProducerRecord<String, String>("test", "somekey", "somevalue"));
            producer.flush();
            record.get();
            Assert.fail("Authorization failure expected");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("Not authorized to access topics"));
        }
        
        try {
            Future<RecordMetadata> record = 
                producer.send(new ProducerRecord<String, String>("dev", "somekey", "somevalue"));
            producer.flush();
            record.get();
            Assert.fail("Authorization failure expected");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("Not authorized to access topics"));
        }
        
        producer.close();
    }
}
