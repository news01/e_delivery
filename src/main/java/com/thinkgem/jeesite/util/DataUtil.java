package com.thinkgem.jeesite.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

import net.sf.json.JSONObject;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataUtil extends DataEntity<DataUtil> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String table;
	private Object obj;
	private String opration;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getOpration() {
		return opration;
	}
	public void setOpration(String opration) {
		this.opration = opration;
	}
	
}
