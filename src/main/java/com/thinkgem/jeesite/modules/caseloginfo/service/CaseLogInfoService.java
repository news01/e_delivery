/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseloginfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogInfo;
import com.thinkgem.jeesite.modules.caseloginfo.dao.CaseLogInfoDao;

/**
 * 案件物流信息表Service
 *
 * @author niushi
 * @version 2018-08-22
 */
@Service
@Transactional(readOnly = true)
public class CaseLogInfoService extends CrudService<CaseLogInfoDao, CaseLogInfo> {

    @Autowired
    private CaseLogInfoDao caseLogInfoDao;

    public CaseLogInfo get(String id) {
        return super.get(id);
    }

    public List<CaseLogInfo> findList(CaseLogInfo caseLogInfo) {
        return super.findList(caseLogInfo);
    }

    public Page<CaseLogInfo> findPage(Page<CaseLogInfo> page, CaseLogInfo caseLogInfo) {
        return super.findPage(page, caseLogInfo);
    }

    @Transactional(readOnly = false)
    public void save(CaseLogInfo caseLogInfo) {
        super.save(caseLogInfo);
    }

    @Transactional(readOnly = false)
    public void delete(CaseLogInfo caseLogInfo) {
        super.delete(caseLogInfo);
    }

    /**
     * 描述：添加案件物流信息
     *
     * @return
     * @author： news
     * @time: 2018/8/24 14:28
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public Integer insertCaseDelInfo(CaseLogInfo caseLogInfo) {
        return caseLogInfoDao.insertCaseDelInfo(caseLogInfo);
    }

    /**
     * 描述：查询案件物流信息
     * @author： news
     * @time: 2018/9/11 20:16
     * @param:  * @param null
     * @return
     */
    @Transactional(readOnly = false)
    public List<CaseLogInfo> getCaseLogInfo(CaseLogInfo caseLogInfo){
        return caseLogInfoDao.getCaseLogInfo(caseLogInfo);
    }

}