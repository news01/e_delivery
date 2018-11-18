package com.thinkgem.jeesite.util;

import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.thinkgem.jeesite.modules.userwechat.entity.AccessToken;
import com.thinkgem.jeesite.modules.userwechat.entity.TemplateMsgResult;
import com.thinkgem.jeesite.modules.userwechat.entity.WechatTemplateMsg;

import net.sf.json.JSONObject;

public class WechatUtil {
	private Logger log = Logger.getLogger(WechatUtil.class);
	/**
	 * 发送模板消息
	 */
	public String sendTemplate(JSONObject dataJson){
		TemplateMsgResult result = null;
		AccessToken token = AccessTokenUtil.getInstance().getAccessToken();
		String Url = StringUtil.getSendTemplateUrl(token);
		String name = dataJson.getString("name");
		String caseNum = dataJson.getString("caseNum");
		String openId = dataJson.getString("openId");
		String templateId = StringUtil.TEMPLATE_ID;
		String formId = System.currentTimeMillis()+"";
		String signTime = new TimeUtil().getDateTime(dataJson.getString("trankTime"));
		/**
		 * 组合data
		 */
		JSONObject message = new JSONObject();
		message.put("keyword1", name);
		message.put("keyword2", caseNum);
		message.put("keyword3", signTime);
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
		for (Object key : message.keySet()) {
			params.put(key.toString(), WechatTemplateMsg.item(message.getString(key.toString()), "#000000"));
			System.out.println(key+"=========>"+message.get(key));
		}
		log.info("模板内容=====>"+JSONObject.fromObject(params).toString());
		WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
		wechatTemplateMsg.setData(params);
		wechatTemplateMsg.setTemplate_id(templateId);
		wechatTemplateMsg.setTouser(openId);
		JSONObject param = JSONObject.fromObject(wechatTemplateMsg);
		String res = HttpClientUtil.httpPost(Url, param, false);
		return res;
	}
}
