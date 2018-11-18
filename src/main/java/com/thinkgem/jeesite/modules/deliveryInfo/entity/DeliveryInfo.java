/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.deliveryInfo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

/**
 * 网格员送达信息Entity
 * @author niushi
 * @version 2018-08-15
 */
public class DeliveryInfo extends DataEntity<DeliveryInfo> {

	private static final long serialVersionUID = 1L;
	private String devId;		// 送达信息id
	private String caseId;		// 案件id
	private Integer devNum;		// 送达次数
	private Date devTime;		// 送达时间
	private String devTime2;		// 送达时间
	private byte[] devPhoto1;		// 送达拍照1
	private byte[] devPhoto2;		// 送达拍照2
	private byte[] devPhoto3;		// 送达拍照3
	private byte[] devVideo1;		// 送达录像1
	private byte[] devVideo2;		// 送达录像2
	private byte[] devVideo3;		// 送达录像3
	private String devStatus;
	private String comSign;
	private String devGird;
	private String proveStaff;		// 证明人员
	private String attribute1;		// 备用字段
	private String attribute2;		// attribute2
	private String attribute3;		// attribute3
	private String attribute4;		// attribute4

	private String attribute5;		// attribute5
	private String attribute6;		// attribute6
	private String attribute7;		// attribute7


	public DeliveryInfo() {
		super();
	}

	public DeliveryInfo(String id){
		super(id);
	}

	@Length(min=1, max=11, message="送达信息id长度必须介于 1 和 11 之间")
	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	@Length(min=0, max=64, message="案件id长度必须介于 0 和 64 之间")
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@Length(min=0, max=3, message="送达次数长度必须介于 0 和 3 之间")
	public Integer getDevNum() {
		return devNum;
	}

	public void setDevNum(Integer devNum) {
		this.devNum = devNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDevTime() {
		return devTime;
	}

	public void setDevTime(Date devTime) {
		this.devTime = devTime;
	}

	@Length(min=0, max=2000, message="送达拍照1长度必须介于 0 和 2000 之间")
	public byte[] getDevPhoto1() {
		return devPhoto1;
	}

	public void setDevPhoto1(byte[] devPhoto1) {
		this.devPhoto1 = devPhoto1;
	}

	@Length(min=0, max=2000, message="送达拍照2长度必须介于 0 和 2000 之间")
	public byte[] getDevPhoto2() {
		return devPhoto2;
	}

	public void setDevPhoto2(byte[] devPhoto2) {
		this.devPhoto2 = devPhoto2;
	}

	@Length(min=0, max=2000, message="送达拍照3长度必须介于 0 和 2000 之间")
	public byte[] getDevPhoto3() {
		return devPhoto3;
	}

	public void setDevPhoto3(byte[] devPhoto3) {
		this.devPhoto3 = devPhoto3;
	}

	@Length(min=0, max=2000, message="送达录像1长度必须介于 0 和 2000 之间")
	public byte[] getDevVideo1() {
		return devVideo1;
	}

	public void setDevVideo1(byte[] devVideo1) {
		this.devVideo1 = devVideo1;
	}

	@Length(min=0, max=2000, message="送达录像2长度必须介于 0 和 2000 之间")
	public byte[] getDevVideo2() {
		return devVideo2;
	}

	public void setDevVideo2(byte[] devVideo2) {
		this.devVideo2 = devVideo2;
	}

	@Length(min=0, max=2000, message="送达录像3长度必须介于 0 和 2000 之间")
	public byte[] getDevVideo3() {
		return devVideo3;
	}

	public void setDevVideo3(byte[] devVideo3) {
		this.devVideo3 = devVideo3;
	}

	@Length(min=0, max=255, message="证明人员长度必须介于 0 和 255 之间")
	public String getProveStaff() {
		return proveStaff;
	}

	public void setProveStaff(String proveStaff) {
		this.proveStaff = proveStaff;
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

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDevStatus() {
		return devStatus;
	}

	public void setDevStatus(String devStatus) {
		this.devStatus = devStatus;
	}


	public String getComSign() {
		return comSign;
	}

	public void setComSign(String comSign) {
		this.comSign = comSign;
	}

	public String getDevGird() {
		return devGird;
	}

	public void setDevGird(String devGird) {
		this.devGird = devGird;
	}

	public String getDevTime2() {
		return devTime2;
	}

	public void setDevTime2(String devTime2) {
		this.devTime2 = devTime2;
	}

	@Override
	public String toString() {
		return "DeliveryInfo{" +
				"devId='" + devId + '\'' +
				", caseId='" + caseId + '\'' +
				", devNum=" + devNum +
				", devTime='" + devTime + '\'' +
				", devPhoto1='" + devPhoto1 + '\'' +
				", devPhoto2='" + devPhoto2 + '\'' +
				", devPhoto3='" + devPhoto3 + '\'' +
				", devVideo1='" + devVideo1 + '\'' +
				", devVideo2='" + devVideo2 + '\'' +
				", devVideo3='" + devVideo3 + '\'' +
				", proveStaff='" + proveStaff + '\'' +
				", attribute1='" + attribute1 + '\'' +
				", attribute2='" + attribute2 + '\'' +
				", attribute3='" + attribute3 + '\'' +
				", attribute4='" + attribute4 + '\'' +
				'}';
	}
}