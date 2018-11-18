package com.thinkgem.jeesite.modules.caseinfo.web;

import com.hc77.utils.DataExporter;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.e.delivery.ControlHandler;
import com.thinkgem.jeesite.common.utils.e.delivery.HandlerProxy;
import com.thinkgem.jeesite.common.utils.e.delivery.ResultVo;
import com.thinkgem.jeesite.common.utils.e.delivery.WebParamUtils;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import com.thinkgem.jeesite.modules.deliveryInfo.service.DeliveryInfoService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import com.thinkgem.jeesite.modules.wx.entity.EdeliveryExport;
import com.thinkgem.jeesite.util.ConfigurationService;
import com.thinkgem.jeesite.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Case;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.modules.caseinfo.web
 * @Desc: ${end}
 * @date 2018/8/20  15:09
 */
@Controller
public class GirdController {
    Logger logger = Logger.getLogger(GirdController.class);
    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    @Autowired
    private ConfigurationService configurationService;


    private DataExporter outDataExporter = null;


    /**
     * 描述：网格员案件接收
     *
     * @return
     * @author： news
     * @time: 2018/8/18 20:53
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/girdAcceptCase")
    public void girdAcceptCase(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String dev_code = WebParamUtils.getString("dev_code", request);
                String com_id = WebParamUtils.getString("com_id", request);


                if (StringUtils.isBlank(dev_code)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setFirstMail(dev_code);
                    setCommuntityId(com_id);
                }});
                if (caseInfoList == null || caseInfoList.isEmpty()) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("没有此案件");
                    return null;
                }
                if (caseInfoList.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("对应案件不止一条，请联系管理员！");
                    return null;
                }

                CaseInfo caseInfo = caseInfoList.get(0);
                String uid = caseInfo.getGridId();
                if (StringUtils.isBlank(uid)) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("该案件未指派给您！");
                    return null;
                }
//                SysUser sysUser = sysUserService.get(uid);
//                String turnsOutMen = sysUser.getName();

                List<Map> maps = new ArrayList<Map>();

                maps.add(new HashMap() {
                    {
                        put("c_id", caseInfo.getId());
                        put("caseNum", caseInfo.getCaseNum());
//                        put("turnsOut", caseInfo.getTurnsOut());
//                        put("turnsOutMen", turnsOutMen);
//                        put("judge", caseInfo.getAttribute1());
                    }
                });

                resultVo.setErrCode(1);
                resultVo.setResult(maps);

                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格员确定签收案件
     *
     * @return
     * @author： news
     * @time: 2018/8/20 10:06
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/girdComfirmSign")
    public void girdComfirmSign(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                String com_id = WebParamUtils.getString("com_id", request);
                String c_id = WebParamUtils.getString("c_id", request);

                int resp = 0;
                List<CaseInfo> caseInfos = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setId(c_id);
                        setCommuntityId(com_id);
//                        setGridId(userId);
                    }
                });
                if (StringUtils.isBlank(c_id) || caseInfos == null) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("未找到签收案件");
                    return null;
                }
                if (caseInfos.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("结果大于1");
                    return null;
                }
                CaseInfo caseInfo = caseInfos.get(0);
                String turnOut = caseInfo.getTurnsOut();
                Date date = new Date();

                if (StringUtils.isNotBlank(caseInfo.getAttribute1()) && "1".equals(turnOut)) {
                    String to_people = caseInfo.getAttribute1();//被转入人

                    String outMenID = caseInfo.getGridId();//转出人id

                    if (!to_people.equals(userId)) {
                        resultVo.setErrCode(-3);
                        resultVo.setErrMsg("该案件未转派给您");
                        return null;
                    }
                    if (StringUtils.isBlank(to_people)) {
                        resultVo.setErrCode(-1);
                        resultVo.setErrMsg("该案件未被转派");
                        return null;
                    }
                    SysUser sysUser = sysUserService.get(outMenID);
                    String outMenName = sysUser.getLoginName();//转出人姓名

                    if (StringUtils.isNotBlank(turnOut) && "1".equals(turnOut)) {
                        CaseInfo caseInfo1 = new CaseInfo() {
                            {
                                setId(c_id);
                                setGridSignTime(date);
                                setCaseStatus("3");//网格员签收
                                setGridId(userId);
                                setTurnsOut("1");
                                setTurnsOutPeople(outMenName);
                                setAttribute4("5");
                                setAttribute1(null);
                                logger.info("_____1111________++++++++++++++++");
                                setAttribute2("2");//送达中
                            }
                        };

                        resp = caseInfoService.updateCaseInfoById(caseInfo1);

                        if (outDataExporter == null) {
                            outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                        }

                        CaseInfo caseInfo2 = caseInfoService.get(c_id);
                        caseInfo2.setCaseStatus("3");
                        caseInfo2.setGridSignTime(date);
                        caseInfo2.setGridId(userId);

                        DeliveryInfo deliveryInfo = new DeliveryInfo();
                        deliveryInfo.setCaseId(caseInfo2.getFirstMail());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = simpleDateFormat.format(date);
                        deliveryInfo.setDevTime2(time);
                        deliveryInfo.setDevTime(date);

                        deliveryInfo.setDevGird(userId);//签收网格员id
                        deliveryInfo.setDevStatus("3");

                        int res = deliveryInfoService.insertDeliveryInfo(deliveryInfo);


                        DeliveryInfo deliveryInfo2 = new DeliveryInfo();
                        deliveryInfo2.setCaseId(caseInfo2.getFirstMail());
                        DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo2);

                        EdeliveryExport edeliveryExport = new EdeliveryExport();
                        edeliveryExport.setCaseInfo(caseInfo2);
                        edeliveryExport.setDeliveryInfo(deliveryInfo3);

                        outDataExporter.export(edeliveryExport);

                    }
                } else {
                    String g_id = caseInfo.getGridId();
                    if (!userId.equals(g_id)) {
                        resultVo.setErrCode(-4);
                        resultVo.setErrMsg("该案件未指定给您");
                        return null;
                    }
                    if (caseInfo.getGridSignTime() != null) {
                        resultVo.setErrCode(0);
                        resultVo.setErrMsg("案件已被签收");
                        return null;
                    }

                    CaseInfo caseInfo2 = new CaseInfo() {
                        {
                            setId(c_id);
                            setGridSignTime(date);
                            setCaseStatus("3");//网格员签收
                            setGridId(userId);
                            logger.info("_____2222________++++++++++++++++");
                            setAttribute2("2");//送达中
                        }
                    };

                    resp = caseInfoService.updateCaseInfoById(caseInfo2);

                    if (outDataExporter == null) {
                        outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                    }

                    CaseInfo caseInfo3 = caseInfoService.get(c_id);
                    caseInfo3.setCaseStatus("3");
                    caseInfo3.setGridSignTime(date);
                    caseInfo3.setGridId(userId);


                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                    String fm = caseInfo3.getFirstMail();
                    deliveryInfo.setCaseId(fm);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    deliveryInfo.setDevTime2(time);
                    deliveryInfo.setDevTime(date);

                    deliveryInfo.setDevGird(userId);//签收网格员id
                    deliveryInfo.setDevStatus("3");


                    int res = deliveryInfoService.insertDeliveryInfo(deliveryInfo);

                    DeliveryInfo deliveryInfo2 = new DeliveryInfo();
                    deliveryInfo2.setCaseId(caseInfo3.getFirstMail());
                    DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo2);

                    EdeliveryExport edeliveryExport = new EdeliveryExport();
                    edeliveryExport.setCaseInfo(caseInfo3);
                    edeliveryExport.setDeliveryInfo(deliveryInfo3);
                    outDataExporter.export(edeliveryExport);

                }

                if (resp != 1) {
                    resultVo.setErrCode(-2);
                    resultVo.setErrMsg("签收失败");
                } else {
                    resultVo.setErrCode(1);
                }


                return null;
            }
        }, request, response, sesionId);

    }


    /**
     * 描述：网格员下获取已接收案件
     *
     * @return
     * @author： news
     * @time: 2018/8/14 19:38
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getAllAcceptedCase")
    public void getAllAcceptedCase(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                String communityId = WebParamUtils.getString("communityId", request);
//                String id = user.getId();
//                String cid = user.getOffice().getId();

                System.out.println(userId);

                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setGridId(userId);
                        setCommuntityId(communityId);
                        setAttribute4("1");
//                        随便传入个时间
                        setGridSignTime(new Date());
                    }
                });

                List<Map> caseInfoList1 = new ArrayList<Map>();
                for (CaseInfo caseInfo : caseInfoList) {
                    caseInfoList1.add(new HashMap() {
                        {
                            String status = caseInfo.getCaseStatus();
                            if (StringUtils.isNotBlank(status)) {
                                int sta = Integer.parseInt(status);
                                if (sta > 3) {
                                    status = "-1";
                                }

                                put("c_id", caseInfo.getId());
                                put("caseId", caseInfo.getCaId());
                                put("deliver", caseInfo.getDeliver());//送送达人
                                put("deliver_address", caseInfo.getDeliverAddress());
                                put("case_num", caseInfo.getCaseNum());//案号
                                put("status", status);
                            }
                        }
                    });
                }

                if (caseInfoList1 != null && !caseInfoList.isEmpty()) {
                    resultVo.setErrCode(1);
                    resultVo.setResult(caseInfoList1);
                } else {
                    resultVo.setErrCode(0);
                }
                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：网格员获取已完成送达案件列表
     *
     * @return
     * @author： news
     * @time: 2018/8/24 10:12
     * @param: * @param null
     */
    @RequestMapping(value = "/gird/getComplateCaseList")
    public void getComplateCaseList(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                String communityId = WebParamUtils.getString("communityId", request);
                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setGridId(userId);
                        setCommuntityId(communityId);
                        setAttribute4("1");
//                        随便传入个时间
                        setGridSignTime(new Date());
                    }
                });

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        String caseStstus = caseInfo.getCaseStatus();
                        if ("4".equals(caseStstus) || "5".equals(caseStstus) || "6".equals(caseStstus) || "7".equals(caseStstus)) {
                            caseInfoList1.add(new HashMap() {{
                                put("c_id", caseInfo.getId());
                                put("caseId", caseInfo.getCaId());
                                put("deliver", caseInfo.getDeliver());//送送达人
                                put("deliver_address", caseInfo.getDeliverAddress());
                                put("case_num", caseInfo.getCaseNum());//案号
                                put("emsSignTime", caseInfo.getEmsSignTime());
                            }});
                            continue;
                        }

                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});
                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                        int times = deliveryInfo.getDevNum();
