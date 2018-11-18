package com.thinkgem.jeesite.util;

import com.thinkgem.jeesite.modules.userwechat.entity.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class AccessTokenUtil {
	/***
	 * 单例模式，保存access_token
	 */
	private AccessToken accessToken;
	private static AccessTokenUtil accessTokenUtil;
	@Autowired
	ConfigurationService configurationService;
	
	private AccessTokenUtil() {
		accessToken = StringUtil.getAccessToken(configurationService.getAppId(), configurationService.getAppSecret());
		initThread();
	}
	/**
	 * 获取SingleAccessToken对象
	 * 
	 * @return
	 */
	public  static AccessTokenUtil getInstance() {
		if(accessTokenUtil==null){
			synchronized(AccessTokenUtil.class){
				if (accessTokenUtil == null) {
					accessTokenUtil = new AccessTokenUtil();
				}
			}
		}
		return accessTokenUtil;

	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	private void initThread() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 睡眠7000秒
					Thread.sleep(1000 * 1000);
					accessTokenUtil = null;

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}
	
}
