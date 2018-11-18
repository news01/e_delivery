/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.caseloginfo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc77.utils.DataExporter;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.e.delivery.*;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogDTO;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.wx.entity.EdeliveryExport;
import com.thinkgem.jeesite.util.ConfigurationService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.caseloginfo.entity.CaseLogInfo;
import com.thinkgem.jeesite.modules.caseloginfo.service.CaseLogInfoService;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 案件物流信息表Controller
 *
 * @author niushi
 * @version 2018-08-22
 */
@Controller
public class CaseLogInfoController extends BaseController {

    @Autowired
    private CaseLogInfoService caseLogInfoService;

    @Autowired
    private ConfigurationService configurationService;

    private DataExporter outDataExporter = null;


    //    @RequestMapping(value = "/caseLogInfo/getCaseLogInfo1", method = RequestMethod.POST)
    public ReExpressMail emsNotifyInterface(HttpServletRequest request, @RequestBody String caseLogDTO) {


        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = request.getReader();
            String temp = null;

            while ((temp = reader.readLine()) != null) {
                stringBuilder.append(temp);
            }

            logger.info("ems_notify_interface 原始报文:" + stringBuilder);
            ExpressMail expressMail = (ExpressMail) (new ObjectMapper()).readValue(stringBuilder.toString(), ExpressMail.class);

            ExpressMail expressMail1 = new ExpressMail();
            expressMail1.getListExpressMail();


            for (ExpressMail.ExpressMailItem item : expressMail.getListExpressMail()) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date czsj = new Date(simpleDateFormat.parse(item.getProcDate() + item.getProcTime()).getTime());
                System.out.println(item);

                CaseLogInfo caseLogInfo = new CaseLogInfo();


                //状态的顺序号（该字段表示着该条状态产生的顺序，没有实际意义）
                if (StringUtils.isNotBlank(item.getSerialNumber())) {
                    String serialnumber = item.getSerialNumber();

                }
                //邮件号码
                if (StringUtils.isNotBlank(item.getMailNum())) {
                    String mailnum = item.getMailNum();
                    caseLogInfo.setEmailId(mailnum);

                }
                //日期格式：YYYYMMDD
                if (StringUtils.isNotBlank(item.getProcDate())) {
                    String procdate = item.getProcDate();
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("YYYYMMDD");
                    caseLogInfo.setLogTime(sDateFormat.parse(procdate));

                }
                //时间格式：HHMMSS
                if (StringUtils.isNotBlank(item.getProcDate())) {
                    String proctime = item.getProcDate();
                    caseLogInfo.setAttribute1(proctime);

                }
                //所在地名称
                if (StringUtils.isNotBlank(item.getOrgFullName())) {
                    String orgfullname = item.getOrgFullName();
                    caseLogInfo.setAttribute2(orgfullname);

                }
                        /*
                        业务动作
                            00：收寄
                            10：妥投
                            20: 未妥投
                            30：经转过程中
                            40：离开处理中心
                            41：到达处理中心
                            50安排投递
                            51：正在投递
                            60：揽收
                         */
                if (StringUtils.isNotBlank(item.getAction())) {
                    String action = item.getAction();
                    caseLogInfo.setLogStatus(action);

                }
                //描述
                if (StringUtils.isNotBlank(item.getDescription())) {
                    String description = item.getDescription();
                    caseLogInfo.setLogInfo(description);

                }
                //有效无效
                //0：表示无效(表示该邮件当前这个状态标识为无效的状态，判断依据邮件号、日期、时间、动作)，
                //1：表示有效
                if (StringUtils.isNotBlank(item.getEffect())) {
                    String effect = item.getEffect();

                }
                //妥投（只有当action=10时该字段才有值，参见附录中签收情况代码表）
                if (StringUtils.isNotBlank(item.getProperDelivery())) {
                    String properdelivery = item.getProperDelivery();

                }
                //未妥投（只有当action=20的时候该字段才有值，参见附录中未妥投原因代码表）
                if (StringUtils.isNotBlank(item.getNotProperDelivery())) {
                    String notproperdelivery = item.getNotProperDelivery();

                }


                int resp = caseLogInfoService.insertCaseDelInfo(caseLogInfo);
                if (resp == 1) {


                    ReExpressMail reExpressMail = new ReExpressMail();
                    Response response = new Response();
                    response.setFailMailNums("");
                    response.setSuccess("1");
                    response.setRemark("");
                    response.setFailMailNums("");
                    reExpressMail.setResponse(response);
                    return reExpressMail;

                }
            }

        } catch (Exception var6) {
            logger.error("ems_notify_interface", var6);
        }

        ReExpressMail reExpressMail = new ReExpressMail();
        Response response = new Response();
        response.setFailMailNums("");
        response.setSuccess("1");
        response.setRemark("");
        response.setFailMailNums("");
        reExpressMail.setResponse(response);
        return reExpressMail;
    }


    /**
     * 描述：获取案件物流信息
     *
     * @return
     * @author： news
     * @time: 2018/8/27 14:26
     * @param: * @param null
     */
    @RequestMapping(value = "/caseLogInfo/getCaseLogInfo", method = RequestMethod.POST)
    public ReExpressMail getCaseLogInfo1(HttpServletRequest request, @RequestBody String caseLogDTO) {
        System.out.println(caseLogDTO);
        String listexpressmail = caseLogDTO;

        JSONObject jsonObject = JSONObject.fromObject(listexpressmail);

        if (jsonObject.has("listexpressmail")) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("listexpressmail");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                System.out.println(jsonObject1.toString());

                CaseLogInfo caseLogInfo = new CaseLogInfo();

                //状态的顺序号（该字段表示着该条状态产生的顺序，没有实际意义）
                if (jsonObject1.has("serialnumber")) {
                    String serialnumber = (String) jsonObject1.get("serialnumber");

                }
                //邮件号码
                if (jsonObject1.has("mailnum")) {
                    String mailnum = (String) jsonObject1.get("mailnum");
                    caseLogInfo.setEmailId(mailnum);
                }
                //日期格式：YYYYMMDD
                if (jsonObject1.has("procdate")) {
                    String procdate = (String) jsonObject1.get("procdate");
                    SimpleDateFormat sDateFormat2 = new SimpleDateFormat("YYYYMMDD");
                    Date aaa = null;
                    try {
                        aaa = sDateFormat2.parse(procdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    caseLogInfo.setLogTime(aaa);
                }
                //时间格式：HHMMSS
                if (jsonObject1.has("proctime")) {
                    String proctime = (String) jsonObject1.get("proctime");
                    caseLogInfo.setAttribute1(proctime);

                }
                //所在地名称
                if (jsonObject1.has("orgfullname")) {
                    String orgfullname = (String) jsonObject1.get("orgfullname");
                    caseLogInfo.setAttribute2(orgfullname);
                }
                        /*
                        业务动作
                            00：收寄
                            10：妥投
                            20: 未妥投
                            30：经转过程中
                            40：离开处理中心
                            41：到达处理中心
                            50安排投递
                            51：正在投递
                            60：揽收
                         */
                if (jsonObject1.has("action")) {
                    String action = (String) jsonObject1.get("action");
                    caseLogInfo.setLogStatus(action);
                }
                //描述
                if (jsonObject1.has("description")) {
                    String description = (String) jsonObject1.get("description");
                    caseLogInfo.setLogInfo(description);
                }
                //有效无效
                //0：表示无效(表示该邮件当前这个状态标识为无效的状态，判断依据邮件号、日期、时间、动作)，
                //1：表示有效
                if (jsonObject1.has("effect")) {
                    String effect = (String) jsonObject1.get("effect");

                }
                //妥投（只有当action=10时该字段才有值，参见附录中签收情况代码表）
                if (jsonObject1.has("properdelivery")) {
                    String properdelivery = (String) jsonObject1.get("properdelivery");
                }
                //未妥投（只有当action=20的时候该字段才有值，参见附录中未妥投原因代码表）
                if (jsonObject1.has("notproperdelivery")) {
                    String notproperdelivery = (String) jsonObject1.get("notproperdelivery");
                }

                int resp = caseLogInfoService.insertCaseDelInfo(caseLogInfo);
                ReExpressMail reExpressMail = new ReExpressMail();
                if (resp == 1) {
                    /*
                    导出
                     */
                    if (outDataExporter == null) {
                        outDataExporter = new DataExporter(configurationService.getInTaskPath(),configurationService.getPrefixNw());
                    }

                    EdeliveryExport edeliveryExport = new EdeliveryExport();
                    edeliveryExport.setCaseLogInfo(caseLogInfo);
                    outDataExporter.export(edeliveryExport);



                    Response response = new Response();
                    response.setFailMailNums("");
                    response.setSuccess("1");
                    response.setRemark("");
                    response.setFailMailNums("");
                    reExpressMail.setResponse(response);
                }
                return reExpressMail;


            }

        }
        return null;

    }

    /**
     * 描述：获取案件物流信息
     *
     * @return
     * @author： news
     * @time: 2018/8/22 19:14
     * @param: * @param null
     */

