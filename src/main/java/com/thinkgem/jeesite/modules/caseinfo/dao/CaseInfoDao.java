/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseinfo.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;

import java.util.List;

/**
 * 案件信息表DAO接口
 *
 * @author niushi
 * @version 2018-08-10
 */
@MyBatisDao
public interface CaseInfoDao extends CrudDao<CaseInfo> {

    /**
     * 描述：修改案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/13 18:43
     * @param: * @param null
     */
    Integer updateCaseInfoById(CaseInfo caseInfo);

    /**
     * 描述：查询案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/14 19:52
     * @param: * @param null
     */
    List<CaseInfo> selectCaseInfo(CaseInfo caseInfo);

    /**
     * 描述：查询案件信息(减少查询字段)
     *
     * @return
     * @author： news
     * @time: 2018/9/8 14:45
     * @param: * @param null
     */
    List<CaseInfo> getCaseInfo(CaseInfo caseInfo);

    /**
     * 描述：定时向网格员推送消息
     *
     * @return
     * @author： news
     * @time: 2018/9/11 18:42
     * @param: * @param null
     */
    List<CaseInfo> timePushFroGird(CaseInfo caseInfo);

    /**
     * 描述：数据导入或者更新
     *
     * @return
     * @author： news
     * @time: 2018/9/19 16:59
     * @param: * @param null
     */
    int insertOrUpdate(CaseInfo caseInfo);

}