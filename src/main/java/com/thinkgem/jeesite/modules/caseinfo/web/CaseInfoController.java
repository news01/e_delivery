/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseinfo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hc77.utils.DataExporter;
import com.thinkgem.jeesite.common.utils.e.delivery.ControlHandler;
import com.thinkgem.jeesite.common.utils.e.delivery.HandlerProxy;
import com.thinkgem.jeesite.common.utils.e.delivery.ResultVo;
import com.thinkgem.jeesite.common.utils.e.delivery.WebParamUtils;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogInfo;
import com.thinkgem.jeesite.modules.caseloginfo.service.CaseLogInfoService;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import com.thinkgem.jeesite.modules.deliveryInfo.service.DeliveryInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import com.thinkgem.jeesite.modules.wx.entity.EdeliveryExport;
import com.thinkgem.jeesite.util.ConfigurationService;
import com.thinkgem.jeesite.util.SMSMessageUtil;
import com.thinkgem.jeesite.util.StringUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.groovy.tools.shell.commands.SetCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 案件信息表Controller
 *
 * @author niushi
 * @version 2018-08-10
 */
@Controller
public class CaseInfoController extends BaseController {

    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    @Autowired
    private CaseLogInfoService caseLogInfoService;


//    @Value("${prefixWithdraw}")
//    private String strPrefixWithdraw;
//
//    @Value("${outServerKdxxRoot}")
//    private String outServerTaskRoot;

    @Autowired
    private ConfigurationService configurationService;


    private DataExporter outDataExporter = null;


    /**
     * 描述：社区工作人员扫码获取案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/10 15:40
     * @param: * @param null
     */
    @RequestMapping(value = "caseinfo/CommunityWorkersScan")
    public void getCommunityWorkersScan(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {
                String communityId = WebParamUtils.getString("communityId", request);
                String userId = WebParamUtils.getString("userId", request);
                String code = WebParamUtils.getString("code", request);
                System.out.println(sessionId);


                if (StringUtils.isNotBlank(code)) {
                    //TODO


                }


                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：根据网格中心的首程邮号查询案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/18 17:38
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getCaseInfoByFirstmail")
    public void getCaseInfoByFirstmail(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String firstmail = WebParamUtils.getString("firstmail", request);


                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setFirstMail(firstmail);
                    }
                });

                if (caseInfoList == null || caseInfoList.isEmpty()) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("没有此案件");
                    return null;
                } else if (caseInfoList.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("大于1");
                    return null;
                }

                CaseInfo caseInfo = caseInfoList.get(0);
                List<Map> caseInfoList1 = new ArrayList<Map>();
                Date date = caseInfo.getCommuntitySignTime();//社区签收时间
                if (date == null) {
                    //未签收
                    resultVo.setErrCode(-1);
                    caseInfoList1.add(new HashMap<String, Object>() {
                        {
                            String c_id = caseInfo.getId();
                            put("c_id", c_id);
                            String caseNum = caseInfo.getCaseNum();
                            put("caseNum", caseNum);
                        }
                    });

                    resultVo.setResult(caseInfoList1);

                } else {
                    //已签收
                    resultVo.setErrCode(1);
                    resultVo.setErrMsg("已签收");
                }

                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心确定签收案件
     *
     * @return
     * @author： news
     * @time: 2018/8/18 18:24
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/girdWorkersComfirmSign")
    public void girdWorkersComfirmSign(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String com_id = WebParamUtils.getString("com_id", request);
                String userId = WebParamUtils.getString("userId", request);
                String c_id = WebParamUtils.getString("c_id", request);

                if (StringUtils.isBlank(com_id) || StringUtils.isBlank(userId) || StringUtils.isBlank(c_id)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("参数不能为空");
                    return null;
                }
                Date date = new Date();
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                CaseInfo caseInfo = new CaseInfo() {
                    {
                        setCommuntityId(com_id);
                        setCommuntitySignId(userId);
                        setId(c_id);
                        setCommuntitySignTime(date);
                        setCaseStatus("1");//网格中心签收（未分派）
                        setAttribute2("1");//未开始送达

                    }
                };
                int result = caseInfoService.updateCaseInfoById(caseInfo);

                if (result != 1) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("结果不正确");
                    return null;
                }


                if (outDataExporter == null) {
                    outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                }

                CaseInfo caseInfo2 = caseInfoService.get(c_id);
                caseInfo2.setCommuntityId(com_id);
                caseInfo2.setCommuntitySignId(userId);
