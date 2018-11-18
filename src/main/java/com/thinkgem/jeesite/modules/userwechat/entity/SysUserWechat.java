/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 测试Entity
 * @author 朱明军
 * @version 2017-06-02
 */
public class SysUserWechat extends DataEntity<SysUserWechat> {
	
	private static final long serialVersionUID = 1L;
	private SysUser user;		// user_id
	private String openId;		// open_id
	
	public SysUserWechat() {
		super();
	}

	public SysUserWechat(String id){
		super(id);
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="open_id长度必须介于 0 和 255 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	
	
}