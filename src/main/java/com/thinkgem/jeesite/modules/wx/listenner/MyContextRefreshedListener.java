package com.thinkgem.jeesite.modules.wx.listenner;

import com.hc77.utils.DataImporter;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfoDTO;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
//import com.thinkgem.jeesite.modules.wx.service.WxCaseInfoObserver;
import com.thinkgem.jeesite.modules.wx.service.WxCaseInfoObserver;
import com.thinkgem.jeesite.modules.wx.service.WxDsrObserver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class MyContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value(value = "${inServerKdxxRoot}")
    private String strImportPath;

    @Value(value = "${outServerTaskRoot}")
    private String outStrImportPath;

    @Value(value = "${prefixWxDsr}")
    private String strPrefixWxDsr;

    @Value(value = "${prefixWxJdxx}")
    private String strPrefixWxJdxx;

    @Value(value = "${prefixOutServerSendRequest}")
    private String strPrefixOutServerSendRequest;

    @Value("${prefixWithdraw}")
    private String strPrefixWithdraw;

    /**
     * 描述：案件信息
     * @author： news
     * @time: 2018/8/10 11:38
     * @param:  * @param null
     * @return
     */
    private DataImporter<CaseInfo> wxCaseinfoImporter = null;
//    private DataImporter<WxJdxx> wxJdxxImporter = null;
//    private DataImporter<OutServerSendRequest> outServerSendRequestDataImporter = null;
//
//
//    private DataImporter<Archives> archivesDataImporter = null;
//    private DataImporter<ArchivesSend> archivesSendDataImporter = null;
//    private DataImporter<ArchivesTrank> archivesTrankDataImporter = null;


    @Autowired
    private WxCaseInfoObserver wxCaseInfoObserver;
//    @Autowired
//    private WxJdxxObserver wxJdxxObserver;
//    @Autowired
//    private OutServerSendRequestObserver outServerSendRequestObserver;

    private Logger log = Logger.getLogger(MyContextRefreshedListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {    // root WebApp，这样下面的逻辑才不会多次调用
            // 创建需要监控数据同步的类对应的DataImporter
            log.info("================创建数据导入类=======================================================");
            wxCaseinfoImporter = new DataImporter(strImportPath, strPrefixWxDsr, wxCaseInfoObserver, CaseInfoDTO.class);
            wxCaseinfoImporter.init();


//            wxJdxxImporter = new DataImporter<>(strImportPath, strPrefixWxJdxx, wxJdxxObserver, WxJdxx.class);
//            wxJdxxImporter.init();
//

        }
    }
}
