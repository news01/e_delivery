/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.service;

import java.util.List;

import com.thinkgem.jeesite.modules.userwechat.dao.SysUserDao;
import com.thinkgem.jeesite.modules.userwechat.dao.SysUserDao2;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.web.SysUserController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 小程序用户表Service
 *
 * @author niushi
 * @version 2018-08-01
 */
@Service
@Transactional(readOnly = true)
public class SysUserService extends CrudService<SysUserDao, SysUser> {
    Logger logger = Logger.getLogger(SysUserService.class);

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserDao2 sysUserDao2;

    public SysUser get(String id) {
        return super.get(id);
    }

    public List<SysUser> findList(SysUser sysUser) {
        return super.findList(sysUser);
    }

    public Page<SysUser> findPage(Page<SysUser> page, SysUser sysUser) {
        return super.findPage(page, sysUser);
    }

    @Transactional(readOnly = false)
    public void save(SysUser sysUser) {
        super.save(sysUser);
    }

    /**
     * 描述：添加用户
     *
     * @return
     * @author： news
     * @time: 2018/8/7 13:46
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public int insertSysUser(SysUser sysUser) {
        return sysUserDao.insertSysUser(sysUser);
    }

    @Transactional(readOnly = false)
    public void delete(SysUser sysUser) {
        super.delete(sysUser);
    }

    /**
     * 描述：获取用户信息
     *
     * @return
     * @author： news
     * @time: 2018/8/1 19:44
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public SysUser get(SysUser sysUser) {
        return super.get(sysUser);
    }

    /**
     * 描述：根据id获取用户信息
     *
     * @return
     * @author： news
     * @time: 2018/8/7 14:27
     * @param: * @param null
     */
    public SysUser getSysUserByIdOrMobile(SysUser sysUser) {
        System.out.println("sysUser:" + sysUser);
        logger.info("sysUser:" + sysUser);
        return sysUserDao.getSysUserByIdOrMobile(sysUser);
    }

    /**
     * 描述：根据手机查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/13 20:54
     * @param: * @param null
     */
    public long getSysUserByMobile(SysUser sysUser) {
        long res = sysUserDao.getSysUserByMobile(sysUser.getMobile());
        logger.info("===========:" + res);
        return res;
    }

    /**
     * 描述：根据手机查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/13 20:54
     * @param: * @param null
     */
    public List<SysUser> selectsysuser(SysUser sysUser) {
        logger.info("sysUser:" + sysUser);
        return sysUserDao.selectsysuser(sysUser);
    }

    /**
     * 描述：根据id查询用户
     *
     * @return
     * @author： news
     * @time: 2018/9/13 20:54
     * @param: * @param null
     */
    public List<SysUser> getSysUserbyId(String id) {
        logger.info("sysUser:" + id);
        return sysUserDao2.getSysUserbyId(id);
    }

    /**
     * 描述：根据社区获取网格员
     *
     * @return
     * @author： news
     * @time: 2018/10/13 21:14
     * @param: * @param null
     */
    public List<SysUser> getSysUserByCommunityId(SysUser sysUser) {
        return sysUserDao2.getSysUserByCommunityId(sysUser);
    }

}