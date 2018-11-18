package com.thinkgem.jeesite.modules.userwechat.entity;

import java.util.TreeMap;

import net.sf.json.JSONObject;

public class WechatTemplateMsg {
	/**
	 * 接受者openId
	 */
	private String touser;
	
	/**
	 * 模板ID
	 */
	private String template_id;
	
	/**
	 * 模板跳转链接
	 */
	private String url;
	/**
	 * 小程序跳转链接
	 */
	private JSONObject miniprogram;
	/**
	 * data数据
	 */
	private TreeMap<String, TreeMap<String,String>> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public JSONObject getMiniprogram() {
		return miniprogram;
	}

	public void setMiniprogram(JSONObject miniprogram) {
		this.miniprogram = miniprogram;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}

	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}
	
	/**
	 * @param value
	 * @param color(可不填)
	 * @return
	 */
	public static TreeMap<String, String> item(String value, String color) {  
        TreeMap<String, String> params = new TreeMap<String, String>();  
        params.put("value", value);  
        params.put("color", color);  
        return params;  
    }  
}
