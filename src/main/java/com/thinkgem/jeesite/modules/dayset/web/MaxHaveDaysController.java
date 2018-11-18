/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.dayset.web;

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
import com.thinkgem.jeesite.modules.dayset.entity.MaxHaveDays;
import com.thinkgem.jeesite.modules.dayset.service.MaxHaveDaysService;

/**
 * 配置时间Controller
 * @author 朱明军
 * @version 2017-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/dayset/maxHaveDays")
public class MaxHaveDaysController extends BaseController {

	@Autowired
	private MaxHaveDaysService maxHaveDaysService;
	
	@ModelAttribute
	public MaxHaveDays get(@RequestParam(required=false) String id) {
		MaxHaveDays entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = maxHaveDaysService.get(id);
		}
		if (entity == null){
			entity = new MaxHaveDays();
		}
		return entity;
	}
	
	@RequiresPermissions("dayset:maxHaveDays:view")
	@RequestMapping(value = {"list", ""})
	public String list(MaxHaveDays maxHaveDays, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MaxHaveDays> page = maxHaveDaysService.findPage(new Page<MaxHaveDays>(request, response), maxHaveDays); 
		model.addAttribute("page", page);
		return "modules/dayset/maxHaveDaysList";
	}

	@RequiresPermissions("dayset:maxHaveDays:view")
	@RequestMapping(value = "form")
	public String form(MaxHaveDays maxHaveDays, Model model) {
		model.addAttribute("maxHaveDays", maxHaveDays);
		return "modules/dayset/maxHaveDaysForm";
	}

	@RequiresPermissions("dayset:maxHaveDays:edit")
	@RequestMapping(value = "save")
	public String save(MaxHaveDays maxHaveDays, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, maxHaveDays)){
			return form(maxHaveDays, model);
		}
		maxHaveDaysService.save(maxHaveDays);
		addMessage(redirectAttributes, "保存保存时间成功成功");
		return "redirect:"+Global.getAdminPath()+"/dayset/maxHaveDays/?repage";
	}
	
	@RequiresPermissions("dayset:maxHaveDays:edit")
	@RequestMapping(value = "delete")
	public String delete(MaxHaveDays maxHaveDays, RedirectAttributes redirectAttributes) {
		maxHaveDaysService.delete(maxHaveDays);
		addMessage(redirectAttributes, "删除保存时间成功成功");
		return "redirect:"+Global.getAdminPath()+"/dayset/maxHaveDays/?repage";
	}

}