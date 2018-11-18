/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.wx.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 绑定微信账号的当事人Entity
 * @author zmj
 * @version 2017-09-16
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxDsr extends DataEntity<WxDsr> {
	
	private static final long serialVersionUID = 1L;
	private String lx;		// 当事人类型
	private String mc;		// 当事人姓名
	private String zjhm;		// 证件号码
	private String sjhm;		// 手机号码
	private String unionId;		// 微信unionId
	private String nickName;		// 微信号
	private String weappOpenId;		// 微信小程序OpenId
	private String weserviceOpenId;		// 微信服务号OpenId

	//0已关注  1已提交信息  2验证通过  8手动验证  9验证失败
	private char zt;


	public char getZt() {
		return zt;
	}

	public void setZt(char zt) {
		this.zt = zt;
	}

	public WxDsr() {
		super();
	}

	public WxDsr(String id){
		super(id);
	}

	@Length(min=0, max=15, message="当事人类型长度必须介于 0 和 15 之间")
	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}
	
	@Length(min=0, max=64, message="当事人姓名长度必须介于 0 和 64 之间")
	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}
	
	@Length(min=0, max=32, message="证件号码长度必须介于 0 和 32 之间")
	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
	
	@Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	
	@Length(min=0, max=32, message="微信unionId长度必须介于 0 和 32 之间")
	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
	@Length(min=0, max=32, message="微信小程序OpenId长度必须介于 0 和 32 之间")
	public String getWeappOpenId() {
		return weappOpenId;
	}

	public void setWeappOpenId(String weappOpenId) {
		this.weappOpenId = weappOpenId;
	}
	
	@Length(min=0, max=32, message="微信服务号OpenId长度必须介于 0 和 32 之间")
	public String getWeserviceOpenId() {
		return weserviceOpenId;
	}

	public void setWeserviceOpenId(String weserviceOpenId) {
		this.weserviceOpenId = weserviceOpenId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}