//    @RequestMapping(value = "/caseLogInfo/getCaseLogInfo", method = RequestMethod.POST)
    public void getCaseLogInfo(HttpServletRequest request, HttpServletResponse response, String sessionId, @RequestBody String caseLogDTO) {


        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                System.out.println(caseLogDTO);
                String listexpressmail = caseLogDTO;

                JSONObject jsonObject = JSONObject.fromObject(listexpressmail);

                if (jsonObject.has("listexpressmail")) {
//                    JSONObject jsonObject1 = ;
                    JSONArray jsonArray = (JSONArray) jsonObject.get("listexpressmail");


                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        System.out.println(jsonObject1.toString());

                        CaseLogInfo caseLogInfo = new CaseLogInfo();


                        //状态的顺序号（该字段表示着该条状态产生的顺序，没有实际意义）
                        if (jsonObject1.has("serialnumber")) {
                            String serialnumber = (String) jsonObject1.get("serialnumber");

                        }
                        //邮件号码
                        if (jsonObject1.has("mailnum")) {
                            String mailnum = (String) jsonObject1.get("mailnum");
                            caseLogInfo.setEmailId(mailnum);

                        }
                        //日期格式：YYYYMMDD
                        if (jsonObject1.has("procdate")) {
                            String procdate = (String) jsonObject1.get("procdate");
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("YYYYMMDD");
                            caseLogInfo.setLogTime(sDateFormat.parse(procdate));

                        }
                        //时间格式：HHMMSS
                        if (jsonObject1.has("proctime")) {
                            String proctime = (String) jsonObject1.get("proctime");
                            caseLogInfo.setAttribute1(proctime);

                        }
                        //所在地名称
                        if (jsonObject1.has("orgfullname")) {
                            String orgfullname = (String) jsonObject1.get("orgfullname");
                            caseLogInfo.setAttribute2(orgfullname);

                        }
                        /*
                        业务动作
                            00：收寄
                            10：妥投
                            20: 未妥投
                            30：经转过程中
                            40：离开处理中心
                            41：到达处理中心
                            50安排投递
                            51：正在投递
                            60：揽收
                         */
                        if (jsonObject1.has("action")) {
                            String action = (String) jsonObject1.get("action");
                            caseLogInfo.setLogStatus(action);

                        }
                        //描述
                        if (jsonObject1.has("description")) {
                            String description = (String) jsonObject1.get("description");
                            caseLogInfo.setLogInfo(description);

                        }
                        //有效无效
                        //0：表示无效(表示该邮件当前这个状态标识为无效的状态，判断依据邮件号、日期、时间、动作)，
                        //1：表示有效
                        if (jsonObject1.has("effect")) {
                            String effect = (String) jsonObject1.get("effect");

                        }
                        //妥投（只有当action=10时该字段才有值，参见附录中签收情况代码表）
                        if (jsonObject1.has("properdelivery")) {
                            String properdelivery = (String) jsonObject1.get("properdelivery");

                        }
                        //未妥投（只有当action=20的时候该字段才有值，参见附录中未妥投原因代码表）
                        if (jsonObject1.has("notproperdelivery")) {
                            String notproperdelivery = (String) jsonObject1.get("notproperdelivery");

                        }

                        int resp = caseLogInfoService.insertCaseDelInfo(caseLogInfo);
                        if (resp == 1) {


                            resultVo.setErrCode(1);
                            resultVo.setErrMsg("保存成功");
                        }


                    }

                }


                return null;
            }
        }, request, response, sessionId);

    }


}