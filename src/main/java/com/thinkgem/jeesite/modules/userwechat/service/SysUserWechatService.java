/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.dao.SysUserWechatDao;

/**
 * 测试Service
 * @author 朱明军
 * @version 2017-06-02
 */
@Service
@Transactional(readOnly = true)
public class SysUserWechatService extends CrudService<SysUserWechatDao, SysUserWechat> {
	@Autowired 
	private SysUserWechatDao dao;
	public SysUserWechat get(String id) {
		return super.get(id);
	}
	
	public List<SysUserWechat> findList(SysUserWechat sysUserWechat) {
		return super.findList(sysUserWechat);
	}
	
	public Page<SysUserWechat> findPage(Page<SysUserWechat> page, SysUserWechat sysUserWechat) {
		return super.findPage(page, sysUserWechat);
	}
	
	@Transactional(readOnly = false)
	public void save(SysUserWechat sysUserWechat) {
		super.save(sysUserWechat);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysUserWechat sysUserWechat) {
		super.delete(sysUserWechat);
	}
	public SysUserWechat findByName(String name){
		return dao.findByName(name);
	}

	@Transactional(readOnly = false)
	public int insertSysUser(SysUserWechat sysUserWechat) {
		return dao.insertSysUser(sysUserWechat);
	}
}