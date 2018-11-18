package com.thinkgem.jeesite.util;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

import com.thinkgem.jeesite.modules.sys.entity.User;

public class SMSMessageUtil {
	
	String _uc,_pwd;
	String _host = "http://kltx.sms10000.com.cn/sdk/SMS?";
	public SMSMessageUtil(String uc, String pwd){
		this._uc = uc;
		this._pwd = pwd;
	}
	public String get_pwd() {
		return this.MD5Encode(_pwd);
	}
	
	/**
	 * 初始化验证信息
	 * @return 
	 */
	
	public static void initAndSend(String phone,String vcode){
		SMSMessageUtil sms = new SMSMessageUtil("ZJFY01", "ZJFY0918");
		Logger log = Logger.getLogger(SMSMessageUtil.class);
		log.debug("验证码：    "+vcode);
		System.out.println(sms.sendSMS(phone.trim(), "【深圳法院】深圳市龙岗区人民法院E网送达平台登录验证码："+vcode+" \n先生/女士，您正在使用深圳龙岗法院E网送达平台。如不是您本人操作，请勿理会此信息。", "hc" + System.currentTimeMillis()));
	}
	/**
	 * 描述：发送派件短信提醒
	 * @author： news
	 * @time: 2018/9/15 10:57
	 * @param:  * @param null
	 * @return
	 */
	public static void sendDeliveryRemind(String phone,String name,String caseNames){
		SMSMessageUtil sms = new SMSMessageUtil("ZJFY01", "ZJFY0918");
		Logger log = Logger.getLogger(SMSMessageUtil.class);
		log.debug("任务列表：    "+caseNames);
		System.out.println(sms.sendSMS(phone.trim(), "【E网送达】"+name+"先生/女士,您在深圳市龙岗区人民法院E网送达平台有新的派件任务，案号为："+caseNames+"请及时到社区服务站领取并接受案件。", "hc" + System.currentTimeMillis()));
	}
	public static void initAndSend3(String phone,String content){
		SMSMessageUtil sms = new SMSMessageUtil("ZJFY01", "ZJFY0918");
		Logger log = Logger.getLogger(SMSMessageUtil.class);
		log.info("验证码：    "+content);
		System.out.println(sms.sendSMS(phone.trim(), "【深圳法院】深圳市南山区人民法院\n"+content, "hc" + System.currentTimeMillis()));
	}
	public static void initAndSend2(String phone,String caseNums,String officeName,String name,String jsName){
		SMSMessageUtil sms = new SMSMessageUtil("ZJFY01", "ZJFY0918");
		String content = "【深圳法院】深圳市南山区人民法院\n"+jsName+"先生/女士，请您到"+officeName+"的"+name+"处签收"+caseNums+"等卷宗";
		Logger log = Logger.getLogger(SMSMessageUtil.class);
		log.debug("内容：    "+content);
		sms.sendSMS(phone.trim(), content, "hc" + System.currentTimeMillis());
	}
	public static void initAndSendBack(String caseNums,User user){
		SMSMessageUtil sms = new SMSMessageUtil("ZJFY01", "ZJFY0918");
		String content = "【南山法院】深圳市南山区人民法院\n"+user.getName()+"先生/女士，您好！\n 您刚刚打印并签收了"+caseNums+"卷宗";
		Logger log = Logger.getLogger(SMSMessageUtil.class);
		log.debug("内容：    "+content);
		sms.sendSMS(user.getMobile().trim(), content, "hc" + System.currentTimeMillis());
	}
	/**
	 * 接收短信
	 * 
	 * @return String
	 */
	public String getMO() {
		String re = "";
		try {
			String moUrl = _host  + "cmd=getmo&uid=" + _uc + "&psw="
					+ this.get_pwd() + "";
			re = submit(moUrl);
		} catch (Exception ex) {

		}
		return re;
	}
	
	/***
	 * 发送短信
	 * @param mobiles
	 * @param cont
	 * @param msgid
	 * @return
	 */
	public String sendSMS(String mobiles, String cont, String msgid) {
		String re = "";
		try {
			cont = URLEncoder.encode(cont, "GBK"); // 短信内容需要编码
			String sendUrl = _host + "cmd=send&uid=" + _uc + "&psw="
					+ this.get_pwd() + "&mobiles=" + mobiles + "&msgid="
					+ msgid + "&msg=" + cont + " ";
			re = submit(sendUrl);
		} catch (Exception ex) {
		}
		return re;
	}
	
	/**
	 * 取发送状态
	 * @return 
	 * 
	 * @return String
	 */
	public   String getStatus() {
		String re = "";
		String getstatusUrl = _host + "cmd=getstatus&uid=" + _uc + "&psw="
				+ this.get_pwd() + "";
		re = submit(getstatusUrl);
		return re;
	}
	
	private String submit(String url) {
		String re = "";
		int byteMax = 50;
		try {
			URL myurl = new URL(url);
			URLConnection urlcon = myurl.openConnection();
			urlcon.setUseCaches(false);
			urlcon.connect();
			InputStream inputstream = urlcon.getInputStream();
			byte[] buff = new byte[byteMax];
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			int ins;
			while ((ins = inputstream.read(buff)) >= 0) {
				bytearrayoutputstream.write(buff, 0, ins);
			}
			re = new String(bytearrayoutputstream.toByteArray());
			re.trim();
		} catch (MalformedURLException ex) {
		} catch (IOException ex) {
			/** TODO Handle this exception */
		}

		return re;
	}
	
	private String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}
	public String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	private String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	public static void main(String[] args) {
		initAndSend("18664301621", "2334");
	}
	public static String Vcode(){
		String str = "";
		str += (int) (Math.random() * 9 + 1);
		for (int i = 0; i < 5; i++) {
			str += (int) (Math.random() * 10);

		} 
		return str;
	}
	
}
