package com.thinkgem.jeesite.common.utils.e.delivery.SmsMessage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述：定时推送
 *
 * @author： news
 * @time: 2018/9/11 17:07
 * @param: * @param null
 * @return
 */
@Component
public class TimedPush {

    private static Logger logger = Logger.getLogger(TimedPush.class);

    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 描述：每一个小时执行一次
     *
     * @return
     * @author： news
     * @time: 2018/9/11 16:01
     * @param: * @param null
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
    @Scheduled(cron = "0 0 9 1/1 * ? *")//每天早上九点
    public void doPush() throws ClientProtocolException, IOException {


        /**
         * 网格员签收案件材料后，如果3天（速裁案件）、5天（其余案件）未完成送达任务，送达平台会接收到网格员超期未完成送达任务的提示，
         * 	该提示同时以短信的方式推送至社区专职人员的手机，提示内容为（以紫微社区网格员张三为例）“请注意，
         * 	紫薇社区网格员张三签收案号为…号案件材料未及时完成送达任务，已超期，请及时联系该名网格员”。
         */


        List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
            setRemarks("3");
        }});

        for (CaseInfo caseInfo : caseInfoList) {
            /**
             * 速裁案件和其余案件怎么区分
             */

        }


    }

}