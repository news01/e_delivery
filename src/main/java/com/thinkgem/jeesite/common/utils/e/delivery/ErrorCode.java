package com.thinkgem.jeesite.common.utils.e.delivery;

/**
 * @Desc:返回错误code 
 * @author niushi
 * @date 2018年7月25日 下午5:39:48
 * @ClassName: ErrorCode.java
 */
public class ErrorCode {
	 
	/**
	 * 正常状态[0]代码
	 */
	public static final Integer SYS_CODE_SUCCESS = 0;
	/**
	 * 正常状态[0]消息
	 */
	public static final String SYS_MSG_SUCCESS = "success";
	
	/**
	 * 系统报错[444]代码
	 */
	public static final Integer SYS_CODE_MISTAKE = 444;
	/**
	 * 系统报错[444]消息
	 */
	public static final String SYS_MSG_MISTAKE = "系统出现了问题，请重试！";
	/**
	 * 首次登录系统[410]代码
	 */
	public static final Integer SYS_CODE_FIRST_LOGIN = 410;
	/**
	 * 首次登录系统[410]消息
	 */
	public static final String SYS_MSG_FIRST_LOGIN = "首次登录系统！";
	
	/**
	 * 无权限访问[445]代码
	 */
	public static final Integer INVALID_RIGHT_CODE = 445;
	
	/**
	 *无权限访问[445]消息
	 */
	public static final String INVALID_RIGHT_MSG = "Sorry,您无权限操作!";
	
	/**
	 * 普通报错或者未知报错[446]代码
	 */
	public static final Integer SYS_ERROR_COMMON_CODE = 446;
	/**
	 * 普通报错或者未知报错[446]]消息
	 */
	public static final String SYS_ERROR_COMMON_MSG = "操作报错，请重试！";

	/**
	 * 重新登录[404]
	 */
	public static final Integer AFRESH_LOGIN_CODE = 404;
	/**
	 * 重新登录[404]消息
	 */
	public static final String AFRESH_LOGIN_MSG = "重新登录";
	
	/**
	 * 会话实现[406]
	 */
	public static final Integer SESSION_INVALID_CODE = 406;
	/**
	 * 重新登录[406]消息
	 */
	public static final String SESSION_INVALID_MSG = "会话已失效，请重新登录！";
	
	/**
	 * 手机验证码不正确[501]
	 */
	public static final Integer VALID_MOBILE_CODE = 501;
	/**
	 * 手机验证码不正确[501]消息
	 */
	public static final String VALID_MOBILE_MSG = "手机验证码不正确";
	
	/**
	 *  强制退出[601]
	 */
	public static final Integer FORCED_OUT_CODE = 601;
	/**
	 *  强制退出[601]消息
	 */
	public static final String FORCED_OUT_MSG = "账号已被他人登录...";
	
	/**
	 *  强制登录[602]
	 */
	public static final Integer FORCED_LOGIN_CODE = 602;
	/**
	 *  强制登录[602]消息
	 */
	public static final String FORCED_LOGIN_MSG = "账号已在其它地方登录,是否强制进入！";
	

	// ////////////////////////////////////////////////////////
	// 用户企业类常量
	// 范围：1000-1999 
	// ////////////////////////////////////////////////////////

	/**
	 * 用户名不正确[1000]
	 */
	public static final Integer USER_CODE_1000 = 1000;

	/**
	 * 用户名不正确[1000]消息
	 */
	public static final String USER_MSG_1000 = "用户名不正确";

	/**
	 * 密码错误[1001]
	 */
	public static final Integer USER_CODE_1001 = 1001;

	/**
	 * 密码错误[1001]消息
	 */
	public static final String USER_MSG_1001 = "密码错误";


	/**
	 * 此用户已存在[1003]
	 */
	public static final Integer USER_CODE_1003 = 1003;

	/**
	 * 此用户已存在[1003]消息
	 */
	public static final String USER_MSG_1003 = "此用户已存在";
	
	
	/**
	 * 账号不存在[1000]
	 */
	public static final Integer USER_CODE_1010 = 1010;

	/**
	 * 账号不存在[1000]消息
	 */
	public static final String USER_MSG_1010 = "账号不存在";
	
	/**
	 * 未查询到数据[1009]
	 */
	public static final Integer USER_CODE_1009 = 1009;

	/**
	 * 未查询到数据[1009]消息
	 */
	public static final String USER_MSG_1009 = "未查询到数据";
	

}
