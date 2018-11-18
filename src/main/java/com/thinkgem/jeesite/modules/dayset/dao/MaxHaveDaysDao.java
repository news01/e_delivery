/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.dayset.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.dayset.entity.MaxHaveDays;

/**
 * 配置时间DAO接口
 * @author 朱明军
 * @version 2017-10-31
 */
@MyBatisDao
public interface MaxHaveDaysDao extends CrudDao<MaxHaveDays> {
	public MaxHaveDays findByOfficeId(@Param("officeId") String officeId);
	public List<MaxHaveDays> findAllDays();
}