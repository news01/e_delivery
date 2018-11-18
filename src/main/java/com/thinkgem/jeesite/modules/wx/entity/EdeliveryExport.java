package com.thinkgem.jeesite.modules.wx.entity;

import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogInfo;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.modules.wx.entity
 * @Desc: ${end}
 * @date 2018/9/26  9:48
 */
public class EdeliveryExport {

    private CaseInfo caseInfo;

    private DeliveryInfo deliveryInfo;

    private CaseLogInfo caseLogInfo;

    private SysUser user;

    private Office office;


    public CaseInfo getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(CaseInfo caseInfo) {
        this.caseInfo = caseInfo;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public CaseLogInfo getCaseLogInfo() {
        return caseLogInfo;
    }

    public void setCaseLogInfo(CaseLogInfo caseLogInfo) {
        this.caseLogInfo = caseLogInfo;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
}
