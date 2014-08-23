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
 * List wrapper class for VXGroupGroup
 *
 */

import java.util.*;
import javax.xml.bind.annotation.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.xasecure.common.view.*;

@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, fieldVisibility=Visibility.ANY)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL )
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VXGroupGroupList extends VList {
	private static final long serialVersionUID = 1L;
    List<VXGroupGroup> vXGroupGroups = new ArrayList<VXGroupGroup>();

    public VXGroupGroupList() {
	super();
    }

    public VXGroupGroupList(List<VXGroupGroup> objList) {
	super(objList);
	this.vXGroupGroups = objList;
    }

    /**
     * @return the vXGroupGroups
     */
    public List<VXGroupGroup> getVXGroupGroups() {
	return vXGroupGroups;
    }

    /**
     * @param vXGroupGroups
     *            the vXGroupGroups to set
     */
    public void setVXGroupGroups(List<VXGroupGroup> vXGroupGroups) {
	this.vXGroupGroups = vXGroupGroups;
    }

    @Override
    public int getListSize() {
	if (vXGroupGroups != null) {
	    return vXGroupGroups.size();
	}
	return 0;
    }

    @Override
    public List<VXGroupGroup> getList() {
	return vXGroupGroups;
    }

}