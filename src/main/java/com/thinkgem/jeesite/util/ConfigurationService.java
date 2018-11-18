package com.thinkgem.jeesite.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
	@Value("${inTaskPath}")
	private String inTaskPath;
	@Value("${appId}")
	private String appId;
	@Value("${appSecret}")
	private String appSecret;
	@Value("${LsajPrefix}")
	private String lsajPrefix;
	@Value("${prefixNw}")
	private String prefixNw;
	@Value("${importPath}")
	private String importPath;

	@Value("${qcloudsmsAppid}")
	private int qcloudsmsAppid;
	@Value("${qcloudsmsAppkey}")
	private String qcloudsmsAppkey;
	@Value("${vcodeTemplateId}")
	private int vcodeTemplateId;


	public String getInTaskPath() {
		return inTaskPath;
	}

	public String getAppId() {
		return appId;
	}
	public String getAppSecret() {
		return appSecret;
	}

	public String getLsajPrefix() {
		return lsajPrefix;
	}

	public String getPrefixNw() {
		return prefixNw;
	}

	public String getImportPath() {
		return importPath;
	}

	public int getQcloudsmsAppid() {
		return qcloudsmsAppid;
	}

	public void setQcloudsmsAppid(int qcloudsmsAppid) {
		this.qcloudsmsAppid = qcloudsmsAppid;
	}

	public String getQcloudsmsAppkey() {
		return qcloudsmsAppkey;
	}

	public void setQcloudsmsAppkey(String qcloudsmsAppkey) {
		this.qcloudsmsAppkey = qcloudsmsAppkey;
	}

	public int getVcodeTemplateId() {
		return vcodeTemplateId;
	}

	public void setVcodeTemplateId(int vcodeTemplateId) {
		this.vcodeTemplateId = vcodeTemplateId;
	}
}
