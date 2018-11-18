package com.thinkgem.jeesite.modules.userwechat.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.thinkgem.jeesite.common.utils.e.delivery.*;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.util.*;
import com.thinkgem.jeesite.util.SessionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.userwechat.dao.SysUserWechatDao;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserWechatService;

import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WechatController {
    Logger logger = Logger.getLogger(WechatController.class);

    @Autowired
    private SysUserWechatDao dao;

    @Autowired
    private SysUserWechatService wechatService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private SysUserWechatDao sysUserWechatDao;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private UserDao userDao;

    @ModelAttribute("office")
    public Office get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return officeService.get(id);
        } else {
            return new Office();
        }
    }


    /**
     * @param
     * @return
     * @DESC: 登录
     * @author: niushi
     * @date 2018年7月31日 下午4:48:58
     */
    @RequestMapping(value = "login")
    public void login(HttpServletRequest request, HttpServletResponse response, String session_id) {

        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {
                String open_id = WebParamUtils.getString("open_id", request);
                HttpSession session;
                Map<String, Object> map = new HashMap<String, Object>();
                if (StringUtils.isBlank(session_id)) {
                    session = request.getSession();
                    SessionUtil.getInstance().addSession(session);
                } else {
                    session = SessionUtil.getInstance().getSession(session_id);
                }

                String sessionId2 = session.getId();
                session.setAttribute("open_id", open_id);
                System.out.println(sessionId2);
                map.put("sessionId", sessionId2);
                SysUserWechat wechat = dao.findOpenId(open_id);
                if (wechat == null) {
                    map.put("resultCode", 0);
                } else {
                    User user2 = systemService.getUser(wechat.getUser().getId());
                    session.setAttribute(Constants.USER_AUTH_KEY, user2);
                    JSONObject j = new JsonUtils().sysUserToJson(user);
                    System.out.println(j.toString());
                    map.put("result", j);

                }
                resultVo.setResult(map);
                return null;
            }
        }, request, response, session_id);
    }


    //    @RequestMapping(value = "login")
