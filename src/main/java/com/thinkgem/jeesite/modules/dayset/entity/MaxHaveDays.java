/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.dayset.entity;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 配置时间Entity
 * @author 朱明军
 * @version 2017-10-31
 */
public class MaxHaveDays extends DataEntity<MaxHaveDays> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 部门标识符
	private String days;		// 最大持有时长
	
	public MaxHaveDays() {
		super();
	}

	public MaxHaveDays(String id){
		super(id);
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=3, message="最大持有时长长度必须介于 0 和 3 之间")
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
	
}