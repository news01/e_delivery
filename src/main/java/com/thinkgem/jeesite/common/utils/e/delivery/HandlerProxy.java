package com.thinkgem.jeesite.common.utils.e.delivery;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.util.SessionUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * @Desc: 前端handle代理
 * @author niushi
 * @date 2018年7月24日 上午11:38:23
 * @ClassName: HandlerProxy.java
 */
public class HandlerProxy {

	private static Logger LOGGER = Logger.getLogger(HandlerProxy.class);

	/**
	 * @DESC: 同步请求处理
	 * @author: niushi
	 * @date 2018年7月24日 上午11:48:01
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static ModelAndView assemble(ControlHandler controlHandler,
			HttpServletRequest request, HttpServletResponse response,
			String sessionId) {
		try {
			return controlHandler.handler(request, response,
					getUserInfo(sessionId), null, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView modelAndView = new ModelAndView("");
			return modelAndView;
		}
	}

	/**
	 * @DESC:异步请求处理
	 * @author: niushi
	 * @date 2018年7月24日 下午2:50:09
	 * @param
	 * @return
	 */
	public static void assembleAjax(ControlHandler handler,
			HttpServletRequest request, HttpServletResponse response,
			String sessionId) {
		ResultVo resultVo = ResultVo.instance();
		try {
			handler.handler(request, response, getUserInfo(sessionId),
					resultVo, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outData(JSONUtil.toFormatJSON(resultVo), response);
	}

	/**
	 * @DESC:输出数据
	 * @author: niushi
	 * @date 2018年7月24日 下午3:45:31
	 * @param
	 * @return
	 */
	private static void outData(String data, HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = null;
		try {
			// 输出数据
			writer = response.getWriter();
			writer.print(data);
		} catch (IOException e) {
			LOGGER.error("输出数据出错", e);
			e.printStackTrace();
		} finally {
			// 关闭流
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * @DESC:获取用户信息
	 * @author: niushi
	 * @date 2018年7月24日 上午11:47:05
	 * @param
	 * @return
	 */
	public static SysUser getUserInfo(String sessionId) {
		System.out.println(sessionId);

		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		if (session != null) {
			SysUser user = (SysUser) session.getAttribute(Constants.USER_AUTH_KEY);
			return user;

		}
		return null;

	}

}
