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
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import com.thinkgem.jeesite.util.ConfigurationService;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.modules.caseinfo.web
 * @Desc: ${end}
 * @date 2018/8/21  15:57
 */
@Controller
public class GirdCenterController {

    Logger logger = Logger.getLogger(GirdCenterController.class);

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
     * 描述：获取社区下所有网格员
     *
     * @return
     * @author： news
     * @time: 2018/8/13 15:21
     * @param: * @param null
     */
    @RequestMapping(value = "/girdCenter/getGridWorkersInTheCommunity")
    public void getGridWorkersInTheCommunity(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {
                String communityId = WebParamUtils.getString("communityId", request);
                logger.info("-----communityId------"+communityId);

                try {
                    List<SysUser> sysUserList = null;
                    if (StringUtils.isNotBlank(communityId)) {
                        //logger.info("-----进来了1------"+sysUserList.size());
                        SysUser sysUser = new SysUser();
                        Office office = new Office();
                        office.setId(communityId);

                        sysUser.setOffice(office);
                        sysUser.setUserType("3");

                        logger.info("-----进来了2------" + sysUser);

                        sysUserList = sysUserService.getSysUserByCommunityId(sysUser);
                        logger.info("-----communityId------" + sysUserList.size());

//                    sysUserList = sysUserService.getSysUserByCommunityId(new SysUser() {
//                        {
//                            setOffice(new Office() {
//                                {
//                                    setId(communityId);
//                                }
//                            });
//                            setUserType("3");
//
//                        }
//                    });
                        logger.info("-----communityId------" + sysUserList.size());
                    }

                    List<Map> caseInfoList1 = new ArrayList<Map>();
                    logger.info("-----communityId------" + sysUserList);
                    if (sysUserList != null) {

                        for (SysUser sysUser : sysUserList) {
                            String id = sysUser.getId();
                            String name = sysUser.getName();
                            caseInfoList1.add(new HashMap<String, Object>() {
                                {
                                    put("id", id);
                                    put("name", name);
                                }
                            });
                        }

                        resultVo.setErrCode(1);
                    } else {
                        resultVo.setErrCode(0);
                    }

                    resultVo.setResult(caseInfoList1);

                    return null;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }

                return null;
            }
        }, request, response, sessionId);
    }

    /**
     * 描述：网格中心已完成送达案件
     *
     * @return
     * @author： news
     * @time: 2018/8/18 16:10
     * @param: * @param null
     */
    @RequestMapping(value = "/girdCenter/completedCaseList")
    public void completedCaseList(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                /**
                 * 2.本社区下
                 * 3.不管有没有被当事人接收到，完成三次都算已完成
                 * 送达成功 送达失败的都显示，如果网格办签收返程的邮码后，显示已签收
                 */

                String com_id = WebParamUtils.getString("com_id", request);//社区编号

                List<CaseInfo> caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {
                    {
                        setCommuntityId(com_id);
//                        setAttribute4("2");
//                        setEmsSignTime(new Date());
                        setAttribute4("1");
                        setGridSignTime(new Date());

                    }
                });

                List<Map> caseInfoList1 = new ArrayList<Map>();
                if (caseInfoList != null) {
                    for (CaseInfo caseInfo : caseInfoList) {
//                        4:送达完成且未被当事人签收;5:送达完成且被当事人签收;6:网格中心扫描返程邮码(快递中);7:法官助理签收',
                        String caseStatus = caseInfo.getCaseStatus();
                        Map<String,Object> map = new HashMap<>();
                        if("4".equals(caseStatus)||"5".equals(caseStatus)||"6".equals(caseStatus)||"7".equals(caseStatus)){
                            caseInfoList1.add(new HashMap(){{
                                put("c_id", caseInfo.getId());
                                put("caId", caseInfo.getCaId());
                                put("caseNum", caseInfo.getCaseNum());
                                put("emsSignTime", caseInfo.getEmsSignTime());
                            }});
//                            map.put("c_id", caseInfo.getId());
//                            map.put("caId", caseInfo.getCaId());
//                            map.put("caseNum", caseInfo.getCaseNum());
                            continue;
                        }
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
                       List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo(){{
                            setCaseId(firstMail);
                        }});
                       if(deliveryInfos ==null || deliveryInfos.isEmpty()){
                           continue;
                       }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                       int times = deliveryInfo.getDevNum();
//                       String  delStatus = deliveryInfo.getAttribute1();
////                       if(times >= 3 || times <= 3 && "1".equals(delStatus) || times <= 3 && "3".equals(delStatus) || times <= 3 && "4".equals(delStatus)){
//                        if(!(times<3 && "2".equals(delStatus))){
//                           caseInfoList1.add(new HashMap(){{
//                               put("c_id", caseInfo.getId());
//                               put("caId", caseInfo.getCaId());
//                               put("caseNum", caseInfo.getCaseNum());
//                               put("emsSignTime", caseInfo.getEmsSignTime());
//                           }});
//                           continue;
//                       }
                        String status = deliveryInfo.getDevStatus();
                        int sta = Integer.parseInt(status);
                        if(sta>=9){
                            caseInfoList1.add(new HashMap(){{
                               put("c_id", caseInfo.getId());
                               put("caId", caseInfo.getCaId());
                               put("caseNum", caseInfo.getCaseNum());
                               put("emsSignTime", caseInfo.getEmsSignTime());
                           }});
                           continue;
                        }

//                        caseInfoList1.add(map);
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
     * 描述：网格中心送达情况，根据状态查询案件信息
     *
     * @return
     * @author： news
     * @time: 2018/8/21 16:12
     * @param: * @param null
     */
    @RequestMapping(value = "/girdCenter/getDelInfoByStatus")
    public void getDelInfoByStatus(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


//                String userId = WebParamUtils.getString("userId", request);
                String communityId = WebParamUtils.getString("communityId", request);
                String status = WebParamUtils.getString("status", request);


                if (StringUtils.isBlank(communityId)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();

                //0:送达中；1：送达成功；2：失败
                /**
                 * -1:未被当事人成功签收
                 * 0：次数未满3次，送达状态未成功
                 * 1：成功被当事人接收
                 */
                if ("2".equals(status)) {

                    List<CaseInfo> caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(null);
                    }});

                    for (CaseInfo caseInfo : caseInfoList2) {
                        String caseId = caseInfo.getCaId();
                        String returnMail = caseInfo.getReturnMail();
                        String firstMail = caseInfo.getFirstMail();
//                        caseInfoList = new ArrayList<CaseInfo>();
                        List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                            setCaseId(firstMail);
                        }});

                        if (deliveryInfos == null || deliveryInfos.isEmpty()) {
                            //没有送达信息
                            continue;
                        }
                        DeliveryInfo deliveryInfo = deliveryInfos.get(0);