//                        String delStatus = deliveryInfo.getAttribute1();
//                        if (times >= 3 || "1".equals(delStatus) || "3".equals(delStatus) || "4".equals(delStatus)) {
//                            caseInfoList1.add(new HashMap() {{
//                                put("c_id", caseInfo.getId());
//                                put("caseId", caseInfo.getCaId());
//                                put("deliver", caseInfo.getDeliver());//送送达人
//                                put("deliver_address", caseInfo.getDeliverAddress());
//                                put("case_num", caseInfo.getCaseNum());//案号
//                                put("emsSignTime", caseInfo.getEmsSignTime());
//                            }});
//                            continue;
//                        }

                        String devStatus = deliveryInfo.getDevStatus();
                        if (devStatus.equals("4") || devStatus.equals("5") || devStatus.equals("6")) {
                            caseInfoList1.add(new HashMap() {{
                                put("c_id", caseInfo.getId());
                                put("caseId", caseInfo.getCaId());
                                put("deliver", caseInfo.getDeliver());//送送达人
                                put("deliver_address", caseInfo.getDeliverAddress());
                                put("case_num", caseInfo.getCaseNum());//案号
                                put("emsSignTime", caseInfo.getEmsSignTime());
                            }});
                            continue;
                        }
                        int status = Integer.parseInt(devStatus);
                        if (status >= 9) {
                            caseInfoList1.add(new HashMap() {{
                                put("c_id", caseInfo.getId());
                                put("caseId", caseInfo.getCaId());
                                put("deliver", caseInfo.getDeliver());//送送达人
                                put("deliver_address", caseInfo.getDeliverAddress());
                                put("case_num", caseInfo.getCaseNum());//案号
                                put("emsSignTime", caseInfo.getEmsSignTime());
                            }});
                            continue;
                        }

                    }
                }

                resultVo.setErrCode(1);
                resultVo.setResult(caseInfoList1);

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：获取案件详情信息
     *
     * @return
     * @author： news
     * @time: 2018/8/15 11:56
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getCaseDetailInfo")
    public void getCaseDetailInfo(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String c_id = WebParamUtils.getString("c_id", request);
                List<CaseInfo> caseInfos = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setId(c_id);
                    }
                });
                if (caseInfos.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("大于1");
                    return null;
                }

