/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.dayset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.dayset.entity.MaxHaveDays;
import com.thinkgem.jeesite.modules.dayset.dao.MaxHaveDaysDao;

/**
 * 配置时间Service
 * @author 朱明军
 * @version 2017-10-31
 */
@Service
@Transactional(readOnly = true)
public class MaxHaveDaysService extends CrudService<MaxHaveDaysDao, MaxHaveDays> {
	@Autowired
	private MaxHaveDaysDao dao;
	public List<MaxHaveDays> findAllDays(){
		return dao.findAllDays();
	}
	public MaxHaveDays getOfficeId(String officeId){
		return dao.findByOfficeId(officeId);
	}
	
	public MaxHaveDays get(String id) {
		return super.get(id);
	}
	
	public List<MaxHaveDays> findList(MaxHaveDays maxHaveDays) {
		return super.findList(maxHaveDays);
	}
	
	public Page<MaxHaveDays> findPage(Page<MaxHaveDays> page, MaxHaveDays maxHaveDays) {
		return super.findPage(page, maxHaveDays);
	}
	
	@Transactional(readOnly = false)
	public void save(MaxHaveDays maxHaveDays) {
		super.save(maxHaveDays);
	}
	
	@Transactional(readOnly = false)
	public void delete(MaxHaveDays maxHaveDays) {
		super.delete(maxHaveDays);
	}
	
}