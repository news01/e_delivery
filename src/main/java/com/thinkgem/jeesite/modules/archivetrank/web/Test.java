package com.thinkgem.jeesite.modules.archivetrank.web;

import java.util.List;

import javax.print.attribute.standard.RequestingUserName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.archive.dao.ArchivesDao;
import com.thinkgem.jeesite.modules.archivetrank.dao.ArchivesTrankDao;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

@Controller
@RequestMapping(value="test")
public class Test {
	@Autowired
	private SystemService service;
	@Autowired
	private ArchivesTrankDao trankDao;
	@Autowired
	private ArchivesDao archivesDao;

}
