/*package com.thinkgem.jeesite.modules.archivesuser.web;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.archivesuser.dao.ArchivesUserDao;
import com.thinkgem.jeesite.modules.archivesuser.entity.ArchivesUser;
import com.thinkgem.jeesite.util.JsonUtil;
import com.thinkgem.jeesite.util.OfficeUtil;
import com.thinkgem.jeesite.util.SMSMessageUtil;
import com.thinkgem.jeesite.util.SessionUtil;
import com.thinkgem.jeesite.util.StringUtil;
import com.thinkgem.jeesite.util.get3rdSessionIdUtil;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

@Controller
public class ArUserController {
	Logger logger = Logger.getLogger(ArUserController.class);
	@Autowired
	private ArchivesUserDao userDao;

	@RequestMapping(value = "login")
	@ResponseBody
	public String Login(HttpServletRequest request, @RequestParam(value = "open_id", required = false) String openId,
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
		session.setAttribute("open_id", openId);
		System.out.println(sessionId);
		json.put("sessionId", sessionId);
		ArchivesUser user = userDao.findByOpenId(openId);
		if (user == null) {
			json.put("result", "null");
		} else {
			session.setAttribute("user", user);
			JSONObject j = new JsonUtil().UserToJson(user);
			json.put("result", j);
			System.out.println(json.toString());
		}

		return json.toString();
		
		
	}

	@RequestMapping(value = "chet")
	@ResponseBody
	public String FindByUserName(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "userName", required = false) String userName) {
		System.out.println("1" + session_id);
		logger.debug("sessionId:   "+session_id);
		logger.debug("userName:   "+userName);
		JSONObject json = new JSONObject();
		HttpSession session = SessionUtil.getInstance().getSession(session_id);
		if (session==null) {
			json.put("result", "expired");
			return json.toString();
		}
		ArchivesUser User = userDao.findByUserName(userName);
		
		if (User == null) {
			System.out.println("没有");
			json.put("result", "null");
		} else {
			String str = SMSMessageUtil.Vcode();
			System.out.println(str);
			System.out.println(User.getName());
			session.setAttribute("username", userName);
			session.setAttribute("vcode", str);
			SMSMessageUtil.initAndSend(User.getPhoneNum(), str);
			System.out.println(User.getPhoneNum());
			json.put("result", "ok");
		}
		return json.toString();
	}

	@RequestMapping(value = "insert")
	@ResponseBody
	public String insert(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "vcode", required = false) String vcode) {
		System.out.println("2:"+session_id);
		JSONObject json = new JSONObject();
		HttpSession session = SessionUtil.getInstance().getSession(session_id);
		if (session==null) {
			json.put("result", "expired");
			return json.toString();
		}
		Object str = session.getAttribute("vcode");
		String openId = (String) session.getAttribute("open_id");
		String userName = (String) session.getAttribute("username");
		if (str.equals(vcode)||str==vcode) {
			userDao.updateOpenId(openId, userName);
			json.put("result", "ok");
		}else{
			json.put("result", "null");
		}
		System.out.println(session.getId());
		System.out.println(openId+"   "+vcode);
		System.out.println(str);
		return json.toString();
	}
	@RequestMapping(value="getopenid")
	@ResponseBody
	public String GetOpenId(@RequestParam(value = "code", required = false) String code){
		String openId = (String) new get3rdSessionIdUtil().get3rdSession(code);
		System.out.println(openId);
		JSONObject json = new JSONObject();
		json.put("result", openId);
		return json.toString();
		
	}
	
	@RequestMapping(value = "office")
	@ResponseBody
	public String findOffice(){
		String result = new OfficeUtil().getFy(userDao.findOffice());
		return result;
	}
	@RequestMapping(value = "fy")
	@ResponseBody
	public String findByFy(@RequestParam(value="office",required = false ) String office){
		String result = new OfficeUtil().getBranch(userDao.findByFy(office));
		return result;
	}
	
	@RequestMapping(value = "users")
	@ResponseBody
	public String findByFullOffice(@RequestParam(value="office",required = false) String office,
			@RequestParam(value="fy",required = false) String fy
			){
		String fullOffice1 = fy+","+office;
		String fullOffice2 = fy+"，"+office;
		List<ArchivesUser> user = userDao.findByFullOffice(fullOffice1,fullOffice2);
		String result = new OfficeUtil().getUsers(user);
		return result;
	}
}
*/