//                案件的基本信息
                CaseInfo caseInfo = caseInfos.get(0);
                JSONObject caseInfoJsonObject = new JSONObject();
                caseInfoJsonObject.put("deliver", caseInfo.getDeliver());
                caseInfoJsonObject.put("deliverAddress", caseInfo.getDeliverAddress());
                caseInfoJsonObject.put("judgeAssistant", caseInfo.getJudgeAssistant());
                caseInfoJsonObject.put("judgeAssistantPhone", caseInfo.getJudgeAssistantPhone());
                caseInfoJsonObject.put("caseNum", caseInfo.getCaseNum());
                caseInfoJsonObject.put("caId", caseInfo.getCaId());
                caseInfoJsonObject.put("returnMail", caseInfo.getReturnMail());
                caseInfoJsonObject.put("firstMail", caseInfo.getFirstMail());

//                  案件的送达信息
                String returnMail = caseInfo.getReturnMail();
                String firstMail = caseInfo.getFirstMail();
                List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {
                    {
                        setCaseId(firstMail);
                    }
                });

                JSONObject deliveryInfoJsonObject = new JSONObject();
                if (deliveryInfos != null && !deliveryInfos.isEmpty()) {
                    if(deliveryInfos.size()>3){
                        Iterator<DeliveryInfo> iterator = deliveryInfos.iterator();
                        while (iterator.hasNext()) {
                            DeliveryInfo deliveryInfo = iterator.next();
                            Integer num = deliveryInfo.getDevNum();
                            if (num == null) {
                                iterator.remove();
                            }

                        }
                    }

                    DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                    deliveryInfoJsonObject.put("devId", deliveryInfo.getDevId());
                    deliveryInfoJsonObject.put("caseId", deliveryInfo.getCaseId());
                    deliveryInfoJsonObject.put("devNum", deliveryInfo.getDevNum());
                    //是否被受送达人签收1:成功;2:未成功;3:强制退出;4:不强制送达三次
                    deliveryInfoJsonObject.put("devStatus", deliveryInfo.getAttribute1());
                }

                List<Map> caseInfoList1 = new ArrayList<Map>();
                caseInfoList1.add(new HashMap() {
                    {
                        put("caseInfoJsonObject", caseInfoJsonObject.toString());
                        put("deliveryInfoJsonObject", deliveryInfoJsonObject.toString());
                    }
                });

                resultVo.setErrCode(1);
                resultVo.setResult(caseInfoList1);

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：网格员待接收案件信息列表
     *
     * @return
     * @author： news
     * @time: 2018/8/20 15:11
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getToReceiveCaseList")
    public void getCommunityWorkersScan(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String com_id = WebParamUtils.getString("com_id", request);
                String userId = WebParamUtils.getString("userId", request);
                String judge = WebParamUtils.getString("judge", request);
                //1:待接收案件；2：已完成案件

                if (StringUtils.isBlank(com_id) || StringUtils.isBlank(userId)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<Map> mapList = new ArrayList<>();
                List<CaseInfo> caseInfoList = new ArrayList<>();
                if ("1".equals(judge)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(com_id);
                        setAttribute4("1");
                        setGridSignTime(null);
                    }});

                    List<CaseInfo> caseInfoList1 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setAttribute1(userId);
                    }});

                    if (caseInfoList1 != null && !caseInfoList1.isEmpty()) {
                        caseInfoList.addAll(caseInfoList1);
                    }


                } else if ("2".equals(judge)) {
                    List<CaseInfo> caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(com_id);
                    }});

