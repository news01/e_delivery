package com.thinkgem.jeesite.modules.userwechat.entity;

public class AccessToken {
	private String token;//凭证
    private int expiresIn;//凭证有效时间
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
