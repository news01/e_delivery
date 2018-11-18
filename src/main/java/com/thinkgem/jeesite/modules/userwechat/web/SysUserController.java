/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.userwechat.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hc77.utils.DataExporter;
//import com.ndktools.javamd5.Mademd5;
import com.thinkgem.jeesite.common.utils.e.delivery.*;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.userwechat.dao.SysUserWechatDao;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserService;
import com.thinkgem.jeesite.util.*;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 小程序用户表Controller,小程序的用户dto
 *
 * @author niushi
 * @version 2018-08-01
 */
@Controller
public class SysUserController extends BaseController {

    Logger logger = Logger.getLogger(SysUserController.class);
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserWechatDao userWechatDao;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private ConfigurationService configurationService;


    @Value(value = "${inServerTaskRoot}")
    private String strExportPath;
    @Value(value = "${prefixWxDsr}")
    private String strPrefixWxDsr;
    private DataExporter wxDsrExporter = null;


//    @Value("${prefixWithdraw}")
//    private String strPrefixWithdraw;
//
//    @Value("${outServerKdxxRoot}")
//    private String outServerTaskRoot;


    private DataExporter outDataExporter = null;


    @ModelAttribute
    public SysUser get(@RequestParam(required = false) String id) {
        SysUser entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysUserService.get(id);
        }
        if (entity == null) {
            entity = new SysUser();
        }
        return entity;
    }

    /**
     * 描述：用户注册
     *
     * @return
     * @author： news
     * @time: 2018/8/1 16:43
     * @param: * @param null
     */
/*
    @RequestMapping(value = "register")
    public void register(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String paramters = WebParamUtils.getString("paramters", request);

                String loginname = WebParamUtils.getString("loginname", request);
                String password = WebParamUtils.getString("password", request);
                String relaname = WebParamUtils.getString("relaname", request);
                String phone = WebParamUtils.getString("phone", request);
                String iden = WebParamUtils.getString("iden", request);
                String commiid = WebParamUtils.getString("commi", request);//社区编号
//                String commid = WebParamUtils.getString("commid", request);
                String openId = WebParamUtils.getString("openId", request);

                if (StringUtils.isBlank(loginname) || StringUtils.isBlank(password) || StringUtils.isBlank(relaname) || StringUtils.isBlank(phone)
                        || StringUtils.isBlank(iden)) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }
                if ((iden.equals("社区专职人员") || iden.equals("网格员")) && StringUtils.isBlank(commiid)) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }
                SysUser sysUser2 = new SysUser();
                sysUser2.setId(phone);
//                SysUser sysUser1 = sysUserService.get(sysUser2);
                SysUser sysUser1 = sysUserService.getSysUserByIdOrMobile(sysUser2);

                if (sysUser1 != null) {
                    resultVo.setErrCode(1);
                    resultVo.setErrMsg("已经注册过");
                    return null;
                }

                SysUser user1 = new SysUser();
                user1.setLoginName(loginname);
                String passWord = new Mademd5().toMd5(password);
                user1.setPassword(passWord);
                user1.setName(relaname);
                user1.setPhone(phone);

                //company_id;1:邮政;2:社区;3:法院'
                //userType : 1：管理员；2：社区专职人员；3：网格员；4：邮递员；5：法院工作人员',
                if (iden.equals("邮递员")) {
                    user1.setCompanyId("1");
                    user1.setUserType("4");
                }
                if (iden.equals("社区专职人员") || iden.equals("网格员")) {
                    user1.setCompanyId("2");
                    Office office = new Office();
                    office.setId(commiid);
                    user1.setOffice(office);
                    if (iden.equals("社区专职人员")) {
                        user1.setUserType("2");
                    } else if (iden.equals("网格员")) {
                        user1.setUserType("3");
                    }

                }
                if (iden.equals("法院人员")) {
                    user1.setCompanyId("3");
                    user1.setUserType("5");
                }

                user1.setId(phone);//一个手机号只能注册一次
                user1.setLoginFlag("1");

                int resp = sysUserService.insertSysUser(user1);
                if (resp > 0) {
                    resultVo.setErrCode(1);
                    resultVo.setErrMsg("注册成功");
                } else {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("注册失败");
                }
                return null;
            }
        }, request, response, sessionId);
    }
*/

    /**
     * 描述：获取所有社区
     *
     * @return
     * @author： news
     * @time: 2018/7/31 23:49
     * @param: * @param null
     */
    @RequestMapping(value = "getAllOffice")
    public void getAllOffice(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                List<Office> list = officeService.getAllOffice();
                List resList = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Office o = list.get(i);
                    String id = o.getId();
                    String name = o.getName();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", id);
                    jsonObject.put("name", name);
                    resList.add(jsonObject);
                }
                String result = JSONUtil.toJSON(resList);
                resultVo.setResult(result);
                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：获取验证码
     *
     * @return
     * @author： news
     * @time: 2018/8/7 16:11
     * @param: * @param null
     */
    @RequestMapping(value = "getVcode")
    public void getVcode(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {


                String phone = WebParamUtils.getString("phone", request);

                logger.info("获取验证码手机号：" + phone);
                SysUser sysUser1 = new SysUser();
                sysUser1.setMobile(phone);
                long sysUser = sysUserService.getSysUserByMobile(sysUser1);
//
//                logger.info("获取到的用户：" + sysUser);

                if ( sysUser <= 0) {
                    logger.info("获取到的用户：" + sysUserService.getSysUserByMobile(sysUser1));
                    resultVo.setErrCode(-1);
                    return null;
                }

                String str = "";
                str = SMSMessageUtil.Vcode();
                logger.info("获取到的验证码：" + str);
//                SMSMessageUtil.initAndSend(phone, str);
                new QCloudSMSUtil().sendWithParam(phone, configurationService.getVcodeTemplateId(), str);
                System.out.println("获取到的验证码:" + str);

                resultVo.setErrCode(1);
                resultVo.setErrMsg(str);

                return null;
            }
        }, request, response, sessionId);

    }


    /**
     * 描述：小程序用户登录
     *
     * @return
     * @author： news
     * @time: 2018/8/7 15:01
     * @param: * @param null
     */
    @RequestMapping(value = "sysUserLogin")
    public void sysUserLodin(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String phone = WebParamUtils.getString("phone", request);
//                String password = WebParamUtils.getString("password", request);
                String openid = WebParamUtils.getString("openid", request);
                String session_id = WebParamUtils.getString("session_id", request);

                if (StringUtils.isBlank(phone) || StringUtils.isBlank(openid)) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                SysUser sysUser2 = new SysUser();
                sysUser2.setMobile(phone);
//                SysUser sysUser = sysUserService.getSysUserById(sysUser2);
                List<SysUser> sysUser = sysUserService.selectsysuser(new SysUser() {{
                    setMobile(phone);
                }});
                if (sysUser == null || sysUser.isEmpty()) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("您尚未成为系统用户，请联系管理员！");
                    return null;
                }

