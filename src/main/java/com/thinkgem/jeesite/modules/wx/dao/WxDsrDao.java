/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.wx.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.wx.entity.WxDsr;

import java.util.List;

/**
 * 绑定微信账号的当事人DAO接口
 * @author zmj
 * @version 2017-09-16
 */
@MyBatisDao
public interface WxDsrDao extends CrudDao<WxDsr> {
	public List<WxDsr> findAuthenticatedList(WxDsr wxDsr);

	public WxDsr getByZjhm(WxDsr wxDsr);

    List<WxDsr> findPageFuzzy(WxDsr wxDsr);

	String selectIdCard(String idCard);

	WxDsr selectByZjhm(String zjhm);

	String selectUnIdByzjhm(String idCard);

	WxDsr getByUnionId(String unionId);
}