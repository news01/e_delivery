/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivetrank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.service.ArchivesTrankService;

/**
 * 测试Controller
 * @author 朱明军
 * @version 2017-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/archivetrank/archivesTrank")
public class ArchivesTrankController extends BaseController {
	@Autowired
	private ArchivesTrankService archivesTrankService;
	
	@ModelAttribute
	public ArchivesTrank get(@RequestParam(required=false) String id) {
		ArchivesTrank entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = archivesTrankService.get(id);
		}
		if (entity == null){
			entity = new ArchivesTrank();
		}
		return entity;
	}
	
	@RequiresPermissions("archivetrank:archivesTrank:view")
	@RequestMapping(value = {"list", ""})
	public String list(ArchivesTrank archivesTrank, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArchivesTrank> page = archivesTrankService.findPage(new Page<ArchivesTrank>(request, response), archivesTrank); 
		model.addAttribute("page", page);
		return "modules/archivetrank/archivesTrankList";
	}

	@RequiresPermissions("archivetrank:archivesTrank:view")
	@RequestMapping(value = "form")
	public String form(ArchivesTrank archivesTrank, Model model) {
		model.addAttribute("archivesTrank", archivesTrank);
		return "modules/archivetrank/archivesTrankForm";
	}

	@RequiresPermissions("archivetrank:archivesTrank:edit")
	@RequestMapping(value = "save")
	public String save(ArchivesTrank archivesTrank, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, archivesTrank)){
			return form(archivesTrank, model);
		}
		archivesTrankService.save(archivesTrank);
		addMessage(redirectAttributes, "保存保存成功");
		return "redirect:"+Global.getAdminPath()+"/archivetrank/archivesTrank/?repage";
	}
	
	@RequiresPermissions("archivetrank:archivesTrank:edit")
	@RequestMapping(value = "delete")
	public String delete(ArchivesTrank archivesTrank, RedirectAttributes redirectAttributes) {
		archivesTrankService.delete(archivesTrank);
		addMessage(redirectAttributes, "删除保存成功");
		return "redirect:"+Global.getAdminPath()+"/archivetrank/archivesTrank/?repage";
	}

}