//                    caseInfoList = new ArrayList<>();
                    for (CaseInfo caseInfo : caseInfoList2) {
                        Date deliverySignTime = caseInfo.getDeliverSignTime();
                        if (deliverySignTime == null) {
                            String caseId = caseInfo.getCaId();
                            String returnMail = caseInfo.getReturnMail();
                            String firstMail = caseInfo.getFirstMail();
                            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                                setCaseId(firstMail);
                            }});
                            if (deliveryInfos != null && !deliveryInfos.isEmpty()) {
                                DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                                int times = deliveryInfo.getDevNum();
                                String del_status = deliveryInfo.getAttribute1();
                                /*
                                 * 如果未查到当事人签收，则查送达信息记录是否已满3次
                                 */
                                if (times == 3 || "1".equals(del_status) || "3".equals(del_status) || "4".equals(del_status)) {
                                    caseInfoList.add(caseInfo);
                                }
                            }

                        } else {
                            caseInfoList.add(caseInfo);
                        }

                    }

                }

                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        mapList.add(new HashMap() {{
                            put("c_id", caseInfo.getId());
                            put("caseNum", caseInfo.getCaseNum());
                            put("caseId", caseInfo.getCaId());

                        }});
                    }

                }

                resultVo.setErrCode(1);
                resultVo.setResult(mapList);

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：网格员根据案件状态获取案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/20 18:44
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getDelInfoByStatus")
    public void getDelInfoByStatus(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                String communityId = WebParamUtils.getString("communityId", request);
                String status = WebParamUtils.getString("status", request);


                if (StringUtils.isBlank(userId) || StringUtils.isBlank(communityId)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();

                //0:送达中；1：送达成功；2：失败；
                /**
                 * -1:未被当事人成功签收
                 * 0：次数未满3次，送达状态未成功
                 * 1：成功被当事人接收
                 */
                if ("2".equals(status)) {
                    List<CaseInfo> caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(null);
                    }});

                    if (caseInfoList2 != null) {
                        for (CaseInfo caseInfo : caseInfoList2) {
                            String caseStatus = caseInfo.getCaseStatus();
                            if ("4".equals(caseStatus)) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }
                            String caseId = caseInfo.getCaId();
                            String returnMail = caseInfo.getReturnMail();
                            String firstMail = caseInfo.getFirstMail();
                            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                                setCaseId(firstMail);
                            }});
                            if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                                continue;
                            }

                            DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                            /**
                             * 需求修改前
                             */
