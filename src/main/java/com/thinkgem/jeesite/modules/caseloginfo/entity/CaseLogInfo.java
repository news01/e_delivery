/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseloginfo.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 案件物流信息表Entity
 * @author niushi
 * @version 2018-08-22
 */
public class CaseLogInfo extends DataEntity<CaseLogInfo> {
	
	private static final long serialVersionUID = 1L;
	private String logId;		// 物流信息id
	private String caseId;		// 对应的案件信息
	private String emailId;		// 邮单id(返程)
	private Date logTime;		// 物流时间
	private String logInfo;		// 流转信息
	private String logStatus;		// 流转状态;1:正在流转;2:已被签收
	private String deliver;		// 配送人
	private String signMen;		// 签收人
	private String attribute1;		// 备用字段
	private String attribute2;		// attribute2
	private String attribute3;		// attribute3
	private String attribute4;		// attribute4
	private String attribute5;		// attribute5
	private String attribute6;		// attribute6
	
	public CaseLogInfo() {
		super();
	}

	public CaseLogInfo(String id){
		super(id);
	}

	@Length(min=1, max=11, message="物流信息id长度必须介于 1 和 11 之间")
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
	
	@Length(min=0, max=64, message="对应的案件信息长度必须介于 0 和 64 之间")
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	@Length(min=0, max=64, message="邮单id(返程)长度必须介于 0 和 64 之间")
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	
	@Length(min=0, max=255, message="流转信息长度必须介于 0 和 255 之间")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	
	@Length(min=0, max=2, message="流转状态;1:正在流转;2:已被签收长度必须介于 0 和 2 之间")
	public String getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}
	
	@Length(min=0, max=255, message="配送人长度必须介于 0 和 255 之间")
	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}
	
	@Length(min=0, max=255, message="签收人长度必须介于 0 和 255 之间")
	public String getSignMen() {
		return signMen;
	}

	public void setSignMen(String signMen) {
		this.signMen = signMen;
	}
	
	@Length(min=0, max=255, message="备用字段长度必须介于 0 和 255 之间")
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	
	@Length(min=0, max=255, message="attribute2长度必须介于 0 和 255 之间")
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	
	@Length(min=0, max=255, message="attribute3长度必须介于 0 和 255 之间")
	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	
	@Length(min=0, max=255, message="attribute4长度必须介于 0 和 255 之间")
	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	
	@Length(min=0, max=255, message="attribute5长度必须介于 0 和 255 之间")
	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
	
	@Length(min=0, max=255, message="attribute6长度必须介于 0 和 255 之间")
	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
	
}