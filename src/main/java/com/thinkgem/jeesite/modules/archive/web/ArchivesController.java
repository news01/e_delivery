/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archive.web;

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
import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.service.ArchivesService;

/**
 * 测试Controller
 * @author 朱明军
 * @version 2017-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/archive/archives")
public class ArchivesController extends BaseController {

	@Autowired
	private ArchivesService archivesService;
	
	@ModelAttribute
	public Archives get(@RequestParam(required=false) String id) {
		Archives entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = archivesService.get(id);
		}
		if (entity == null){
			entity = new Archives();
		}
		return entity;
	}
	
	@RequiresPermissions("archive:archives:view")
	@RequestMapping(value = {"list", ""})
	public String list(Archives archives, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Archives> page = archivesService.findPage(new Page<Archives>(request, response), archives); 
		model.addAttribute("page", page);
		return "modules/archive/archivesList";
	}

	@RequiresPermissions("archive:archives:view")
	@RequestMapping(value = "form")
	public String form(Archives archives, Model model) {
		model.addAttribute("archives", archives);
		return "modules/archive/archivesForm";
	}

	@RequiresPermissions("archive:archives:edit")
	@RequestMapping(value = "save")
	public String save(Archives archives, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, archives)){
			return form(archives, model);
		}
		archivesService.save(archives);
		addMessage(redirectAttributes, "保存保存成功");
		return "redirect:"+Global.getAdminPath()+"/archive/archives/?repage";
	}
	
	@RequiresPermissions("archive:archives:edit")
	@RequestMapping(value = "delete")
	public String delete(Archives archives, RedirectAttributes redirectAttributes) {
		archivesService.delete(archives);
		addMessage(redirectAttributes, "删除保存成功");
		return "redirect:"+Global.getAdminPath()+"/archive/archives/?repage";
	}

}