//                            int times = deliveryInfo.getDevNum();
//                            String caseStatus2 = deliveryInfo.getAttribute1();
//
//                            if (times >= 3 && "2".equals(caseStatus2) || times <= 3 && "3".equals(caseStatus2) || times <= 3 && "4".equals(caseStatus2)) {
//                                caseInfoList.add(caseInfo);
//                            }
                            /**
                             * 需求修改后
                             */
                            String status2 = deliveryInfo.getDevStatus();
                            int sta = Integer.parseInt(status2);
                            if (sta >= 9 && sta < 12) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }
                            String caseStatus2 = deliveryInfo.getAttribute1();
                            if ("2".equals(caseStatus2) || "3".equals(caseStatus2) || "4".equals(caseStatus2)) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }


                        }
                    }

                } else if ("0".equals(status)) {
                    List<CaseInfo> caseInfoList2 = new ArrayList<>();
                    caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(communityId);
                    }});
                    for (CaseInfo caseInfo : caseInfoList2) {
                        Date girdSignTime = caseInfo.getGridSignTime();
                        if (girdSignTime == null) {
                            continue;
                        }
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});
                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                        /**
                         *需求修改前
                         */
//                        int times = deliveryInfo.getDevNum();
////                        1:成功;2:未成功;3:强制退出;4:不强制送达三次
//                        String devStatus = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 || "1".equals(devStatus) || "3".equals(devStatus) || "4".equals(devStatus)) {
//                            continue;
//                        }
                        /**
                         * 需求修改后
                         */
                        String status2 = deliveryInfo.getDevStatus();
                        if (status2.equals("3")) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        int sta = Integer.parseInt(status2);
                        if (sta >= 7 && sta < 9) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }

//                        caseInfoList.add(caseInfo);
                    }
                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(new Date());
                    }});
                }

                List<Map> mapList = new ArrayList<>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        mapList.add(new HashMap() {{
                            put("c_id", caseInfo.getId());
                            put("caseNum", caseInfo.getCaseNum());
                            put("caseId", caseInfo.getCaId());

                        }});
                    }

                }

                resultVo.setErrCode(1);
                resultVo.setResult(mapList);


                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：网格员个人中心送达情况模糊查询
     *
     * @return
     * @author： news
     * @time: 2018/8/21 10:56
     * @param: * @param null
     */
    @RequestMapping(value = "/caseInfo/getCaseInfoByLike")
    public void getCaseInfoByLike(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String like = WebParamUtils.getString("like", request);
                String status = WebParamUtils.getString("status", request);
                String userId = WebParamUtils.getString("userId", request);
                String com_id = WebParamUtils.getString("com_id", request);

                if (StringUtils.isBlank(userId) || StringUtils.isBlank(com_id) || StringUtils.isBlank(status)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = null;

                //0:送达中；1：送达成功；2：失败；
                /**
                 * -1:未被当事人成功签收
                 * 0：次数未满3次，送达状态未成功
                 * 1：成功被当事人接收
                 */
                if ("2".equals(status)) {
                    caseInfoList = new ArrayList<CaseInfo>();
                    List<CaseInfo> caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(com_id);
                        setDeliverSignTime(null);
                        setAttribute4("4");
                        setCaseNum(like);
                    }});
                    if (caseInfoList2 != null) {

                        for (CaseInfo caseInfo : caseInfoList2) {
                            String caseStatus = caseInfo.getCaseStatus();
                            if ("4".equals(caseStatus)) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }
                            String caseId = caseInfo.getCaId();
                            String returnMail = caseInfo.getReturnMail();
                            String firstMail = caseInfo.getFirstMail();
                            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                                setCaseId(firstMail);
                            }});
                            if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                                continue;
                            }

                            DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                            int times = deliveryInfo.getDevNum();
