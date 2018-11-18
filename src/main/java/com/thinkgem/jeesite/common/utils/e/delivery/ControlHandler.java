package com.thinkgem.jeesite.common.utils.e.delivery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.modules.sys.entity.User;


/**
 * @Desc: 前端控制业务处理handler
 * @author niushi
 * @date 2018年7月24日 上午11:33:33
 * @ClassName: ControlHandler.java
 */
public interface ControlHandler {

	/**
	 * 
	 * 描述：业务handler
	 * @author niushi
	 * @date 2018年7月24日 上午11:33:33
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws Exception
	 */
	ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception;

	
}
