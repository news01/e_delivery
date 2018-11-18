package com.thinkgem.jeesite.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.thinkgem.jeesite.modules.userwechat.web.SysUserController;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class QCloudSMSUtil {
    Logger logger = Logger.getLogger(QCloudSMSUtil.class);

    ConfigurationService configurationService;

    public QCloudSMSUtil(){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        if(wac!=null) {
            configurationService =wac.getBean(ConfigurationService.class);
        }
    }
    public static void main(String[] args) {
    }
    public String sendWithParam(String phoneNumber,int templateId,String ... params){
        try {
            SmsSingleSender ssender = new SmsSingleSender(configurationService.getQcloudsmsAppid(), configurationService.getQcloudsmsAppkey());
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, "E网送达", "", "");
            System.out.print(result);
            logger.info(result);
        } catch (Exception e) {
            // HTTP响应码错误
            e.printStackTrace();
        }
        return "";
    }
}
