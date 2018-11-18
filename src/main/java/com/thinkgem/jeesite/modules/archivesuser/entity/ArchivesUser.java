/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivesuser.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 卷宗用户Entity
 * @author 朱明军
 * @version 2017-05-18
 */
public class ArchivesUser extends DataEntity<ArchivesUser> {
	
	private static final long serialVersionUID = 1L;
	private String userName;		// 用户名
	private String sex;		// 性别
	private String inDataDate;		// 创建时间
	private String office;		// 部门
	private String openId;		// open_id
	private String phoneNum;		// 电话号码
	private String name;		// 真实姓名
	private Date updateTime;		// 更新时间
	
	public ArchivesUser() {
		super();
	}

	public ArchivesUser(String id){
		super(id);
	}

	@Length(min=0, max=255, message="用户名长度必须介于 0 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=2, message="性别长度必须介于 0 和 2 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=255, message="创建时间长度必须介于 0 和 255 之间")
	public String getInDataDate() {
		return inDataDate;
	}

	public void setInDataDate(String inDataDate) {
		this.inDataDate = inDataDate;
	}
	
	@Length(min=0, max=255, message="部门长度必须介于 0 和 255 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
	
	@Length(min=0, max=255, message="open_id长度必须介于 0 和 255 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Length(min=0, max=40, message="电话号码长度必须介于 0 和 40 之间")
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	@Length(min=0, max=255, message="真实姓名长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}