/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archive.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.dao.ArchivesDao;

/**
 * 测试Service
 * @author 朱明军
 * @version 2017-06-02
 */
@Service
@Transactional(readOnly = true)
public class ArchivesService extends CrudService<ArchivesDao, Archives> {

	public Archives get(String id) {
		return super.get(id);
	}
	
	public List<Archives> findList(Archives archives) {
		return super.findList(archives);
	}
	
	public Page<Archives> findPage(Page<Archives> page, Archives archives) {
		return super.findPage(page, archives);
	}
	
	@Transactional(readOnly = false)
	public void save(Archives archives) {
		super.save(archives);
	}
	
	@Transactional(readOnly = false)
	public void delete(Archives archives) {
		super.delete(archives);
	}
	
	@Transactional(readOnly = false)
	public void save1(Archives archives) {
		super.save1(archives);
	}
	
	@Transactional(readOnly = false)
	public void save2(Archives archives) {
		super.save2(archives);
	}
	
}