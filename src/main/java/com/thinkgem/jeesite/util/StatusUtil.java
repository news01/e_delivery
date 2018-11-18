package com.thinkgem.jeesite.util;

public class StatusUtil {
	public String GetSpcxZt(String companyId, String spcx) {
		if (companyId .equals( "J33") && spcx.equals( "10")) {
			return StringUtil.FIRST_INSTANCE;
		} else if (companyId.equals( "J30") && spcx .equals( "10")) {
			return StringUtil.FIRST_INSTANCE2SECOND_INSTANCE;
		} else if (companyId.equals( "J30") && spcx.equals( "20")) {
			return StringUtil.SECOND_INSTANCE;
		} else if (companyId.equals("J33") && spcx.equals("20")) {
			return StringUtil.SECOND_INSTANCE2BACK;
		} else if (spcx.equals( "999")) {
			return StringUtil.SHUTDOWN;
		} else {
			return "";
		}
	}

	public String getRemark(String status) {
		if (status == StringUtil.FIRST_INSTANCE) {
			return "10";
		} else if (status == StringUtil.FIRST_INSTANCE2SECOND_INSTANCE) {
			return "20";
		} else if (status == StringUtil.SECOND_INSTANCE) {
			return "20";
		} else if (status == StringUtil.SECOND_INSTANCE2BACK) {
			return "30";
		} else if (status == StringUtil.SHUTDOWN) {
			return "999";
		} else {
			return "";
		}
	}
}
