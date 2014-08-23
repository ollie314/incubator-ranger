/**
 * 
 * Common module for Links used.
 * All hrefs should be used from this file.
 *    
*/
define(function(require) {
	'use strict';
	var XALinks     = {};
	var defaults = {
		href : 'javascript:void(0)',
		text : '',
		title : 'Title'
	};
	var links = {
			/* Main Menu links START */
			
			Dashboard : { 
				href : '#', 
				text : 'h.dashboard',
				title: 'h.dashboard'
			},
			PolicyManager :{
				href : '#!/policymanager', 
				text : 'h.policyManager',
				title: 'h.policyManager'
			},
			RepositoryManager :{
				href : '#!/policymanager', 
				text : 'h.repositoryManager',
				title: 'h.repositoryManager'
			},
			Users : { 
				href : '#!/users/usertab',
				text : 'h.usersOrGroups',
				title: 'h.usersOrGroups'
			},
			Groups : { 
				href : '#!/users/grouptab',
				text : 'h.usersOrGroups',
				title: 'h.usersOrGroups'
			},
			Configs: { 
				href : '#!/configs',
				text : 'h.configs',
				title: 'h.configs'
			},
			ManageTables: { 
				href : '#!/managetables',
				text : 'h.managetables',
				title: 'h.managetables'
			},
			addPolicyForTable: { 
				href : '#!/addpolicyfortable',
				text : 'h.addpolicyfortable',
				title: 'h.addpolicyfortable'
			},
			ManageFolders: { 
				href : '#!/managefolders',
				title: 'h.managefolders',
				text: 'h.managefolders'
			},
			addPolicyForFolder: { 
				href : '#!/addpolicyforfolder',
				title: 'h.addpolicyforfolder',
				text: 'h.addpolicyforfolder'
			},
			HDFSFolders: { 
				href : '#!/hdfs',
				text : 'h.managePolices',
				title: 'h.managePolices'
			},
			PolicyCreate: { 
				href : '#!/policy/create',
				text: 'h.createPolicy',
				title: 'h.createPolicy'
			},
			PolicyEdit: { 
				href : '#!/policy/create',
				text: 'h.editPolicy',
				title: 'h.editPolicy'
			},
			HivePolicyListing : {
				href : '#!/hive',
				title: 'h.managetables',
				text: 'h.manageTables'
			},
			/*
			 * Asset related
			 */
			/*AssetCreate: { 
				href : '#!/asset/create',
				text: 'lbl.createAsset',
				title: 'lbl.createAsset'
			},*/
			/*
			 * Reports Related
			 */
			UserAccessReport: { 
				href : '#!/reports/userAccess',
				text: 'lbl.userAccessReport',
				title: 'lbl.userAccessReport'
			},
			AuditReport: { 
				href : '#!/reports/audit',
				text: 'lbl.auditReport',
				title: 'lbl.auditReport'
			},
			UserProfile :{
				href : '#!/userprofile', 
				text : 'h.userProfile',
				title: 'h.userProfile'
			},
			
			/*
			 * User OR Groups Related
			 */
			UserCreate : {
					href : '#!/user/create',
					text : 'lbl.userCreate',
					title: 'lbl.userCreate'
			},
			UserEdit : {
				href : 'javascript:void(0);',
				text : 'lbl.userEdit',
				title: 'lbl.userEdit'
			},
			GroupCreate : {
				href : '#!/group/create',
				text : 'lbl.groupCreate',
				title: 'lbl.groupCreate'
			},
			GroupEdit : {
				href : 'javascript:void(0);',
				text : 'lbl.groupEdit'
			},
			SessionDetail : {
				href : '#!/reports/audit/loginSession',
				text : 'lbl.sessionDetail'
			},
			AssetCreate : function(options){
				var href = '#!/asset/create';
				if(_.has(options,'model')){
					href =  '#!/asset/create/'+options.model.get('assetType');
				}
				return {
					href : href,
					text : 'lbl.createAsset',
					title: 'lbl.createAsset'
				};
			},
			AssetEdit : function(options){
				var href = "javascript:void(0);";
				if(_.has(options,'model')){
					href =  '#!/asset/'+options.model.get('id');
				}
				if(_.has(options,'id')){
					href =  '#!/asset/'+options.id;
				}
				return {
					href : href,
					text : 'lbl.editAsset',
					title: 'lbl.editAsset'
				};
			},
			ManagePolicies : function(options){
				var href = "javascript:void(0);";
				if(_.has(options,'model')){
					href =  '#!/hdfs/'+options.model.id+"/policies";
				}
				return {
					href : href,
					text : options.model.get('name') +' Policies',
					title: options.model.get('name') +' Policies'
				};
			},
			ManageHivePolicies : function(options){
				var href = "javascript:void(0);";
				if(_.has(options,'model')){
					href =  '#!/hive/'+options.model.id+"/policies";
				}
				return {
					href : href,
					text : options.model.get('name') +' Policies',
					title: options.model.get('name') +' Policies'
				};
			},
			ManageHbasePolicies : function(options){
				var href = "javascript:void(0);";
				if(_.has(options,'model')){
					href =  '#!/hbase/'+options.model.id+"/policies";
				}
				return {
					href : href,
					text : options.model.get('name') +' Policies',
					title: options.model.get('name') +' Policies'
				};
			},
			ManageKnoxPolicies : function(options){
				var href = "javascript:void(0);";
				if(_.has(options,'model')){
					href =  '#!/knox/'+options.model.id+"/policies";
				}
				return {
					href : href,
					text : options.model.get('name') +' Policies',
					title: options.model.get('name') +' Policies'
				};
			}
	};      
       
	
	XALinks.get = function(type, options){
		if(! _.has(links, type)){
			throw new Error("Link " + type + " not found in XALinks Module!");
		}
		var opts = {};
		// in some case we will get model directly as options
		if( options && !_.has(options,'model') &&  options instanceof Backbone.Model){
			opts['model'] = options;
		} else {
			opts = options;
		}

		if(typeof links[type] === 'function'){
			return links[type](opts||{});
		}

		return links[type];
	};
	
	
	
	
	return XALinks;

});
