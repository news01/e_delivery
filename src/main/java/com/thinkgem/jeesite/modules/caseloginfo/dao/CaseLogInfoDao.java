/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseloginfo.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogInfo;

import java.util.List;

/**
 * 案件物流信息表DAO接口
 * @author niushi
 * @version 2018-08-22
 */
@MyBatisDao
public interface CaseLogInfoDao extends CrudDao<CaseLogInfo> {

    /**
     * 描述：添加案件物流信息
     * @author： news
     * @time: 2018/8/24 14:26
     * @param:  * @param null
     * @return
     */
    Integer insertCaseDelInfo(CaseLogInfo caseLogInfo);
    
    /**
     * 描述：查询案件物流信息
     * @author： news
     * @time: 2018/9/11 20:14
     * @param:  * @param null
     * @return
     */
    List<CaseLogInfo> getCaseLogInfo(CaseLogInfo caseLogInfo);



}