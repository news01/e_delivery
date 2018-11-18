package com.thinkgem.jeesite.common.utils.e.delivery;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * @Desc:数据常量
 * @author niushi
 * @date 2018年7月24日 下午2:37:10
 * @ClassName: Constants.java
 */
public class Constants {

	public static final String USER_AUTH_KEY = "USER_INFO";// 用户登录会话key值session
	public static final String OPENID = "OPEN_ID";
	public static final String ADDRESS = "http://localhost:8080";

	public static Map<String, Object> sessionMap;

	static Map<String, Object> init() {
		if (sessionMap == null) {
			sessionMap = new HashMap<String, Object>();
		}
		return sessionMap;
	}

}