//                String pwd = sysUser.getPassword();
//                // 设置加密密码
//                String passWord = new Mademd5().toMd5(password);
//                if (!pwd.equals(passWord)) {
//                    resultVo.setErrCode(0);
//                    resultVo.setErrMsg("密码不正确");
//                    return null;
//                }


                SysUser sysUser1 = sysUser.get(0);
//                维护登录记录表
                SysUserWechat wechat = new SysUserWechat();
                wechat.setOpenId(openid);
                wechat.setUser(sysUser1);
                wechat.setIsNewRecord(true);

                int resp = userWechatDao.insertSysUser(wechat);
                if (resp > 0) {
                    HttpSession session;
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (StringUtils.isBlank(session_id)) {
                        session = request.getSession();
                        SessionUtil.getInstance().addSession(session);
                    } else {
                        session = SessionUtil.getInstance().getSession(session_id);
                    }
                    String sessionId2 = session.getId();

                    session.setAttribute(Constants.OPENID, openid);
                    map.put("sessionId", sessionId2);

                    session.setAttribute(Constants.USER_AUTH_KEY, sysUser1);
                    JSONObject j = new JsonUtils().sysUserToJson(sysUser1);
                    System.out.println(j.toString());
                    map.put("result", j);

                    resultVo.setErrCode(1);
                    resultVo.setResult(map);

                }


                return null;
            }
        }, request, response, sessionId);

    }


    /**
     * 描述：小程序自动登录
     *
     * @return
     * @author： news
     * @time: 2018/8/7 19:11
     * @param: * @param null
     */
    @RequestMapping(value = "sysUserAutoLogin")
    public void sysUserAutoLogin(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String openid = WebParamUtils.getString("openId", request);
//                String session_id = WebParamUtils.getString("sessionId", request);
                String session_id = sessionId;
                System.out.println(sessionId);

                if (StringUtils.isBlank(openid)) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("不能为空");
                    return null;
                }

                SysUserWechat sysUserWechat = userWechatDao.findOpenId(openid);
                if (sysUserWechat == null) {
                    //无登录记录
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("无登录记录");
                    return null;
                }

                SysUser sysUser2 = sysUserWechat.getUser();
                String userId = sysUser2.getId();
                List<SysUser> sysUserList = sysUserService.getSysUserbyId(userId);
                SysUser sysUser = sysUserList.get(0);
                if (sysUser == null) {
                    resultVo.setErrCode(0);
                    resultVo.setErrMsg("没有找到此用户");
                    return null;
                }

                HttpSession session;
                Map<String, Object> map = new HashMap<String, Object>();
                if (StringUtils.isBlank(session_id)) {
                    session = request.getSession();
                    SessionUtil.getInstance().addSession(session);
                } else {
                    session = SessionUtil.getInstance().getSession(session_id);
                }
                String sessionId2 = session.getId();

                session.setAttribute(Constants.OPENID, openid);
                session.setAttribute(Constants.USER_AUTH_KEY, sysUser);

                map.put("sessionId", sessionId2);
                JSONObject j = new JsonUtils().sysUserToJson(sysUser);
                System.out.println(j.toString());
                map.put("result", j);

                resultVo.setErrCode(1);
                resultVo.setResult(map);

/*
导出
 */
                if (wxDsrExporter == null) {
                    wxDsrExporter = new DataExporter(strExportPath, strPrefixWxDsr);
                }
                wxDsrExporter.export(sysUser);


                return null;
            }
        }, request, response, sessionId);

    }

    /**
     * 描述：获取openid
     *
     * @return
     * @author： news
     * @time: 2018/9/11 23:42
     * @param: * @param null
     */
    @RequestMapping(value = "/getOpenId")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response, String sessionId) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String code = WebParamUtils.getString("code", request);
                String AppId = WebParamUtils.getString("AppId", request);
                String AppSecret = WebParamUtils.getString("AppSecret", request);

                String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + AppId + "&secret=" + AppSecret + "&js_code=" + code + "&grant_type=authorization_code";
                HttpsClientUtil clientUtil = HttpsClientUtil.getInstance();
                JSONObject jsonObject = clientUtil.sendGetRequestJson(URL);
                System.out.println(jsonObject.toString());
                resultVo.setErrCode(1);
                resultVo.setResult(jsonObject.toString());

                return null;
            }
        }, request, response, sessionId);

    }

    public static void main(String[] args) {

    }


}