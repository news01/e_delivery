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
public interface SysUserDao2 extends CrudDao<SysUser> {

    /**
     * 描述：根据手机查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/14 9:36
     * @param: * @param null
     */
    List<SysUser> getSysUserbyId(String id);

    /**
     * 描述：根据社区获取网格员
     *
     * @return
     * @author： news
     * @time: 2018/8/13 15:47
     * @param: * @param null
     */
    List<SysUser> getSysUserByCommunityId(SysUser sysUser);


}