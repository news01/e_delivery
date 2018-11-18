/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivesuser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.archivesuser.entity.ArchivesUser;

/**
 * 卷宗用户DAO接口
 * @author 朱明军
 * @version 2017-05-18
 */
@MyBatisDao
public interface ArchivesUserDao extends CrudDao<ArchivesUser> {
	public ArchivesUser findByOpenId(@Param("openId") String openId);
	public ArchivesUser findByUserName(@Param("userName") String userName);
	public void updateOpenId(@Param("openId") String openId,@Param("userName") String userName);
	public String[] findOffice();
	public String[] findByFy(@Param("office") String office);
	public List<ArchivesUser> findByFullOffice(@Param("office1") String office1,@Param("office2") String office2);
}