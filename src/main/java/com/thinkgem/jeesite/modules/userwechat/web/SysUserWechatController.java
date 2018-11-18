/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.web;

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
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserWechatService;

/**
 * 测试Controller
 * @author 朱明军
 * @version 2017-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/userwechat/sysUserWechat")
public class SysUserWechatController extends BaseController {

	@Autowired
	private SysUserWechatService sysUserWechatService;
	@ModelAttribute
	public SysUserWechat get(@RequestParam(required=false) String id) {
		SysUserWechat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysUserWechatService.get(id);
		}
		if (entity == null){
			entity = new SysUserWechat();
		}
		return entity;
	}
	
	@RequiresPermissions("userwechat:sysUserWechat:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysUserWechat sysUserWechat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysUserWechat> page = sysUserWechatService.findPage(new Page<SysUserWechat>(request, response), sysUserWechat); 
		model.addAttribute("page", page);
		return "modules/userwechat/sysUserWechatList";
	}

	@RequiresPermissions("userwechat:sysUserWechat:view")
	@RequestMapping(value = "form")
	public String form(SysUserWechat sysUserWechat, Model model) {
		model.addAttribute("sysUserWechat", sysUserWechat);
		return "modules/userwechat/sysUserWechatForm";
	}

	@RequiresPermissions("userwechat:sysUserWechat:edit")
	@RequestMapping(value = "save")
	public String save(SysUserWechat sysUserWechat, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysUserWechat)){
			return form(sysUserWechat, model);
		}
		sysUserWechatService.save(sysUserWechat);
		addMessage(redirectAttributes, "保存保存成功");
		return "redirect:"+Global.getAdminPath()+"/userwechat/sysUserWechat/?repage";
	}
	
	@RequiresPermissions("userwechat:sysUserWechat:edit")
	@RequestMapping(value = "delete")
	public String delete(SysUserWechat sysUserWechat, RedirectAttributes redirectAttributes) {
		sysUserWechatService.delete(sysUserWechat);
		addMessage(redirectAttributes, "删除保存成功");
		return "redirect:"+Global.getAdminPath()+"/userwechat/sysUserWechat/?repage";
	}
	
	
}