//                            String caseStatus2 = deliveryInfo.getAttribute1();
//
////                            if (times >= 3 && "2".equals(caseStatus2) || "3".equals(caseStatus2) || "4".equals(caseStatus2)) {
//                            if (times >= 3 && "2".equals(caseStatus2) || times <= 3 && "3".equals(caseStatus2) || times <= 3 && "4".equals(caseStatus2)) {
//                                caseInfoList.add(caseInfo);
//
//                            }

                            String status2 = deliveryInfo.getDevStatus();
                            int sta = Integer.parseInt(status2);
                            if (sta >= 9 && sta < 12) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }
                            String caseStatus2 = deliveryInfo.getAttribute1();
                            if ("2".equals(caseStatus2) || "3".equals(caseStatus2) || "4".equals(caseStatus2)) {
                                caseInfoList.add(caseInfo);
                                continue;
                            }

                        }
                    }


                } else if ("0".equals(status)) {
                    caseInfoList = new ArrayList<CaseInfo>();
                    List<CaseInfo> caseInfoList2 = new ArrayList<>();
                    caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(com_id);
                        setAttribute4("4");
                        setCaseNum(like);
                    }});
                    for (CaseInfo caseInfo : caseInfoList2) {
                        Date girdSignTime = caseInfo.getGridSignTime();
                        if (girdSignTime == null) {
                            continue;
                        }
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});
                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                        int times = deliveryInfo.getDevNum();
////                        1:成功;2:未成功;3:强制退出;4:不强制送达三次
//                        String devStatus = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 || "1".equals(devStatus) || "3".equals(devStatus) || "4".equals(devStatus)) {
//                            continue;
//                        }

                        String status2 = deliveryInfo.getDevStatus();
                        if (status2.equals("3")) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        int sta = Integer.parseInt(status2);
                        if (sta >= 7 && sta < 9) {
                            caseInfoList.add(caseInfo);
                            continue;
                        }
