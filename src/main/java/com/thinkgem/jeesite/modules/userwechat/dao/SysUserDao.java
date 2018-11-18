/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小程序用户表DAO接口
 *
 * @author niushi
 * @version 2018-08-01
 */
@MyBatisDao
public interface SysUserDao extends CrudDao<SysUser> {

    /**
     * 描述：添加用户
     *
     * @return
     * @author： news
     * @time: 2018/8/7 13:45
     * @param: * @param null
     */
    int insertSysUser(SysUser sysUser);

    /**
     * 描述：根据id获取用户信息
     *
     * @return
     * @author： news
     * @time: 2018/8/7 14:22
     * @param: * @param null
     */
    SysUser getSysUserByIdOrMobile(SysUser sysUser);

    /**
     * 描述：根据手机查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/13 23:26
     * @param: * @param null
     */
    long getSysUserByMobile(String mobile);



    /**
     * 描述：根据手机查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/14 9:36
     * @param: * @param null
     */
    List<SysUser> selectsysuser(SysUser sysUser);


}