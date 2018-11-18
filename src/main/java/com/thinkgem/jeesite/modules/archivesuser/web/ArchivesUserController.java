/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivesuser.web;

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
import com.thinkgem.jeesite.modules.archivesuser.entity.ArchivesUser;
import com.thinkgem.jeesite.modules.archivesuser.service.ArchivesUserService;

/**
 * 卷宗用户Controller
 * @author 朱明军
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/archivesuser/archivesUser")
public class ArchivesUserController extends BaseController {

	@Autowired
	private ArchivesUserService archivesUserService;
	
	@ModelAttribute
	public ArchivesUser get(@RequestParam(required=false) String id) {
		ArchivesUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = archivesUserService.get(id);
		}
		if (entity == null){
			entity = new ArchivesUser();
		}
		return entity;
	}
	
	@RequiresPermissions("archivesuser:archivesUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(ArchivesUser archivesUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArchivesUser> page = archivesUserService.findPage(new Page<ArchivesUser>(request, response), archivesUser); 
		model.addAttribute("page", page);
		return "modules/archivesuser/archivesUserList";
	}

	@RequiresPermissions("archivesuser:archivesUser:view")
	@RequestMapping(value = "form")
	public String form(ArchivesUser archivesUser, Model model) {
		model.addAttribute("archivesUser", archivesUser);
		return "modules/archivesuser/archivesUserForm";
	}

	@RequiresPermissions("archivesuser:archivesUser:edit")
	@RequestMapping(value = "save")
	public String save(ArchivesUser archivesUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, archivesUser)){
			return form(archivesUser, model);
		}
		archivesUserService.save(archivesUser);
		addMessage(redirectAttributes, "保存保存成功");
		return "redirect:"+Global.getAdminPath()+"/archivesuser/archivesUser/?repage";
	}
	
	@RequiresPermissions("archivesuser:archivesUser:edit")
	@RequestMapping(value = "delete")
	public String delete(ArchivesUser archivesUser, RedirectAttributes redirectAttributes) {
		archivesUserService.delete(archivesUser);
		addMessage(redirectAttributes, "删除保存成功");
		return "redirect:"+Global.getAdminPath()+"/archivesuser/archivesUser/?repage";
	}

}