//                        caseInfoList.add(caseInfo);
                    }
                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setGridId(userId);
                        setCommuntityId(com_id);
                        setAttribute4("4");//sql判断
                        setCaseNum(like);
                        setDeliverSignTime(new Date());
                    }});
                }

                List<Map> mapList = new ArrayList<>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        mapList.add(new HashMap() {{
                            put("c_id", caseInfo.getId());
                            put("caseNum", caseInfo.getCaseNum());
                            put("caseId", caseInfo.getCaId());

                        }});
                    }

                }

                resultVo.setErrCode(1);
                resultVo.setResult(mapList);

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：网格员案件转派
     *
     * @return
     * @author： news
     * @time: 2018/8/25 17:22
     * @param: * @param null
     */
    @RequestMapping(value = "/gird/gridWorkersRedeployCase")
    public void gridWorkersredeployCase(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String paramters = WebParamUtils.getString("paramters", request);
                JSONObject jsonObject = JSONObject.fromObject(paramters);
                System.out.println(paramters);
                System.out.println(jsonObject.toString());

                String userId = null;
                Integer resp = 0;
                if (jsonObject.has("obj")) {
                    String obj = (String) jsonObject.get("obj");
                    JSONObject objResult = JSONObject.fromObject(obj);
                    String name = (String) objResult.get("name");
                    userId = (String) objResult.get("id");
                }
                if (jsonObject.has("c_id")) {
                    String userId2 = userId;

                    String c_id = (String) jsonObject.get("c_id");

                    List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setId(c_id);
                    }});
                    if (caseInfoList != null) {
                        CaseInfo caseInfo = caseInfoList.get(0);
                        String turnOut = caseInfo.getTurnsOut();
                        if (StringUtils.isNotBlank(turnOut) && "1".equals(turnOut)) {
                            resultVo.setErrCode(-1);
                            resultVo.setErrMsg("已被转派过，不能再次转派");
                            return null;
                        }

                        String status = caseInfo.getCaseStatus();
                        if (!"3".equals(status)) {
                            resultVo.setErrCode(-1);
                            resultVo.setErrMsg("该案件已被您持有，不能转派");
                            return null;
                        }

                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});
                        if (deliveryInfos != null && !deliveryInfos.isEmpty()) {
                            //如果已经存在送达信息
                            resultVo.setErrCode(2);
                            resultVo.setErrMsg("已经存在送达记录的案件不能转派");
                            return null;
                        }

                        CaseInfo caseInfo1 = new CaseInfo() {
                            {
                                setTurnsOut("1");
                                setAttribute1(userId2);//临时记录转派之被转派人之id
                                setId(c_id);
                            }
                        };
                        resp = caseInfoService.updateCaseInfoById(caseInfo1);


                        if (outDataExporter == null) {
                            outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                        }
                        CaseInfo caseInfo2 = new CaseInfo();
                        caseInfo2.setTurnsOut("1");
                        caseInfo2.setAttribute1(userId2);

                        EdeliveryExport edeliveryExport = new EdeliveryExport();
                        edeliveryExport.setCaseInfo(caseInfo2);
                        outDataExporter.export(edeliveryExport);

                    }

                    if (resp > 0) {
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
     * 描述：网格员报表统计
     *
     * @return
     * @author： news
     * @time: 2018/9/6 17:27
     * @param: * @param null
     */
    @RequestMapping(value = "/gird/getReportCount")
    public void getReportCount(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                String u_id = WebParamUtils.getString("u_id", request);
                String communityId = WebParamUtils.getString("communityId", request);
                String currentdata = WebParamUtils.getString("currentdata", request);


                //案件总量:分配给此网格员的
                Date date = new Date();
                List<CaseInfo> caseCounts = new ArrayList<>();
                List<CaseInfo> caseSuccessCounts = new ArrayList<>();
                List<CaseInfo> caseUnsuccessCounts = new ArrayList<>();
                List<CaseInfo> caseDelivingCounts = new ArrayList<>();
                List<CaseInfo> caseNotSign = new ArrayList<>();

                //送达总量
                caseCounts = caseInfoService.selectCaseInfo(new CaseInfo() {{
//                    setAttribute4("1");
//                    setGridSignTime(date);
                    setCommuntityId(communityId);
                    setGridId(u_id);
                    setRemarks(currentdata);
                }});
//              未签收
                caseNotSign = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setAttribute4("1");
                    setGridSignTime(null);
                    setCommuntityId(communityId);
                    setGridId(u_id);
                    setRemarks(currentdata);
                }});


                //送达成功的
                caseSuccessCounts = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setGridId(u_id);
                    setCommuntityId(communityId);
                    setAttribute4("3");
                    setDeliverSignTime(date);
                    setRemarks(currentdata);
                }});


//                caseUnsuccessCounts = caseInfoService.selectCaseInfo(new CaseInfo() {{
//                    setAttribute4("3");
//                    setDeliverSignTime(null);
//                    setGridId(u_id);
//                }});
                //送达失败的
                List<CaseInfo> caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setGridId(u_id);
                    setCommuntityId(communityId);
                    setAttribute4("3");
                    setDeliverSignTime(null);
                    setRemarks(currentdata);
                }});

                if (caseInfoList2 != null) {
                    for (CaseInfo caseInfo : caseInfoList2) {
                        String caseStatus = caseInfo.getCaseStatus();
                        if ("4".equals(caseStatus)) {
                            caseUnsuccessCounts.add(caseInfo);
                            continue;
                        }
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});
                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            continue;
                        }

                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                        /**
                         * 需求修改前
                         */