//                        int times = deliveryInfo.getDevNum();
//                        String status2 = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 && !"1".equals(status2) || times <= 3 && "3".equals(status2) || times <= 3 && "4".equals(status2)) {
//                            caseInfoList.add(caseInfo);
//                        }

                        /**
                         * 需求修改后
                         */
                        String status2 = deliveryInfo.getDevStatus();
                        int sta = Integer.parseInt(status2);
                        if(sta>=9 && sta<12){
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        String caseStatus2 = deliveryInfo.getAttribute1();
                        if("2".equals(caseStatus2) || "3".equals(caseStatus2) || "4".equals(caseStatus2)){
                            caseInfoList.add(caseInfo);
                            continue;
                        }

                    }

                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCommuntityId(communityId);
                        setAttribute4("3");
                        setDeliverSignTime(new Date());
                    }});
                } else if ("0".equals(status)) {
                    List<CaseInfo> caseInfoList2 = new ArrayList<>();
                    caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
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
                        if(status2.equals("3")){
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        int sta = Integer.parseInt(status2);
                        if(sta>=7 && sta<9){
                            caseInfoList.add(caseInfo);
                            continue;
                        }

//                        caseInfoList.add(caseInfo);
                    }
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
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心送达情况，根据案号模糊查询
     *
     * @return
     * @author： news
     * @time: 2018/8/21 17:04
     * @param: * @param null
     */
    @RequestMapping(value = "/girdCenter/getCaseInfoByLike")
    public void getCaseInfoByLike(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                String like = WebParamUtils.getString("like", request);
                String status = WebParamUtils.getString("status", request);
                String com_id = WebParamUtils.getString("com_id", request);

                if (StringUtils.isBlank(com_id) || StringUtils.isBlank(status)) {
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
                        setCommuntityId(com_id);
                        setDeliverSignTime(null);
                        setAttribute4("4");
                        setCaseNum(like);
                    }});

                    for (CaseInfo caseInfo : caseInfoList2) {
                        String caseId = caseInfo.getCaId();
//                        caseInfoList = new ArrayList<CaseInfo>();
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
//                        int times = deliveryInfo.getDevNum();
//                        String status2 = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 && !"1".equals(status2) || times <= 3 && "3".equals(status2) || times <= 3 && "4".equals(status2)) {
//                            caseInfoList.add(caseInfo);
//                        }
                        /**
                         * 需求修改后
                         */
                        String status2 = deliveryInfo.getDevStatus();
                        int sta = Integer.parseInt(status2);
                        if(sta>=9 && sta<12){
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        String caseStatus2 = deliveryInfo.getAttribute1();
                        if("2".equals(caseStatus2) || "3".equals(caseStatus2) || "4".equals(caseStatus2)){
                            caseInfoList.add(caseInfo);
                            continue;
                        }

                    }

                } else if ("1".equals(status)) {
                    caseInfoList = caseInfoService.selectCaseInfo(new CaseInfo() {{
                        setCommuntityId(com_id);
                        setAttribute4("4");//sql判断
                        setCaseNum(like);
                        setDeliverSignTime(new Date());
                    }});
                } else if ("0".equals(status)) {
                    List<CaseInfo> caseInfoList2 = new ArrayList<>();
                    caseInfoList2 = caseInfoService.selectCaseInfo(new CaseInfo() {{
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
////                        1:成功;2:未成功'3:强制退出;4:不强制送达三次
//                        String devStatus = deliveryInfo.getAttribute1();
//
//                        if (times >= 3 || "1".equals(devStatus) || "3".equals(devStatus) || "4".equals(devStatus)) {
//                            continue;
//                        }
                        /**
                         * 需求修改后
                         */
                        String status2 = deliveryInfo.getDevStatus();
                        if(status2.equals("3")){
                            caseInfoList.add(caseInfo);
                            continue;
                        }
                        int sta = Integer.parseInt(status2);
                        if(sta>=7 && sta<9){
                            caseInfoList.add(caseInfo);
                            continue;
                        }

//                        caseInfoList.add(caseInfo);
                    }
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
        }, request, response, sesionId);

    }


    /**
     * 描述：网格中心报表统计
     *
     * @return
     * @author： news
     * @time: 2018/9/7 14:30
     * @param: * @param null
     */
    @RequestMapping(value = "/girdCenter/getReportCount")
    public void getReportCount(HttpServletRequest request, HttpServletResponse response, String sesionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                String status = WebParamUtils.getString("status", request);
                String communityId = WebParamUtils.getString("communityId", request);

                if (StringUtils.isBlank(communityId) || StringUtils.isBlank(status)) {
                    resultVo.setErrCode(-1);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }
                List<SysUser> sysUserList = sysUserService.getSysUserByCommunityId(new SysUser() {{
                    setOffice(new Office() {
                        {
                            setId(communityId);
                        }
                    });
                    setUserType("3");
                }});

                List<Map> mapList = new ArrayList<Map>(sysUserList.size());

                for (SysUser sysUser : sysUserList) {
                    mapList.add(new HashMap() {{
                        put("u_id", sysUser.getId());
                        put("u_Name", sysUser.getName());
                    }});
                }
                resultVo.setErrCode(1);
                resultVo.setResult(mapList);

                return null;
            }
        }, request, response, sesionId);

    }


}
