package com.thinkgem.jeesite.modules.caseinfo.web;

import com.hc77.utils.DataExporter;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.e.delivery.*;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import com.thinkgem.jeesite.modules.deliveryInfo.service.DeliveryInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import com.thinkgem.jeesite.modules.wx.entity.EdeliveryExport;
import com.thinkgem.jeesite.util.ConfigurationService;
import net.sf.json.JSONObject;
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
 * @date 2018/8/25  11:01
 */
@Controller
public class JudgeController {

    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private ConfigurationService configurationService;

    private DataExporter outDataExporter = null;


    /**
     * 描述：法官助理扫描邮码
     *
     * @return
     * @author： news
     * @time: 2018/8/25 11:04
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/judgeScanMail")
    public void judgeScanMail(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String emailCode = WebParamUtils.getString("dev_code", request);
                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setReturnMail(emailCode);
                }});

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    int size = caseInfoList.size();
                    if (size > 1 || size < 1) {
                        resultVo.setErrCode(2);
                        resultVo.setErrMsg("结果不正确");
                        return null;
                    }
                    CaseInfo caseInfo = caseInfoList.get(0);

                    Date date = caseInfo.getCourtSignTime();
                    if (date != null) {
                        resultVo.setErrCode(-1);
                        resultVo.setErrMsg("已经接收过");
                        return null;
                    }


                    caseInfoList1.add(new HashMap<String, Object>() {
                        {
                            put("c_id", caseInfo.getId());
                            put("caId", caseInfo.getCaId());
                            put("caseNum", caseInfo.getCaseNum());

                        }
                    });
                }

                if (caseInfoList1.isEmpty()) {
                    resultVo.setErrCode(0);
                } else {
                    resultVo.setErrCode(1);
                    resultVo.setResult(caseInfoList1);
                }

                return null;
            }
        }, request, response, sessionId);
    }

    /**
     * 描述：法官助理签收案件
     *
     * @return
     * @author： news
     * @time: 2018/8/25 11:30
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/judgeComfirmSign")
    public void judgeComfirmSign(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String c_id = WebParamUtils.getString("c_id", request);
                String userId = WebParamUtils.getString("userId", request);

                Date date2 = new Date();
                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setId(c_id);
                }});

                List<Map> caseInfoList1 = new ArrayList<Map>();
                int resp = 0;
                if (caseInfoList != null) {
                    int size = caseInfoList.size();
                    if (size > 1 || size < 1) {
                        resultVo.setErrCode(2);
                        resultVo.setErrMsg("结果不正确");
                        return null;
                    }
                    CaseInfo caseInfo = caseInfoList.get(0);

                    Date date = caseInfo.getCourtSignTime();
                    if (date != null) {
                        resultVo.setErrCode(-1);
                        resultVo.setErrMsg("已经接收过");
                        return null;
                    }

                    CaseInfo caseInfo1 = new CaseInfo() {{
                        setCourtId(userId);
                        setCourtSignTime(date2);
                        setCaseStatus("7");
                        setId(c_id);
                    }};
                    resp = caseInfoService.updateCaseInfoById(caseInfo1);

                    /*
                    导出
                     */
                    if (outDataExporter == null) {
                        outDataExporter = new DataExporter(configurationService.getInTaskPath(),configurationService.getPrefixNw());
                    }
                    CaseInfo caseInfo2 = caseInfoService.get(c_id);
                    caseInfo2.setCourtId(userId);
                    caseInfo2.setCourtSignTime(date2);
                    caseInfo2.setCaseStatus("7");


                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                    deliveryInfo.setCaseId(caseInfo2.getFirstMail());
                    deliveryInfo.setDevStatus("13");
                    deliveryInfo.setDevGird(userId);//签收返程码的人员id

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date2);
                    deliveryInfo.setDevTime2(time);
                    deliveryInfo.setDevTime(date2);

                    DeliveryInfo deliveryInfo1 = new DeliveryInfo();
                    deliveryInfo1.setCaseId(caseInfo2.getFirstMail());
                    List<DeliveryInfo>  deliveryInfos = deliveryInfoService.getDelInfoByCaseId(deliveryInfo1);
                    if(!deliveryInfos.isEmpty()){
                        DeliveryInfo deliveryInfo2 = deliveryInfos.get(0);
                        String resp2 = deliveryInfo2.getAttribute1();
                        deliveryInfo.setAttribute1(resp2);
                    }
                    deliveryInfoService.insertDeliveryInfo(deliveryInfo);


                    DeliveryInfo deliveryInfo3 = deliveryInfoService.getTheLastDev(deliveryInfo1);

                    EdeliveryExport edeliveryExport = new EdeliveryExport();
                    edeliveryExport.setCaseInfo(caseInfo2);
                    edeliveryExport.setDeliveryInfo(deliveryInfo3);
                    outDataExporter.export(edeliveryExport);

                }

                if (resp <= 0) {
                    resultVo.setErrCode(0);
                } else {
                    resultVo.setErrCode(1);
                }

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：法官助理已接受案件
     *
     * @return
     * @author： news
     * @time: 2018/8/25 15:17
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/getAllAcceptedCase")
    public void getAllAcceptedCase(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                String sessionId2 = WebParamUtils.getString("sessionId", request);


                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                    setCourtId(userId);
                }});

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        Date date = caseInfo.getCourtSignTime();
                        if (date == null) {
                            continue;
                        }
                        String courId = caseInfo.getCourtId();
                        if (courId.equals(userId)) {
                            caseInfoList1.add(new HashMap() {
                                {
                                    put("c_id", caseInfo.getId());
                                    put("caseId", caseInfo.getCaId());
                                    put("case_num", caseInfo.getCaseNum());//案号
                                }
                            });
                        }
                    }
                }

                if (caseInfoList1.isEmpty()) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("没有签收案件");
                } else {
                    resultVo.setErrCode(1);
                    resultVo.setResult(caseInfoList1);
                }


                return null;
            }
        }, request, response, sessionId);
    }

    /**
     * 描述：法官助理根据是否被当事人接收查询案件列表
     *
     * @return
     * @author： news
     * @time: 2018/8/25 16:16
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/getAllCaseByStatus")
    public void getAllCaseByStatus(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String userId = WebParamUtils.getString("userId", request);
                //0:失败；1：送达成功
                String status = WebParamUtils.getString("status", request);

                if (StringUtils.isBlank(userId) || StringUtils.isBlank(status)) {
                    resultVo.setErrCode(-2);
                    resultVo.setErrMsg("参数不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = null;
                Date date = new Date();
                if ("0".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{

                        setCourtId(userId);
                        setAttribute4("5");
                        setEmsSignTime(date);
                        setDeliverSignTime(null);
                    }});

                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCourtId(userId);
                        setAttribute4("5");
                        setEmsSignTime(date);
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
                    resultVo.setErrCode(1);
                    resultVo.setResult(mapList);

                } else {
                    resultVo.setErrCode(0);
                }

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：法官助理送达情况下模糊查询
     *
     * @return
     * @author： news
     * @time: 2018/8/25 16:29
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/getCaseInfoByLike")
    public void getCaseInfoByLike(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String like = WebParamUtils.getString("like", request);
                String status = WebParamUtils.getString("status", request);
                String userId = WebParamUtils.getString("userId", request);

                if (StringUtils.isBlank(userId) || StringUtils.isBlank(like) || StringUtils.isBlank(status)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("参数不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = null;

                //0:失败；1：送达成功
                /**
                 * -1:未被当事人成功签收
                 * 0：次数未满3次，送达状态未成功
                 * 1：成功被当事人接收
                 */
                Date date = new Date();
                if ("0".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCourtId(userId);
                        setDeliverSignTime(null);
                        setEmsSignTime(date);
                        setAttribute4("5");
                        setCaseNum(like);
                    }});

                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCourtId(userId);
                        setAttribute4("5");//sql判断
                        setEmsSignTime(date);
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
     * 描述：法官助理下已完成送达案件
     *
     * @return
     * @author： news
     * @time: 2018/8/31 10:46
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/completedCaseList")
    public void completedCaseList(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String pageSize1 = WebParamUtils.getString("pageSize", request);
//                String currentPageNo1 = WebParamUtils.getString("pageNo", request);
                Integer pageSize = Integer.parseInt(pageSize1);


                /**
                 * 1.邮政人员签收
                 * 2.本社区下
                 * 3.不管有没有被当事人接收到，完成三次都算已完成
                 */
                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setAttribute4("2");
                        setEmsSignTime(new Date());
                        setPage(new Page<CaseInfo>(request, response, pageSize));
                    }
                });

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        caseInfoList1.add(new HashMap<String, Object>() {
                            {

                                put("c_id", caseInfo.getId());
                                put("caId", caseInfo.getCaId());
                                put("caseNum", caseInfo.getCaseNum());
                                put("courtSignTime", caseInfo.getCourtSignTime());//法院签收时间

                            }
                        });
                    }

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
     * 描述：法官助理下已完成送达案件下的模糊查询
     *
     * @return
     * @author： news
     * @time: 2018/9/4 10:36
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/completedCaseListByLike")
    public void completedCaseListByLike(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

//                Integer pageSize = WebParamUtils.getInteger("pageSize", request);
//                Integer currentPageNo = WebParamUtils.getInteger("pageNo", request);
                String like = WebParamUtils.getString("like", request);


                /**
                 * 1.邮政人员签收
                 * 2.本社区下
                 * 3.不管有没有被当事人接收到，完成三次都算已完成
                 */
                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setAttribute4("2");
                        setEmsSignTime(new Date());
                        setCaseNum(like);
                    }
                });

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
                        caseInfoList1.add(new HashMap<String, Object>() {
                            {

                                put("c_id", caseInfo.getId());
                                put("caId", caseInfo.getCaId());
                                put("caseNum", caseInfo.getCaseNum());
                                put("courtSignTime", caseInfo.getCourtSignTime());//法院签收时间

                            }
                        });
                    }

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
     * 描述：法官助理送达统计,获取所有社区
     *
     * @return
     * @author： news
     * @time: 2018/9/8 11:28
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/getCommunity")
    public void getCommunity(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                List<Office> list = officeService.getAllOffice();
                List<Map> resList = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Office o = list.get(i);
                    String id = o.getId();
                    String name = o.getName();

                    resList.add(new HashMap() {{
                        put("communityId", id);
                        put("communityName", name);
                    }});

                }
                resultVo.setErrCode(1);
                resultVo.setResult(resList);

                return null;
            }
        }, request, response, sessionId);
    }

    /**
     * 描述：法官助理送达统计
     *
     * @return
     * @author： news
     * @time: 2018/9/8 11:28
     * @param: * @param null
     */
    @RequestMapping(value = "/judge/getReportCount")
    public void getReportCount(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String communityId = WebParamUtils.getString("communityId", request);
                String communityName = WebParamUtils.getString("communityName", request);
                String currentdata = WebParamUtils.getString("currentdata", request);
                String status = WebParamUtils.getString("status", request);

                List<CaseInfo> communitySignCounts = new ArrayList<CaseInfo>();
                if ("-1".equals(status) || "0".equals(status)) {
                    //社区签收总量
                    communitySignCounts = caseInfoService.getCaseInfo(new CaseInfo() {{
                        setCommuntityId(communityId);
                        setRemarks(currentdata);
                    }});

                }

                List<CaseInfo> deliverySuccess = null;
                if ("-1".equals(status) || "1".equals(status)) {
                    //社区送达成功的
                    deliverySuccess = caseInfoService.getCaseInfo(new CaseInfo() {{
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(new Date());
                        setRemarks(currentdata);
                    }});
                }

                List<CaseInfo> deliveryUNsuccess = null;
                if ("-1".equals(status) || "2".equals(status)) {
                    //社区送达失败的
                    List<CaseInfo> deliveryUNsuccess2 = caseInfoService.getCaseInfo(new CaseInfo() {{
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(null);
                        setRemarks(currentdata);
                    }});
                    deliveryUNsuccess = new ArrayList<CaseInfo>();
                    for (CaseInfo caseInfo : deliveryUNsuccess2) {
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});

                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            //没有送达信息
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                        int times = deliveryInfo.getDevNum();
                        String status2 = deliveryInfo.getAttribute1();

                        if (times >= 3 && !"1".equals(status2) || times <= 3 && "3".equals(status2) || times <= 3 && "4".equals(status2)) {
                            deliveryUNsuccess.add(caseInfo);
                        }

                    }
                }

                List<CaseInfo> deliveryIng = null;
                if ("-1".equals(status) || "3".equals(status)) {
                    //送达中
                    List<CaseInfo> caseInfoList2 = null;
                    if ("-1".equals(status)) {
                        caseInfoList2 = communitySignCounts;
                    } else if ("3".equals(status)) {
                        caseInfoList2 = caseInfoService.getCaseInfo(new CaseInfo() {{
                            setCommuntityId(communityId);
                            setRemarks(currentdata);
                        }});
                    }

                    deliveryIng = new ArrayList<>();
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
                            deliveryIng.add(caseInfo);
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                        int times = deliveryInfo.getDevNum();
//                        1:成功;2:未成功;3:强制退出;4:不强制送达三次
                        String devStatus = deliveryInfo.getAttribute1();

                        if (times >= 3 || "1".equals(devStatus) || "3".equals(devStatus) || "4".equals(devStatus)) {
                            continue;
                        }
                        deliveryIng.add(caseInfo);
                    }

                }

                List<Map> resList = new ArrayList<>();
                if ("-1".equals(status)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("communityId", communityId);
                    map.put("communityName", communityName);
                    map.put("communitySignCounts", communitySignCounts.size());
                    map.put("deliverySuccess", deliverySuccess.size());
                    map.put("deliveryUNsuccess", deliveryUNsuccess.size());
                    map.put("deliveryIng", deliveryIng.size());

                    resList.add(map);

//                    resList.add(new HashMap() {{
//                        put("communityId", id);
//                        put("communityName", name);
//                        put("communitySignCounts", communitySignCounts.size());
//                        put("deliverySuccess", deliverySuccess.size());
//                        put("deliveryUNsuccess", deliveryUNsuccess.size());
//                        put("deliveryIng", deliveryIng.size());
//                    }});
                } else {

                    List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
                    if ("0".equals(status)) {
                        //总量
                        caseInfoList = communitySignCounts;
                    } else if ("1".equals(status)) {
                        //成功
                        caseInfoList = deliverySuccess;
                    } else if ("2".equals(status)) {
                        //失败
                        caseInfoList = deliveryUNsuccess;
                    } else if ("3".equals(status)) {
                        //送达
                        caseInfoList = deliveryIng;
                    }

                    List<Map> mapList = new ArrayList<Map>();
                    for (CaseInfo caseInfo : caseInfoList) {
                        mapList.add(new HashMap() {{
                            put("c_id", caseInfo.getId());
                            put("caseNum", caseInfo.getCaseNum());
                            put("caseId", caseInfo.getCaId());
                            put("girdSignTime", caseInfo.getGridSignTime());
                            put("courtSignTime", caseInfo.getCourtSignTime());

                        }});

                    }
                    resList.add(new HashMap() {{
                        put("communityId", communityId);
                        put("communityName", communityName);
                        put("caseListResult", mapList);
                    }});
                }

                resultVo.setErrCode(1);
                resultVo.setResult(resList);

                return null;
            }
        }, request, response, sessionId);
    }


}