//                date.setHours(date.getHours() + 8);
                caseInfo2.setCommuntitySignTime(date);
                caseInfo2.setCaseStatus("1");//网格中心签收（未分派）


                DeliveryInfo deliveryInfo = new DeliveryInfo();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(date);
                deliveryInfo.setDevTime(date);
                deliveryInfo.setDevTime2(time);
                deliveryInfo.setDevGird(userId);
                deliveryInfo.setComSign(com_id);
                deliveryInfo.setDevStatus("1");
                deliveryInfo.setCaseId(caseInfo2.getFirstMail());

                //在网deliveryInfo里新插入记录的时候，先判断此情况的记录是否已经存在
                int resp = 0;
                List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDevInfo(deliveryInfo);
                if (deliveryInfos.size() >= 1) {
                    resp = deliveryInfoService.updateDevInfo(deliveryInfo);
                } else {
                    resp = deliveryInfoService.insertDeliveryInfo(deliveryInfo);
                }

                DeliveryInfo deliveryInfo2 = new DeliveryInfo();
                deliveryInfo2.setCaseId(caseInfo2.getFirstMail());
                DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo2);


                EdeliveryExport edeliveryExport = new EdeliveryExport();
                edeliveryExport.setCaseInfo(caseInfo2);
                edeliveryExport.setDeliveryInfo(deliveryInfo3);
                outDataExporter.export(edeliveryExport);

                resultVo.setErrCode(1);
                resultVo.setErrMsg("签收完成");

                return null;
            }
        }, request, response, sesionId);

    }

    /**
     * 描述：网格中心扫描返程条码
     *
     * @return
     * @author： news
     * @time: 2018/8/18 19:28
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getCaseInfoByReturnmail")
    public void getCaseInfoByReturnmail(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                String returnmail = WebParamUtils.getString("returnmail", request);
                String com_id = WebParamUtils.getString("com_id", request);
//                System.out.println(user.getCompanyId());


                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setReturnMail(returnmail);
                        setCommuntityId(com_id);
                    }
                });

                if (caseInfoList == null || caseInfoList.isEmpty()) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("本社区未找到此返程码对应案件");
                    return null;
                } else if (caseInfoList.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("此返程码对应案件数量大于1");
                    return null;
                }

                CaseInfo caseInfo = caseInfoList.get(0);

                if (caseInfo.getEmsSignTime() != null) {
                    resultVo.setErrCode(-4);
                    resultVo.setErrMsg("返程条码已被签收");
                    return null;
                }

                String case_id = caseInfo.getCaId();
                String returnMail = caseInfo.getReturnMail();
                String firstMail = caseInfo.getFirstMail();

                List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {
                    {
                        setCaseId(firstMail);
                    }
                });

                if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                    //送达信息为空，说明还未送达
                    resultVo.setErrCode(-3);
                    resultVo.setErrMsg("送达未完成");
                    return null;
                }
                DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                int times = deliveryInfo.getDevNum();//送达次数
//                String delStatus = deliveryInfo.getAttribute1();//是否送达成功;1:成功;2:未成功；3：强制退出；4：不强制送三次
//
//                if (delStatus.equals("2") && times < 3) {
//                    //送达未完成
//                    resultVo.setErrCode(-1);
//                    resultVo.setErrMsg("送达未完成");
//                    return null;
//                }
                String status = deliveryInfo.getDevStatus();
                int sta = Integer.parseInt(status);
                if (sta < 4 || (sta >= 7 && sta < 9)) {
                    //送达未完成
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("送达未完成");
                    return null;
                }


                Date date = caseInfo.getCommuntitySignTime();//社区签收时间
                String comm_id = caseInfo.getCommuntityId();//签收社区id
                if (date == null || !comm_id.equals(com_id)) {
                    //社区未签收
                    resultVo.setErrCode(-2);
                    resultVo.setErrMsg("社区未签收");
                    return null;

                }

                List<Map> caseInfoList1 = new ArrayList<Map>();
                //未签收
                caseInfoList1.add(new HashMap<String, Object>() {
                    {
                        put("c_id", caseInfo.getId());
                        put("caseNum", caseInfo.getCaseNum());
                    }
                });

                resultVo.setErrCode(1);
                resultVo.setResult(caseInfoList1);

                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心确定签收返程邮码
     *
     * @return
     * @author： news
     * @time: 2018/8/20 10:34
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/girdWorkersSignReturnMail")
    public void girdWorkersSignReturnMail(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

//                String com_id = WebParamUtils.getString("com_id", request);
                String userId = WebParamUtils.getString("userId", request);
                String c_id = WebParamUtils.getString("c_id", request);

                Date date = new Date();
                CaseInfo caseInfo = new CaseInfo() {{
                    setId(c_id);
                    setEmsSignTime(date);
                    setCaseStatus("6");//网格中心确定签收返程（快递中）
                    setEmsId(userId);//ems存储签收网格中心工作人员id
                }};
                int resp = caseInfoService.updateCaseInfoById(caseInfo);


                if (resp != 1) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("返程邮码签收失败");
                } else {

                    if (outDataExporter == null) {
                        outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                    }

//                    CaseInfo caseInfo1 = new CaseInfo();
//                    caseInfo1.setEmsSignTime(date);
//                    caseInfo1.setCaseStatus("6");//网格中心确定签收返程（快递中）
//                    caseInfo1.setEmsId(userId);//ems存储签收网格中心工作人员id


                    CaseInfo caseInfo1 = caseInfoService.get(c_id);
                    caseInfo1.setEmsSignTime(date);
                    caseInfo1.setCaseStatus("6");//网格中心确定签收返程（快递中）
                    caseInfo1.setEmsId(userId);//ems存储签收网格中心工作人员id


                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                    deliveryInfo.setCaseId(caseInfo1.getFirstMail());
                    deliveryInfo.setDevStatus("12");
                    deliveryInfo.setDevGird(userId);//签收返程码的人员id

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    deliveryInfo.setDevTime2(time);
                    deliveryInfo.setDevTime(date);

                    DeliveryInfo deliveryInfo1 = new DeliveryInfo();
                    deliveryInfo1.setCaseId(caseInfo1.getFirstMail());
                    List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(deliveryInfo1);
                    if (!deliveryInfos.isEmpty()) {
                        DeliveryInfo deliveryInfo2 = deliveryInfos.get(0);
                        String resp2 = deliveryInfo2.getAttribute1();
                        deliveryInfo.setAttribute1(resp2);
                    }

                    deliveryInfoService.insertDeliveryInfo(deliveryInfo);

//                    DeliveryInfo deliveryInfo2 = new DeliveryInfo();
//                    deliveryInfo2.setCaseId(caseInfo1.getFirstMail());
                    DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo1);

                    EdeliveryExport edeliveryExport = new EdeliveryExport();
                    edeliveryExport.setCaseInfo(caseInfo1);
                    edeliveryExport.setDeliveryInfo(deliveryInfo3);
                    outDataExporter.export(edeliveryExport);

                    resultVo.setErrCode(1);
                }

                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心获取案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/13 9:48
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getDistributedCaseList")
    public void getDistributedCaseList(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String communityId = WebParamUtils.getString("communityId", request);//社区编号
                //1:管理员;2:社区专职人员;3:网格员;4:邮递员;5;法院工作人员
                String userType = WebParamUtils.getString("userType", request);//用户类型
                String judge = WebParamUtils.getString("judge", request);//1:已分配；0：未分配

                List<CaseInfo> caseInfoList = null;
                if (StringUtils.isNotBlank(userType) && userType.equals("2")) {

                    if (StringUtils.isNotBlank(judge) && judge.equals("1")) {
                        caseInfoList = caseInfoService.findList(new CaseInfo() {
                            {
                                setCommuntityId(communityId);
                                setGridId("1");//当网格员id不等于空时
                            }
                        });
                    } else if (StringUtils.isNotBlank(judge) && judge.equals("0")) {
                        caseInfoList = caseInfoService.findList(new CaseInfo() {
                            {
                                setCommuntityId(communityId);
                                setGridId("0");
                            }
                        });
                    }

                } else if (StringUtils.isNotBlank(userType) && userType.equals("3")) {

                }

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        String caseNum = caseInfo.getCaseNum();
                        String id = caseInfo.getId();
                        caseInfoList1.add(new HashMap<String, Object>() {
                            {
                                put("caseId", id);
                                put("caseNum", caseNum);
                                put("girdSignTime", caseInfo.getGridSignTime());
                            }
                        });
                    }
                }

                resultVo.setErrCode(1);
                resultVo.setResult(caseInfoList1);

                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心向网格员派件
     *
     * @return
     * @author： news
     * @time: 2018/8/13 17:25
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getGridWorkersDelivery")
    public void getGridWorkersDelivery(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String paramters = WebParamUtils.getString("paramters", request);
                JSONObject jsonObject = JSONObject.fromObject(paramters);
                System.out.println(paramters);
                System.out.println(jsonObject.toString());
                Date date = new Date();

                String userId = null;
                if (jsonObject.has("obj")) {
                    String obj = (String) jsonObject.get("obj");
                    JSONObject objResult = JSONObject.fromObject(obj);
                    String name = (String) objResult.get("name");
                    userId = (String) objResult.get("id");
                }
                if (jsonObject.has("sendIds")) {
                    String userId2 = userId;
                    List<SysUser> sysUserList = sysUserService.getSysUserbyId(userId);
                    SysUser sysUser = sysUserList.get(0);

                    String sendIds2 = (String) jsonObject.get("sendIds");
                    if (sendIds2.equals("[]")) {
                        resultVo.setErrCode(0);
                        resultVo.setErrMsg("未选择案件");
                        return null;
                    }

                    sendIds2 = sendIds2.substring(1, sendIds2.length() - 1);
                    String[] tests = sendIds2.split(",");
                    Integer resp = 0;
                    String caseNames = "";
                    for (int i = 0; i < tests.length; i++) {
                        String nnn = tests[i];
                        JSONObject jsonObjec2t = JSONObject.fromObject(nnn);
                        String caseId2 = (String) jsonObjec2t.get("caseId");
                        List<CaseInfo> list = caseInfoService.getCaseInfo(new CaseInfo() {{
                            setId(caseId2);
                        }});
                        if (list != null && !list.isEmpty()) {
                            CaseInfo caseInfo = list.get(0);
//                            if(caseNames!=null){
//                                caseNames+=",";
//                            }
                            caseNames += caseInfo.getCaseNum() + ",";
                        }

                        System.out.println(caseId2);

                        CaseInfo caseInfo = new CaseInfo() {
                            {
                                setGridId(userId2);
                                setId(caseId2);
                                setCaseStatus("2");//网格中心分派案件给网格员（网格员未接受前）
                                setUpdateDate(date);
                            }
                        };

                        resp = caseInfoService.updateCaseInfoById(caseInfo);
                        resp += resp;

                        if (outDataExporter == null) {
                            outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                        }

                        CaseInfo caseInfo1 = caseInfoService.get(caseId2);
                        caseInfo1.setGridId(userId2);
                        caseInfo1.setId(caseId2);
                        caseInfo1.setCaseStatus("2");//网格中心分派案件给网格员（网格员未接受前）
                        caseInfo1.setUpdateDate(date);


                        DeliveryInfo deliveryInfo = new DeliveryInfo();
                        deliveryInfo.setCaseId(caseInfo1.getFirstMail());
                        deliveryInfo.setDevTime(date);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = simpleDateFormat.format(date);
                        deliveryInfo.setDevTime2(time);
                        deliveryInfo.setDevGird(userId2);//派送的网格员
                        deliveryInfo.setDevStatus("2");

                        int res = deliveryInfoService.insertDeliveryInfo(deliveryInfo);


                        DeliveryInfo deliveryInfo2 = new DeliveryInfo();
                        deliveryInfo2.setCaseId(caseInfo1.getFirstMail());
                        DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo2);


                        EdeliveryExport edeliveryExport = new EdeliveryExport();
                        edeliveryExport.setCaseInfo(caseInfo1);
                        if (res >= 1) {
                            edeliveryExport.setDeliveryInfo(deliveryInfo3);
                        }
                        outDataExporter.export(edeliveryExport);
                    }
                    if (resp > 0) {
                        String phone = sysUser.getMobile();
//                        String str = "";
//                        str = SMSMessageUtil.Vcode();
                        SMSMessageUtil.sendDeliveryRemind(phone, sysUser.getName(), caseNames);


                        resultVo.setErrCode(1);
                    } else {
                        resultVo.setErrCode(0);
                    }

                }


                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：根据案件id获取案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/21 10:15
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getCaseInfoByCId")
    public void getCaseInfoByCId(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String c_id = WebParamUtils.getString("c_id", request);
                if (StringUtils.isBlank(c_id)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setId(c_id);
                }});
                if (caseInfoList == null) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("未找到案件信息");
                    return null;
                }

                CaseInfo caseInfo = caseInfoList.get(0);

                String g_id = caseInfo.getGridId();
                if (StringUtils.isNotBlank(g_id)) {
                    SysUser sysUser = sysUserService.getSysUserByIdOrMobile(new SysUser() {{
                        setId(g_id);
                    }});
                    String g_name = sysUser.getName();
                    caseInfo.setAttribute2(g_name);
                }


                List<Map> mapList = new ArrayList<>();
                if (caseInfoList != null) {

                    String sta = caseInfo.getCaseStatus();
                    int status = Integer.parseInt(sta);

                    mapList.add(new HashMap() {{
                        List<CaseLogInfo> caseLogInfos = new ArrayList<>();
                        List<Map> caseLogInfos2 = new ArrayList<>();
                        if (status >= 6) {
                            caseLogInfos = caseLogInfoService.getCaseLogInfo(new CaseLogInfo() {{
                                setEmailId(caseInfo.getReturnMail());
                            }});

                            for (CaseLogInfo caseLogInfo : caseLogInfos) {
                                caseLogInfos2.add(new HashMap() {{
                                    put("emailId", caseLogInfo.getEmailId());
                                    put("caseId", caseLogInfo.getCaseId());
                                    put("logTime", caseLogInfo.getLogTime());
                                    put("logInfo", caseLogInfo.getLogInfo());
                                    put("logStatus", caseLogInfo.getLogStatus());
                                    put("attribute2", caseLogInfo.getAttribute2());
                                    put("deliver", caseLogInfo.getDeliver());
                                    put("signMen", caseLogInfo.getSignMen());
                                }});
                            }
                        }

                        put("c_id", caseInfo.getId());
                        put("caseNum", caseInfo.getCaseNum());
                        put("deliver", caseInfo.getDeliver());
                        put("deliverSignTime", caseInfo.getDeliverSignTime());
                        put("deliverAddress", caseInfo.getDeliverAddress());
                        put("judgeAssistant", caseInfo.getJudgeAssistant());
                        put("judgeAssistantPhone", caseInfo.getJudgeAssistantPhone());
                        put("gird_name", caseInfo.getAttribute2());
                        put("girdSignTime", caseInfo.getGridSignTime());
                        put("caseStatus", caseInfo.getCaseStatus());
                        put("emsSignTime", caseInfo.getEmsSignTime());
                        put("deliveryTime", caseInfo.getUpdateDate());
                        put("deliveryFailTime", caseInfo.getAttribute3());
                        put("communtitySignTime", caseInfo.getCommuntitySignTime());
                        put("caseLogInfosList", caseLogInfos2);
                        put("courtSignTime", caseInfo.getCourtSignTime());
                    }});

                }
                resultVo.setErrCode(1);
                resultVo.setResult(mapList);

                return null;
            }
        }, request, response, sessionId);
    }

}