/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseinfo.service;

import java.util.List;

import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.dao.CaseInfoDao;

/**
 * 案件信息表Service
 *
 * @author niushi
 * @version 2018-08-10
 */
@Service
@Transactional(readOnly = true)
public class CaseInfoService extends CrudService<CaseInfoDao, CaseInfo> {

    @Autowired
    private CaseInfoDao caseInfoDao;

    public CaseInfo get(String id) {
        return super.get(id);
    }

    public List<CaseInfo> findList(CaseInfo caseInfo) {
        return super.findList(caseInfo);
    }

    public Page<CaseInfo> findPage(Page<CaseInfo> page, CaseInfo caseInfo) {
        return super.findPage(page, caseInfo);
    }

    @Transactional(readOnly = false)
    public void save(CaseInfo caseInfo) {
        super.save(caseInfo);
    }

    @Transactional(readOnly = false)
    public void delete(CaseInfo caseInfo) {
        super.delete(caseInfo);
    }

    @Transactional(readOnly = false)
    public Integer updateCaseInfoById(CaseInfo caseInfo) {
        return caseInfoDao.updateCaseInfoById(caseInfo);
    }

    /**
     * 描述：查询案件列表
     *
     * @return
     * @author： news
     * @time: 2018/8/14 20:14
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public List<CaseInfo> selectCaseInfo(CaseInfo caseInfo) {
        return caseInfoDao.selectCaseInfo(caseInfo);
    }

    /**
     * 描述：查询案件列表（减少查询字段）
     *
     * @return
     * @author： news
     * @time: 2018/8/14 20:14
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public List<CaseInfo> getCaseInfo(CaseInfo caseInfo) {
        return caseInfoDao.getCaseInfo(caseInfo);
    }

    /**
     * 描述：定时向网格员推送消息
     *
     * @return
     * @author： news
     * @time: 2018/9/11 18:43
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public List<CaseInfo> timePushFroGird(CaseInfo caseInfo) {
        return caseInfoDao.timePushFroGird(caseInfo);
    }


    /**
     * 描述：数据导入或则更新
     * @author： news
     * @time: 2018/9/19 17:01
     * @param:  * @param null
     * @return
     */
    @Transactional(readOnly = false)
    public int insertOrUpdate(CaseInfo caseInfo) {
        return caseInfoDao.insertOrUpdate(caseInfo);
    }

}