//                        int times = deliveryInfo.getDevNum();
//                        String caseStatus2 = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 && "2".equals(caseStatus2) || times <= 3 && "3".equals(caseStatus2) || times <= 3 && "4".equals(caseStatus2)) {
//                            caseUnsuccessCounts.add(caseInfo);
//                        }
                        /**
                         * 需求修改后
                         */
                        logger.info("需求修改后");
                        String devStatus = deliveryInfo.getDevStatus();
                        int status = Integer.parseInt(devStatus);
                        if (status >= 9 && status < 12) {
                            //送达失败
                            caseUnsuccessCounts.add(caseInfo);
                            continue;
                        }
                        String attri = deliveryInfo.getAttribute1();
                        if (StringUtils.isNotBlank(attri)) {
                            if (attri.equals("2") || attri.equals("3") || attri.equals("4")) {
                                //送达失败
                                caseUnsuccessCounts.add(caseInfo);
                                continue;
                            }
                        }

                    }
                }


//                caseDelivingCounts = caseInfoService.selectCaseInfo(new CaseInfo() {{
//                    setAttribute4("3");
//                    setDeliverSignTime(null);
//                    setGridId(u_id);
//                }});
                //送达中的
                for (CaseInfo caseInfo : caseCounts) {
                    Date girdSignTime = caseInfo.getGridSignTime();
                    if (girdSignTime == null) {
                        continue;
                    }
                    String caseId = caseInfo.getCaId();
                    String returnMail = caseInfo.getReturnMail();
                    String firstMail = caseInfo.getFirstMail();
                    List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                        setCaseId(firstMail);
                    }});
                    if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                        caseDelivingCounts.add(caseInfo);
                        continue;
                    }
                    DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                    int times = deliveryInfo.getDevNum();
////                        1:成功;2:未成功;3:强制退出;4:不强制送达三次
//                    String devStatus = deliveryInfo.getAttribute1();
//
//                    if (times >= 3 || "1".equals(devStatus) || "3".equals(devStatus) || "4".equals(devStatus)) {
//                        continue;
//                    }

                    /**
                     * 需求修改后
                     */
                    String status2 = deliveryInfo.getDevStatus();
                    if (status2.equals("3")) {
                        caseDelivingCounts.add(caseInfo);
                        continue;
                    }
                    int sta = Integer.parseInt(status2);
                    if (sta >= 7 && sta < 9) {
                        caseDelivingCounts.add(caseInfo);
                        continue;
                    }

//                    caseDelivingCounts.add(caseInfo);
                }


                Map<String, Object> outMap = new HashMap<String, Object>();

                Map<String, Object> res = new HashMap<String, Object>();
                res.put("sumCounts", caseCounts.size());
                res.put("sumSuccessCounts", caseSuccessCounts.size());
                res.put("sumUnsuccessCounts", caseUnsuccessCounts.size());
                res.put("sumDelivingCounts", caseDelivingCounts.size());
                res.put("sumNotSignCounts", caseNotSign.size());

                outMap.put("caseNums", res);

                String status = WebParamUtils.getString("status", request);
                List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
                if ("0".equals(status)) {
                    //总量
//                    res.put("resultCountLists", caseCounts);
                    caseInfoList = caseCounts;
                } else if ("1".equals(status)) {
                    //成功
//                    res.put("resultCountLists", caseSuccessCounts);
                    caseInfoList = caseSuccessCounts;
                } else if ("2".equals(status)) {
                    //失败
//                    res.put("resultCountLists", caseUnsuccessCounts);
                    caseInfoList = caseUnsuccessCounts;
                } else if ("3".equals(status)) {
                    //送达
//                    res.put("resultCountLists", caseDelivingCounts);
                    caseInfoList = caseDelivingCounts;
                } else if ("4".equals(status)) {
                    //未签收
//                    res.put("resultCountLists", caseDelivingCounts);
                    caseInfoList = caseNotSign;
                }
                List<Map> mapList = new ArrayList<Map>();
                for (CaseInfo caseInfo : caseInfoList) {
                    mapList.add(new HashMap() {{
                        put("c_id", caseInfo.getId());
                        put("caseNum", caseInfo.getCaseNum());
                        put("caseId", caseInfo.getCaId());

                    }});

                }

                outMap.put("caseList", mapList);


                resultVo.setErrCode(1);
                resultVo.setResult(outMap);

                return null;
            }
        }, request, response, sessionId);
    }

    /**
     * 描述：网格员下点击统计报表的数字，获取对应列表
     *
     * @return
     * @author： news
     * @time: 2018/9/7 10:59
     * @param: * @param null
     */
    @RequestMapping(value = "/gird/getReportDetailList")
    public void getReportDetailList(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                return null;
            }
        }, request, response, sessionId);
    }

}
