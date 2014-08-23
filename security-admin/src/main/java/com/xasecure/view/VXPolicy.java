package com.xasecure.view;

/*
 * Copyright (c) 2014 XASecure
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * XASecure ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with XASecure
 */

/**
 * Policy
 * @author tushar
 */

import java.util.*;

import com.xasecure.common.*;

import javax.xml.bind.annotation.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class VXPolicy extends VXDataObject implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * PolicyName
	 */
	protected String policyName;
	/**
	 * resourceName
	 */
	protected String resourceName;
	/**
	 * ResourceType : Label of EnumType : ResourceType
	 */
	protected String resourceType;
	/**
	 * Description
	 */
	protected String description;
	/**
	 * Id of the repository
	 */
	protected Long repositoryId;
	/**
	 * Repository Name
	 */
	protected String repositoryName;
	/**
	 * Repository Type
	 */
	protected String repositoryType;
	/**
	 * List of permissions maps
	 */
	protected List<VXPermObj> permMapList;
	/**
	 * Tables
	 */
	protected String tables;
	/**
	 * Column families
	 */
	protected String columnFamilies;
	/**
	 * Columns
	 */
	protected String columns;
	/**
	 * Databases
	 */
	protected String databases;
	/**
	 * UDFs
	 */
	protected String udfs;
	/**
	 * Table Type
	 */
	protected String tableType;
	/**
	 * Resource Status
	 */
	protected String columnType;
	/**
	 * Topologoies
	 */
	protected String topologies;
	/**
	 * Services
	 */
	protected String services;
	/**
	 * Resource/Policy Status, boolean values : true/false
	 * 
	 */
	protected boolean isEnabled;
	/**
	 * Is recursive This attribute is of type enum CommonEnums::BooleanValue
	 */
	protected boolean isRecursive;
	/**
	 * Audit is enable or not.
	 */
	protected boolean isAuditEnabled;
	/**
	 * Check parent permission This attribute is of type enum
	 * CommonEnums::BooleanValue
	 */
	protected boolean checkParentPermission;
	/**
	 * Version No of Project
	 */
	protected String version;

	protected String grantor;
	/**
	 * Default constructor. This will set all the attributes to default value.
	 */
	public VXPolicy() {
		resourceType = AppConstants
				.getLabelFor_ResourceType(AppConstants.RESOURCE_PATH);
		isRecursive = false;
		checkParentPermission = false;
	}

	/**
	 * Returns the value for the member attribute <b>policyName</b>
	 * 
	 * @return String - value of member attribute <b>policyName</b>.
	 */
	public String getPolicyName() {
		return policyName;
	}

	/**
	 * This method sets the value to the member attribute <b>policyName</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param policyName
	 *            Value to set member attribute <b>policyName</b>
	 */
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	/**
	 * This method sets the value to the member attribute <b>resourceName</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param name
	 *            Value to set member attribute <b>resourceName</b>
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * Returns the value for the member attribute <b>resourceName</b>
	 * 
	 * @return String - value of member attribute <b>resourceName</b>.
	 */
	public String getResourceName() {
		return this.resourceName;
	}

	/**
	 * This method sets the value to the member attribute <b>resourceType</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param resourceType
	 *            Value to set member attribute <b>resourceType</b>
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * Returns the value for the member attribute <b>resourceType</b>
	 * 
	 * @return String - value of member attribute <b>resourceType</b>.
	 */
	public String getResourceType() {
		return this.resourceType;
	}

	/**
	 * This method sets the value to the member attribute <b>description</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param description
	 *            Value to set member attribute <b>description</b>
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the value for the member attribute <b>description</b>
	 * 
	 * @return String - value of member attribute <b>description</b>.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the repositoryId
	 */
	public Long getRepositoryId() {
		return repositoryId;
	}

	/**
	 * @param repositoryId
	 *            the repositoryId to set
	 */
	public void setRepositoryId(Long repositoryId) {
		this.repositoryId = repositoryId;
	}

	/**
	 * This method sets the value to the member attribute <b>assetName</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param assetName
	 *            Value to set member attribute <b>assetName</b>
	 */
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	/**
	 * Returns the value for the member attribute <b>repositoryName</b>
	 * 
	 * @return String - value of member attribute <b>repositoryName</b>.
	 */
	public String getRepositoryName() {
		return this.repositoryName;
	}

	/**
	 * This method sets the value to the member attribute <b>assetType</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param assetType
	 *            Value to set member attribute <b>assetType</b>
	 */
	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}

	/**
	 * Returns the value for the member attribute <b>repositoryType</b>
	 * 
	 * @return String - value of member attribute <b>repositoryType</b>.
	 */
	public String getRepositoryType() {
		return this.repositoryType;
	}

	/**
	 * This method sets the value to the member attribute <b>permMapList</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param permMapList
	 *            Value to set member attribute <b>permMapList</b>
	 */
	public void setPermMapList(List<VXPermObj> permMapList) {
		this.permMapList = permMapList;
	}

	/**
	 * Returns the value for the member attribute <b>userPermList</b>
	 * 
	 * @return List<VXPermObj> - value of member attribute <b>permMapList</b>.
	 */
	public List<VXPermObj> getPermMapList() {
		return this.permMapList;
	}

	/**
	 * This method sets the value to the member attribute <b>tables</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param tables
	 *            Value to set member attribute <b>tables</b>
	 */
	public void setTables(String tables) {
		this.tables = tables;
	}

	/**
	 * Returns the value for the member attribute <b>tables</b>
	 * 
	 * @return String - value of member attribute <b>tables</b>.
	 */
	public String getTables() {
		return this.tables;
	}

	/**
	 * This method sets the value to the member attribute <b>columnFamilies</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param columnFamilies
	 *            Value to set member attribute <b>columnFamilies</b>
	 */
	public void setColumnFamilies(String columnFamilies) {
		this.columnFamilies = columnFamilies;
	}

	/**
	 * Returns the value for the member attribute <b>columnFamilies</b>
	 * 
	 * @return String - value of member attribute <b>columnFamilies</b>.
	 */
	public String getColumnFamilies() {
		return this.columnFamilies;
	}

	/**
	 * This method sets the value to the member attribute <b>columns</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param columns
	 *            Value to set member attribute <b>columns</b>
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}

	/**
	 * Returns the value for the member attribute <b>columns</b>
	 * 
	 * @return String - value of member attribute <b>columns</b>.
	 */
	public String getColumns() {
		return this.columns;
	}

	/**
	 * This method sets the value to the member attribute <b>databases</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param databases
	 *            Value to set member attribute <b>databases</b>
	 */
	public void setDatabases(String databases) {
		this.databases = databases;
	}

	/**
	 * Returns the value for the member attribute <b>databases</b>
	 * 
	 * @return String - value of member attribute <b>databases</b>.
	 */
	public String getDatabases() {
		return this.databases;
	}

	/**
	 * This method sets the value to the member attribute <b>udfs</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param udfs
	 *            Value to set member attribute <b>udfs</b>
	 */
	public void setUdfs(String udfs) {
		this.udfs = udfs;
	}

	/**
	 * Returns the value for the member attribute <b>udfs</b>
	 * 
	 * @return String - value of member attribute <b>udfs</b>.
	 */
	public String getUdfs() {
		return this.udfs;
	}

	/**
	 * Returns the value for the member attribute <b>tableType</b>
	 * 
	 * @return String - value of member attribute <b>tableType</b>.
	 */
	public String getTableType() {
		return tableType;
	}

	/**
	 * This method sets the value to the member attribute <b>tableType</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param tableType
	 *            Value to set member attribute <b>tableType</b>
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	/**
	 * Returns the value for the member attribute <b>columnType</b>
	 * 
	 * @return String - value of member attribute <b>columnType</b>.
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * This method sets the value to the member attribute <b>columnType</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param columnType
	 *            Value to set member attribute <b>columnType</b>
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * Returns the value for the member attribute <b>topologies</b>
	 * 
	 * @return String - value of member attribute <b>topologies</b>.
	 */
	public String getTopologies() {
		return topologies;
	}

	/**
	 * This method sets the value to the member attribute <b>topologies</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param topologies
	 *            Value to set member attribute <b>topologies</b>
	 */
	public void setTopologies(String topologies) {
		this.topologies = topologies;
	}

	/**
	 * Returns the value for the member attribute <b>services</b>
	 * 
	 * @return String - value of member attribute <b>services</b>.
	 */
	public String getServices() {
		return services;
	}

	/**
	 * This method sets the value to the member attribute <b>services</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param services
	 *            Value to set member attribute <b>services</b>
	 */
	public void setServices(String services) {
		this.services = services;
	}

	/**
	 * This method sets the value to the member attribute <b>resourceStatus</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param isEnabled
	 *            Value to set member attribute <b>isEnable</b>
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Returns the value for the member attribute <b>isEnable</b>
	 * 
	 * @return boolean - value of member attribute <b>isEnable</b>.
	 */
	public boolean isEnabled() {
		return this.isEnabled;
	}

	/**
	 * This method sets the value to the member attribute <b>isRecursive</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param isRecursive
	 *            Value to set member attribute <b>isRecursive</b>
	 */
	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	/**
	 * Returns the value for the member attribute <b>isRecursive</b>
	 * 
	 * @return boolean - value of member attribute <b>isRecursive</b>.
	 */
	public boolean isRecursive() {
		return this.isRecursive;
	}

	/**
	 * This method sets the value to the member attribute <b>isAuditEnabled</b>.
	 * You cannot set null to the attribute.
	 * 
	 * @param isAuditEnabled
	 *            Value to set member attribute <b>isAuditEnabled</b>
	 */
	public void setAuditEnabled(boolean isAuditEnabled) {
		this.isAuditEnabled = isAuditEnabled;
	}

	/**
	 * Returns the value for the member attribute <b>isAuditEnabled</b>
	 * 
	 * @return boolean - value of member attribute <b>isAuditEnabled</b>.
	 */
	public boolean isAuditEnabled() {
		return this.isAuditEnabled;
	}

	/**
	 * This method sets the value to the member attribute
	 * <b>checkParentPermission</b>.
	 * 
	 * @param checkParentPermission
	 *            Value to set member attribute <b>checkParentPermission</b>
	 */
	public void setCheckParentPermission(boolean checkParentPermission) {
		this.checkParentPermission = checkParentPermission;
	}

	/**
	 * Returns the value for the member attribute <b>checkParentPermission</b>
	 * 
	 * @return boolean - value of member attribute <b>checkParentPermission</b>.
	 */
	public boolean getCheckParentPermission() {
		return this.checkParentPermission;
	}

	/**
	 * Returns the value for the member attribute <b>version</b>
	 * 
	 * @return String - value of member attribute <b>version</b>.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * This method sets the value to the member attribute <b>version</b>. You
	 * cannot set null to the attribute.
	 * 
	 * @param version
	 *            Value to set member attribute <b>version</b>
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	
	public String getGrantor() {
		return grantor;
	}

	public void setGrantor(String grantor) {
		this.grantor = grantor;
	}

	@Override
	public int getMyClassType() {
		return AppConstants.CLASS_TYPE_XA_RESOURCE;
	}

	/**
	 * This return the bean content in string format
	 * 
	 * @return formatedStr
	 */
	public String toString() {
		String str = "VXResource={";
		str += super.toString();
		str += "policyName={" + policyName + "} ";
		str += "resourceName={" + resourceName + "} ";
		str += "resourceType={" + resourceType + "} ";
		str += "description={" + description + "} ";
		str += "repositoryName={" + repositoryName + "} ";
		str += "repositoryType={" + repositoryType + "} ";
		str += "tables={" + tables + "} ";
		str += "columnFamilies={" + columnFamilies + "} ";
		str += "columns={" + columns + "} ";
		str += "databases={" + databases + "} ";
		str += "udfs={" + udfs + "} ";
		str += "tableType={" + tableType + "} ";
		str += "columnType={" + columnType + "} ";
		str += "topologies={" + topologies + "} ";
		str += "services={" + services + "} ";
		str += "isEnable={" + isEnabled + "} ";
		str += "isRecursive={" + isRecursive + "} ";
		str += "isAuditEnabled={" + isAuditEnabled + "} ";
		str += "checkParentPermission={" + checkParentPermission + "} ";
		str += "version={" + version + "} ";
		str += "}";
		return str;
	}
}