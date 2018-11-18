package com.thinkgem.jeesite.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.web.header.writers.frameoptions.StaticAllowFromStrategy;

import com.thinkgem.jeesite.modules.userwechat.entity.AccessToken;

import net.sf.json.JSONObject;

public class StringUtil {
	public static String AppId = "wxa69e100a0bc720e1";
	private static Logger log = Logger.getLogger(StringUtil.class);
	//2293007571@qq.com微信小程序的AppSecret
	public static String AppSecret = "2df2874c1190126be417cb7482cbff44";
	
	//获取sessionId的url
	public static String SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
	
	/*public static String inTaskPath = "c:/in/task/";
	public static String repeat = "c:/in/task/repeat/";
	public static String error = "c:/in/task/error/";
	public static String history = "c:/in/task/history/";*/
	public static final String FIRST_INSTANCE = "FIRST_INSTANCE";
	public static final String FIRST_INSTANCE2SECOND_INSTANCE = "FIRST_INSTANCE2SECOND_INSTANCE";
	public static final String SECOND_INSTANCE2BACK = "SECOND_INSTANCE2BACK";
	public static final String SECOND_INSTANCE = "SECOND_INSTANCE";
	public static final String SHUTDOWN = "SHUTDOWN";
	public static final String TEMPLATE_ID = "WP6nrXrSX62nyXABByeph5zaHLOf8rbzis0lo9CMpHc";
	/***
	 * 获取发送模板消息的URL
	 * @param token
	 * @return
	 */
	public static String getSendTemplateUrl(AccessToken token){
		String Url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+token.getToken();
		return Url;
	}
	//windows環境下
	public static String[] cmdWindows(String cmd){
		String[] cmdWin = { "cmd", "/c", cmd };
		return cmdWin;
	}
	
	//linux環境下
	public static String[] cmdLinux(String cmd){
		String[] cmdLin = { "/bin/sh", "-c", cmd };
		return cmdLin;
	}
	
	public static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	/***
	 * 获取access_Token
	 */
	public static AccessToken getAccessToken(String appId, String secret) {
		// &appid="+COURT_APPID+"&secret="+COURT_APPSECRET;
		String token_url = StringUtil.GET_ACCESS_TOKEN_URL + "&appid=" + appId + "&secret=" + secret;
		JSONObject json = JSONObject.fromObject(new HttpClientUtil().Get(token_url));
		log.info("获取的内容：===================>" + json);
		AccessToken token = new AccessToken();
		token.setToken(json.getString("access_token"));
		token.setExpiresIn(json.getInt("expires_in"));
		return token;

	}
	//获取法院
	public String getSingle(String ss){
		String[] s = ss.split(",");
		List list = Arrays.asList(s);
		Set set = new HashSet(list);
		String[] r = (String[]) set.toArray(new String[0]);
		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < r.length; i++) {
			JSONObject json = new JSONObject();
			json.put("fy", r[i]);
			String st = "";
			if (i!=r.length-1) {
				st = json.toString()+",";
			}else{
				st = json.toString();
			}
			result.append(st);
		}
		result.append("]}");
		return result.toString();
		
	}
}
