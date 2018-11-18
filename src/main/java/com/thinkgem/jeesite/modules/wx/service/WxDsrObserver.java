package com.thinkgem.jeesite.modules.wx.service;

import com.hc77.utils.INewObjectObserver;
import com.thinkgem.jeesite.modules.wx.entity.WxDsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxDsrObserver implements INewObjectObserver<WxDsr> {

    @Autowired
    private WxDsrService wxDsrService;

    @Override
    public boolean onNewObjectArrived(WxDsr wxDsr) {
        // 如果根据id能够找到当前当事人，则为更新（认证后更新身份信息）；否则为新增
        if (wxDsrService.get(wxDsr.getId()) == null)
            wxDsr.setIsNewRecord(true);

        wxDsrService.save(wxDsr);
        return true;
    }
}
