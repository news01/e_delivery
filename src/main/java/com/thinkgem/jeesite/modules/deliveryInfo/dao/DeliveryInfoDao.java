/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.deliveryInfo.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;

import java.util.List;

/**
 * 网格员送达信息DAO接口
 *
 * @author niushi
 * @version 2018-08-15
 */
@MyBatisDao
public interface DeliveryInfoDao extends CrudDao<DeliveryInfo> {

    /**
     * 描述：查询案件的送达信息
     * @return
     * @author： news
     * @time: 2018/8/15 15:25
     * @param: * @param null
     */
    List<DeliveryInfo> getDelInfoByCaseId(DeliveryInfo deliveryInfo);

    /**
     * 描述：新增送达信息
     * @author： news
     * @time: 2018/8/16 16:34
     * @param:  * @param null
     * @return
     */
    int insertDeliveryInfo(DeliveryInfo deliveryInfo);
    
    /**
     * 描述：修改送达信息
     * @author： news
     * @time: 2018/8/16 18:18
     * @param:  * @param null
     * @return
     */
    int updateDeliveryInfo(DeliveryInfo deliveryInfo);

    /**
     * 描述：获取最新的一条记录
     * @author： news
     * @time: 2018/10/12 16:01
     * @param:  * @param null
     * @return
     */
    DeliveryInfo getTheLastDev(DeliveryInfo deliveryInfo);
    
    /**
     * 描述：修改送达信息,避免重复提交
     * @author： news
     * @time: 2018/11/7 9:58
     * @param:  * @param null
     * @return
     */
    int updateDevInfo(DeliveryInfo deliveryInfo);

    /**
     * 描述：获取deliveryInfo
     * @author： news
     * @time: 2018/11/7 10:08
     * @param:  * @param null
     * @return
     */
    List<DeliveryInfo> getDevInfo(DeliveryInfo deliveryInfo);

}