//    @ResponseBody
    public String Login2(HttpServletRequest request, @RequestParam(value = "open_id", required = false) String open_id,
                         @RequestParam(value = "session_id", required = false) String session_id) {
        HttpSession session;
        JSONObject json = new JSONObject();
        if (session_id == null || session_id.equals("")) {
            session = request.getSession();
            System.out.println(session.getId());
            SessionUtil.getInstance().addSession(session);
        } else {
            session = SessionUtil.getInstance().getSession(session_id);
        }
        String sessionId = session.getId();
        session.setAttribute("open_id", open_id);
        System.out.println(sessionId);
        json.put("sessionId", sessionId);
        SysUserWechat wechat = dao.findOpenId(open_id);
        if (wechat == null) {
            json.put("result", 0);
        } else {
            User user = systemService.getUser(wechat.getUser().getId());
            // User user = systemService.getUser("3");
            session.setAttribute("user", user);
            JSONObject j = new JsonUtils().UserToJson(user);
            System.out.println(j.toString());
            json.put("result", j);
            System.out.println(json.toString());
        }

        return json.toString();

    }

    @RequestMapping(value = "loginout")
    @ResponseBody
    public String LoginOut(@RequestParam(value = "openid", required = false) String openid) {
        System.out.println(openid);
        SysUserWechat wechat = sysUserWechatDao.findOpenId(openid);
        JSONObject json = new JSONObject();
        if (wechat == null) {
            json.put("result", "nohave");
        } else {
            String opration = "deletByOpenId";
            String ta = "SysUserWechatDao";
            User user = systemService.getUser(wechat.getUser().getId());
            new fileUtil().expor(openid, opration, ta, user);
            sysUserWechatDao.deletByOpenId(openid);
            json.put("result", "success");
        }
        return json.toString();
    }

    @RequestMapping(value = "chet")
    @ResponseBody
    public String FindByUserName(@RequestParam(value = "session_id", required = false) String session_id,
                                 @RequestParam(value = "userName", required = false) String userName) {
        System.out.println("1" + session_id);
        logger.debug("sessionId:   " + session_id);
        logger.debug("userName:   " + userName);
        JSONObject json = new JSONObject();

        HttpSession session = SessionUtil.getInstance().getSession(session_id);
        if (session == null) {
            json.put("result", "expired");
            return json.toString();
        }
        User User = systemService.getUserByLoginName(userName);

        if (User == null) {
            System.out.println("没有");
            json.put("result", 0);
        } else {
            String str = "";
            if (userName == "wechat" || userName.equals("wechat")) {
                str = "1234";
            } else {
                str = SMSMessageUtil.Vcode();
                SMSMessageUtil.initAndSend(User.getMobile(), str);
            }
            System.out.println(str);
            System.out.println(User.getName());
            session.setAttribute("username", userName);
            session.setAttribute("user", User);
            session.setAttribute("vcode", str);
            System.out.println(User.getMobile());
            json.put("result", "ok");
        }
        return json.toString();
    }

    @RequestMapping(value = "insert")
    @ResponseBody
    public String insert(@RequestParam(value = "session_id", required = false) String session_id,
                         @RequestParam(value = "vcode", required = false) String vcode) {
        System.out.println("2:" + session_id);
        JSONObject json = new JSONObject();
        HttpSession session = SessionUtil.getInstance().getSession(session_id);
        if (session == null) {
            json.put("result", "expired");
            return json.toString();
        }
        Object str = session.getAttribute("vcode");
        String openId = (String) session.getAttribute("open_id");
        if (str.equals(vcode) || str == vcode) {
            SysUserWechat wechat = new SysUserWechat();
            User user = (User) session.getAttribute("user");
            wechat.setOpenId(openId);
//            wechat.setUser(user);
            wechat.setIsNewRecord(true);
            String opration = "save";
            String ta = "SysUserWechatService";
            new fileUtil().expor(wechat, opration, ta, user);
            wechatService.save(wechat);
            sysUserWechatDao.insert(wechat);
            json.put("result", "ok");
        } else {
            json.put("result", 0);
        }
        System.out.println(session.getId());
        System.out.println(openId + "   " + vcode);
        System.out.println(str);
        return json.toString();
    }

    @RequestMapping(value = "organ")
    @ResponseBody
    public String getAllOrgan() {
        List<Office> list = officeService.findList(true);
        StringBuffer result = new StringBuffer("{\"result\":[");
        String[] officeIds = new OfficeUtil().getOrgen(list);
        for (int i = 0; i < officeIds.length; i++) {
            Office office = officeService.get(officeIds[i]);
            JSONObject json = new JSONObject();
            json.put("fy", office.getName());
            json.put("id", officeIds[i]);
            String st = "";
            if (i != officeIds.length - 1) {
                st += json.toString() + ",";
            } else {
                st += json.toString();
            }
            result.append(st);
        }
        result.append("]}");
        return result.toString();

    }

    @RequestMapping(value = "fy")
    @ResponseBody
    public String findByFy(@RequestParam(value = "office", required = false) String id) {
        List<Office> list = officeDao.findOfficeMohu(id);
        StringBuffer result = new StringBuffer("{\"result\":[");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() != id && !list.get(i).getId().equals(id)) {
                JSONObject json = new JSONObject();
                json.put("office", list.get(i).getName());
                json.put("id", list.get(i).getId());
                String st = "";
                if (i != list.size() - 1) {
                    st += json.toString() + ",";
                } else {
                    st += json.toString();
                }
                result.append(st);
            }
        }
        result.append("]}");
        System.out.println(result);
        return result.toString();
    }

    @RequestMapping(value = "users")
    @ResponseBody
    public String findByFullOffice(@RequestParam(value = "office", required = false) String office,
                                   @RequestParam(value = "officeid", required = false) String officeid) {
        System.out.println(office);
        System.out.println(officeid);
        List<User> user = systemService.findUserByOfficeId(officeid);

        String result = new OfficeUtil().getUsers(user, office);
        System.out.println(result);
        return result;
    }





    /*
     * @RequestMapping(value = "userscript") public String userScript(){ String
     * path = "C:/Users/zmj/Desktop/OA用户.xls"; List<ReadExcelUtil> list = new
     * ReadExcelUtil().getData(path); System.out.println(list.size());
     * List<String> names = new ArrayList<String>();
     *
     * for (int i = 0; i < list.size(); i++) { System.out.println("realName:  "
     * + list.get(i).getName()); List<User> us=
     * userDao.getByRealName(list.get(i).getName()); if (us.size() > 1) {
     * names.add(list.get(i).getName()); } //
     * user.setOffice(officeService.get(list.get(i).getOffice_id())); } //
     * System.out.println(a); for (int i = 0; i < names.size(); i++) {
     * System.out.println(names.get(i)); } return "ok";
     *
     * }
     */
    public static void main(String[] args) {

    }
}
