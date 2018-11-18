/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivetrank.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 测试Entity
 * @author 朱明军
 * @version 2017-06-02
 */
public class ArchivesTrank extends DataEntity<ArchivesTrank> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户Id
	private String fileId;		// 卷宗ID
	private String signTime;		// 签收时间
	private String turnTime;		// 转出时间
	private String allPages;		// 增加页数
	private Date updateTime;		// 更新时间
	private String qsr;
	public ArchivesTrank() {
		super();
	}
	
	public String getQsr() {
		return qsr;
	}

	public void setQsr(String qsr) {
		this.qsr = qsr;
	}

	public ArchivesTrank(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="卷宗ID长度必须介于 0 和 64 之间")
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@Length(min=0, max=255, message="签收时间长度必须介于 0 和 255 之间")
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	
	@Length(min=0, max=255, message="转出时间长度必须介于 0 和 255 之间")
	public String getTurnTime() {
		return turnTime;
	}

	public void setTurnTime(String turnTime) {
		this.turnTime = turnTime;
	}
	
	@Length(min=0, max=11, message="增加页数长度必须介于 0 和 11 之间")
	public String getAllPages() {
		return allPages;
	}

	public void setAllPages(String allPages) {
		this.allPages = allPages;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "ArchivesTrank{" +
				"user=" + user +
				", fileId='" + fileId + '\'' +
				", signTime='" + signTime + '\'' +
				", turnTime='" + turnTime + '\'' +
				", allPages='" + allPages + '\'' +
				", updateTime=" + updateTime +
				", qsr='" + qsr + '\'' +
				'}';
	}
}