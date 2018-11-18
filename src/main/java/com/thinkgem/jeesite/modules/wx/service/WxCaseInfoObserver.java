package com.thinkgem.jeesite.modules.wx.service;

import com.hc77.utils.INewObjectObserver;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfoDTO;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.modules.wx.service
 * @Desc: ${end}
 * @date 2018/8/10  10:59
 */
@Service
public class WxCaseInfoObserver implements INewObjectObserver<CaseInfoDTO> {

    @Autowired
    private CaseInfoService caseInfoService;

    @Override
    public boolean onNewObjectArrived(CaseInfoDTO caseInfoDTO) {
        // 如果根据id能够找到当前当事人，则为更新（认证后更新身份信息）；否则为新增
//        if (caseInfoService.get(caseInfo.getId()) == null)
//            caseInfo.setIsNewRecord(true);
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCaId(caseInfoDTO.getAjid());
        caseInfo.setCaseNum(caseInfoDTO.getAh());
        caseInfo.setFirstMail(caseInfoDTO.getJjdh());
        caseInfo.setReturnMail(caseInfoDTO.getFcyjdh());
        caseInfo.setDeliver(caseInfoDTO.getSjrmc());
        caseInfo.setDeliverAddress(caseInfoDTO.getSjrdz());
        caseInfo.setJudgeAssistant(caseInfoDTO.getSjymc());
        caseInfo.setJudgeAssistantPhone(caseInfoDTO.getSjylxdh());

        caseInfoService.insertOrUpdate(caseInfo);
        return true;

    }
}
