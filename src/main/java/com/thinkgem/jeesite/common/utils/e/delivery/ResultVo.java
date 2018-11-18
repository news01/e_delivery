/******************************************************************************
 * Copyright (C) 2016  ShenZhen InnoPro Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳市精华隆安防设备有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.thinkgem.jeesite.common.utils.e.delivery;


/**
 * @Desc:前台结果值对象 
 * @author niushi
 * @date 2018年7月25日 下午5:34:43
 * @ClassName: ResultVo.java
 */
public class ResultVo {
	
	public static ResultVo resultVo;

	/**
	 * 错误代码
	 */
	private Integer errCode ;
	
	/**
	 * 错误消息
	 */
	private String errMsg ;
	
	/**
	 * 数据
	 */
	private Object result;
	
	
	/**
	 * 无参构造方法
	 */
	public ResultVo(){
		this.errCode = 0;
		this.errMsg = "success";
	}
	
	/**
	 * 有参构造方法
	 */
	public ResultVo(Integer errorCode, String errorMsg) {
		super();
		this.errCode = errorCode;
		this.errMsg = errorMsg;
	}

	
	/**
	 * 
	 * 描述：设置系统出错
	 */
	public void setSystemError(){
		this.errCode = ErrorCode.SYS_CODE_MISTAKE;
		this.errMsg  =  ErrorCode.SYS_MSG_MISTAKE;
	}
	
	/**
	 * 描述：设置session失效出错
	 */
	public void setSessionInvalid(){
		this.errCode = ErrorCode.SESSION_INVALID_CODE;
		this.errMsg  =  ErrorCode.SESSION_INVALID_MSG;
	}
	
	/**
	 * 描述：设置自定义问题
	 * @param code 代码 ;如果为null则赋值为-1
	 * @param msg 消息
	 */
	public void setCustomError(Integer code,String msg){
		this.errCode = code==null?-1:code;
		this.errMsg  =  msg;
	}
	
	/**
	 * 
	 * 描述 获取默认ResusltVo 此对象是正确对象
	 * @return
	 */
	public static ResultVo instance(){
		return new ResultVo();
	}
	
	
	/**
	 * 描述：设置系统出错
	 */
	public static ResultVo getSystemError(){
		return new ResultVo(ErrorCode.SYS_CODE_MISTAKE, ErrorCode.SYS_MSG_MISTAKE);
	}
	
	/**
	 * 描述：设置session失效出错
	 * @return
	 */
	public static ResultVo getSessionInvalid(){
		return new ResultVo(ErrorCode.SESSION_INVALID_CODE, ErrorCode.SESSION_INVALID_MSG);
	}
	

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @DESC:获取实例
	 * @author: niushi
	 * @date 2018年7月24日 下午3:43:48
	 * @param 
	 * @return
	 */
	public static ResultVo getInstance(){
		if(resultVo == null){
			resultVo = new ResultVo();
		}
		return resultVo;
	}
	
}
