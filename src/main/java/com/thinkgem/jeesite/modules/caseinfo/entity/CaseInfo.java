/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseinfo.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 案件信息表Entity
 * @author niushi
 * @version 2018-08-10
 */
public class CaseInfo extends DataEntity<CaseInfo> {

	private static final long serialVersionUID = 1L;
	private String caId;		// 案件id
	private String caseNum;		// 案号
	private String firstMail;		// 首程邮单号
	private String returnMail;		// 返程邮单号
	private String deliver;		// 受送达人
	private String deliverAddress;		// 受送达人地址
	private Date deliverSignTime;		// 受送达人签收时间
	private String judgeAssistant;		// 法官助理
	private String judgeAssistantPhone;		// 法官助理联系电话
	private Date communtitySignTime;		// 社区签收时间
	private String communtitySignId;		// 社区签收人员
	private String communtityId;		// 社区编号
	private String turnsOut;		// 是转派吗?1:是;2:不是
	private String turnsOutPeople;		// 转出人
	private Date gridSignTime;		// 网格员签收时间
	private String gridId;		// 签收网格员id
	private String caseStatus;		// 案件送达状态;1:社区专职人员签收;2:网格员签收;3:被送达人签收;4:邮政签收;5:法院送达中心人员签收
	private String emsId;		// 快递人员id
	private String emsPhone;		// 快递人员电话
	private Date emsSignTime;		// 快递人员揽件时间
	private String courtId;		// 法院签收人员
	private Date courtSignTime;		// 法院签收时间
	private String attribute1;		// 备用字段
	private String attribute2;		// attribute2
	private String attribute3;		// attribute3
	private String attribute4;		// attribute4

	public CaseInfo() {
		super();
	}

	public CaseInfo(String id){
		super(id);
	}

	@Length(min=0, max=64, message="案件id长度必须介于 0 和 64 之间")
	public String getCaId() {
		return caId;
	}

	public void setCaId(String caId) {
		this.caId = caId;
	}

	@Length(min=0, max=255, message="案号长度必须介于 0 和 255 之间")
	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	@Length(min=0, max=64, message="首程邮单号长度必须介于 0 和 64 之间")
	public String getFirstMail() {
		return firstMail;
	}

	public void setFirstMail(String firstMail) {
		this.firstMail = firstMail;
	}

	@Length(min=0, max=64, message="返程邮单号长度必须介于 0 和 64 之间")
	public String getReturnMail() {
		return returnMail;
	}

	public void setReturnMail(String returnMail) {
		this.returnMail = returnMail;
	}

	@Length(min=0, max=255, message="受送达人长度必须介于 0 和 255 之间")
	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	@Length(min=0, max=255, message="受送达人地址长度必须介于 0 和 255 之间")
	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeliverSignTime() {
		return deliverSignTime;
	}

	public void setDeliverSignTime(Date deliverSignTime) {
		this.deliverSignTime = deliverSignTime;
	}

	@Length(min=0, max=255, message="法官助理长度必须介于 0 和 255 之间")
	public String getJudgeAssistant() {
		return judgeAssistant;
	}

	public void setJudgeAssistant(String judgeAssistant) {
		this.judgeAssistant = judgeAssistant;
	}

	@Length(min=0, max=255, message="法官助理联系电话长度必须介于 0 和 255 之间")
	public String getJudgeAssistantPhone() {
		return judgeAssistantPhone;
	}

	public void setJudgeAssistantPhone(String judgeAssistantPhone) {
		this.judgeAssistantPhone = judgeAssistantPhone;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCommuntitySignTime() {
		return communtitySignTime;
	}

	public void setCommuntitySignTime(Date communtitySignTime) {
		this.communtitySignTime = communtitySignTime;
	}

	@Length(min=0, max=255, message="社区签收人员长度必须介于 0 和 255 之间")
	public String getCommuntitySignId() {
		return communtitySignId;
	}

	public void setCommuntitySignId(String communtitySignId) {
		this.communtitySignId = communtitySignId;
	}

	@Length(min=0, max=255, message="社区编号长度必须介于 0 和 255 之间")
	public String getCommuntityId() {
		return communtityId;
	}

	public void setCommuntityId(String communtityId) {
		this.communtityId = communtityId;
	}

	@Length(min=0, max=1, message="是转派吗?1:是;2:不是长度必须介于 0 和 1 之间")
	public String getTurnsOut() {
		return turnsOut;
	}

	public void setTurnsOut(String turnsOut) {
		this.turnsOut = turnsOut;
	}

	@Length(min=0, max=64, message="转出人长度必须介于 0 和 64 之间")
	public String getTurnsOutPeople() {
		return turnsOutPeople;
	}

	public void setTurnsOutPeople(String turnsOutPeople) {
		this.turnsOutPeople = turnsOutPeople;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getGridSignTime() {
		return gridSignTime;
	}

	public void setGridSignTime(Date gridSignTime) {
		this.gridSignTime = gridSignTime;
	}

	@Length(min=0, max=255, message="签收网格员id长度必须介于 0 和 255 之间")
	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	@Length(min=0, max=2, message="案件送达状态;1:社区专职人员签收;2:网格员签收;3:被送达人签收;4:邮政签收;5:法院送达中心人员签收长度必须介于 0 和 2 之间")
	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	@Length(min=0, max=255, message="快递人员id长度必须介于 0 和 255 之间")
	public String getEmsId() {
		return emsId;
	}

	public void setEmsId(String emsId) {
		this.emsId = emsId;
	}

	@Length(min=0, max=255, message="快递人员电话长度必须介于 0 和 255 之间")
	public String getEmsPhone() {
		return emsPhone;
	}

	public void setEmsPhone(String emsPhone) {
		this.emsPhone = emsPhone;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEmsSignTime() {
		return emsSignTime;
	}

	public void setEmsSignTime(Date emsSignTime) {
		this.emsSignTime = emsSignTime;
	}

	@Length(min=0, max=255, message="法院签收人员长度必须介于 0 和 255 之间")
	public String getCourtId() {
		return courtId;
	}

	public void setCourtId(String courtId) {
		this.courtId = courtId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCourtSignTime() {
		return courtSignTime;
	}

	public void setCourtSignTime(Date courtSignTime) {
		this.courtSignTime = courtSignTime;
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

}