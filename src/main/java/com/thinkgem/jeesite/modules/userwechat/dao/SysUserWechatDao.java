/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.dao;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;

/**
 * 测试DAO接口
 * @author 朱明军
 * @version 2017-06-02
 */
@MyBatisDao
public interface SysUserWechatDao extends CrudDao<SysUserWechat> {
	public SysUserWechat findOpenId(@Param("open_id") String openId);
	public void deletByOpenId(@Param("open_id") String openId);
	public SysUserWechat findByName(@Param("name") String name);
	public int insertSysUser(SysUserWechat sysUserWechat);
}