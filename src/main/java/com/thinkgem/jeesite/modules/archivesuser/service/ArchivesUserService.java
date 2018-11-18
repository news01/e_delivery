/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivesuser.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.archivesuser.entity.ArchivesUser;
import com.thinkgem.jeesite.modules.archivesuser.dao.ArchivesUserDao;

/**
 * 卷宗用户Service
 * @author 朱明军
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class ArchivesUserService extends CrudService<ArchivesUserDao, ArchivesUser> {

	public ArchivesUser get(String id) {
		return super.get(id);
	}
	
	public List<ArchivesUser> findList(ArchivesUser archivesUser) {
		return super.findList(archivesUser);
	}
	
	public Page<ArchivesUser> findPage(Page<ArchivesUser> page, ArchivesUser archivesUser) {
		return super.findPage(page, archivesUser);
	}
	
	@Transactional(readOnly = false)
	public void save(ArchivesUser archivesUser) {
		super.save(archivesUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(ArchivesUser archivesUser) {
		super.delete(archivesUser);
	}
	
}