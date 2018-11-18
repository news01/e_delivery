/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.deliveryInfo.service;

import java.util.List;

import com.thinkgem.jeesite.modules.deliveryInfo.dao.DeliveryInfoDao;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 网格员送达信息Service
 *
 * @author niushi
 * @version 2018-08-15
 */
@Service
@Transactional(readOnly = true)
public class DeliveryInfoService extends CrudService<DeliveryInfoDao, DeliveryInfo> {

    @Autowired
    private DeliveryInfoDao deliveryInfoDao;

    public DeliveryInfo get(String id) {
        return super.get(id);
    }

    public List<DeliveryInfo> findList(DeliveryInfo deliveryInfo) {
        return super.findList(deliveryInfo);
    }

    public Page<DeliveryInfo> findPage(Page<DeliveryInfo> page, DeliveryInfo deliveryInfo) {
        return super.findPage(page, deliveryInfo);
    }

    @Transactional(readOnly = false)
    public void save(DeliveryInfo deliveryInfo) {
        super.save(deliveryInfo);
    }

    @Transactional(readOnly = false)
    public void delete(DeliveryInfo deliveryInfo) {
        super.delete(deliveryInfo);
    }

    /**
     * 描述：查询案件送达信息
     *
     * @return
     * @author： news
     * @time: 2018/8/15 15:26
     * @param: * @param null
     */
    public List<DeliveryInfo> getDelInfoByCaseId(DeliveryInfo deliveryInfo) {
        return deliveryInfoDao.getDelInfoByCaseId(deliveryInfo);
    }

    /**
     * 描述：新增送达信息
     *
     * @return
     * @author： news
     * @time: 2018/8/16 16:34
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public int insertDeliveryInfo(DeliveryInfo deliveryInfo) {
        return deliveryInfoDao.insertDeliveryInfo(deliveryInfo);
    }

    /**
     * 描述：修改送达信息
     *
     * @return
     * @author： news
     * @time: 2018/8/16 18:19
     * @param: * @param null
     */
    @Transactional(readOnly = false)
    public int updateDeliveryInfo(DeliveryInfo deliveryInfo) {
        return deliveryInfoDao.updateDeliveryInfo(deliveryInfo);
    }

    /**
     * 描述：获取最新的一条记录
     * @author： news
     * @time: 2018/10/12 16:01
     * @param:  * @param null
     * @return
     */
    public DeliveryInfo getTheLastDev(DeliveryInfo deliveryInfo){
        return deliveryInfoDao.getTheLastDev(deliveryInfo);
    }

    /**
     * 描述：修改送达信息,避免重复提交
     * @author： news
     * @time: 2018/11/7 10:02
     * @param:  * @param null
     * @return
     */
    @Transactional(readOnly = false)
    public int updateDevInfo(DeliveryInfo deliveryInfo){
        return deliveryInfoDao.updateDevInfo(deliveryInfo);
    }

    /**
     * 描述：获取deliveryInfo
     * @author： news
     * @time: 2018/11/7 10:10
     * @param:  * @param null
     * @return
     */
    public List<DeliveryInfo> getDevInfo(DeliveryInfo deliveryInfo){
        return deliveryInfoDao.getDevInfo(deliveryInfo);
    }

}