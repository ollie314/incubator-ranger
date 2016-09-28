/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.services.knox.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ranger.plugin.model.RangerService;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.service.ResourceLookupContext;
import org.apache.ranger.services.knox.RangerServiceKnox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestRangerServiceKnox {

	static final String 	sdName		  =  "svcDef-Knox";
	static final String 	serviceName   =  "KnoxDef";
	HashMap<String, Object> responseData  =  null;
	Map<String, String> 	configs 	  =  null;
	RangerServiceKnox 		svcKnox	  	  =  null;
	RangerServiceDef 		sd 			  =  null;
	RangerService			svc			  =  null;
	ResourceLookupContext   lookupContext =  null;
	
	
	@Before
	public void setup() {
		configs 	= new HashMap<String,String>();
		lookupContext = new ResourceLookupContext();
		
		buildHbaseConnectionConfig();
		buildLookupContext();
	
		sd		 = new RangerServiceDef(sdName, " org.apache.ranger.services.knox.RangerServiceKnox", "TestKnoxService", "test Knox servicedef description", null, null, null, null, null, null, null);
		svc   	 = new RangerService(sdName, serviceName, "unit test Knox resource lookup and validateConfig", null, configs);
		svcKnox  = new RangerServiceKnox();
		svcKnox.init(sd, svc);
	}
	
	@Test
	public void testValidateConfig() {

		/* TODO: does this test require a live Knox environment?
		 *
		HashMap<String,Object> ret = null;
		String errorMessage = null;
		
		try {
			ret = svcKnox.validateConfig();
		}catch (Exception e) {
			errorMessage = e.getMessage();
			if ( e instanceof HadoopException) {
				errorMessage = "HadoopException";
			}
		}
		
		if ( errorMessage != null) {
			assertTrue(errorMessage.contains("HadoopException"));
		} else {
			assertNotNull(ret);
		}
		*
		*/
	}
	
	
	@Test
	public void	testLookUpResource() {
		/* TODO: does this test require a live Knox environment?
		 *
		List<String> ret 	= new ArrayList<String>();
		String errorMessage = null;
		try {
			ret = svcKnox.lookupResource(lookupContext);
		}catch (Exception e) {
			errorMessage = e.getMessage();
			if ( e instanceof HadoopException) {
				errorMessage = "HadoopException";
			}
		}
		
		if ( errorMessage != null) {
			assertTrue(errorMessage.contains("HadoopException"));
		} else {
			assertNotNull(ret);
		}
		*
		*/
	}
	
	public void buildHbaseConnectionConfig() {
		configs.put("username", "admin");
		configs.put("password", "admin-password");
		configs.put("knox.url", "https://localhost:8443/gateway/admin/api/v1/topologies");
	}

	public void buildLookupContext() {
		Map<String, List<String>> resourceMap = new HashMap<String,List<String>>();
		resourceMap.put("topology", null);
		lookupContext.setUserInput("a");
		lookupContext.setResourceName("topology");
		lookupContext.setResources(resourceMap);
	}
		
	@After
	public void tearDown() {
		sd  = null;
		svc = null;
	}
	
}
