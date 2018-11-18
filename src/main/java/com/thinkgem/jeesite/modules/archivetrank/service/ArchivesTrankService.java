/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivetrank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.dao.ArchivesTrankDao;

/**
 * 测试Service
 * @author 朱明军
 * @version 2017-06-02
 */
@Service
@Transactional(readOnly = true)
public class ArchivesTrankService extends CrudService<ArchivesTrankDao, ArchivesTrank> {
	public ArchivesTrank get(String id) {
		ArchivesTrank archivesTrank = super.get(id);
		return archivesTrank;
	}
	public List<ArchivesTrank> findList(ArchivesTrank archivesTrank) {
		return super.findList(archivesTrank);
	}
	
	public Page<ArchivesTrank> findPage(Page<ArchivesTrank> page, ArchivesTrank archivesTrank) {
		return super.findPage(page, archivesTrank);
	}
	
	@Transactional(readOnly = false)
	public void save(ArchivesTrank archivesTrank) {
		super.save(archivesTrank);
	}
	
	@Transactional(readOnly = false)
	public void delete(ArchivesTrank archivesTrank) {
		super.delete(archivesTrank);
	}
	
}