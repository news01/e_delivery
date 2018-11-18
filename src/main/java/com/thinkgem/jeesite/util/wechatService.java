package com.thinkgem.jeesite.util;

import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.web.context.ContextLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc77.utils.INewObjectObserver;
import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.service.ArchivesService;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.service.ArchivesTrankService;
import com.thinkgem.jeesite.modules.dayset.entity.MaxHaveDays;
import com.thinkgem.jeesite.modules.dayset.service.MaxHaveDaysService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUserWechat;
import com.thinkgem.jeesite.modules.userwechat.service.SysUserWechatService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class wechatService implements INewObjectObserver<DataUtil> {
	ObjectMapper objMapper = null;

	@Override
	public boolean onNewObjectArrived(DataUtil dtu) {
		// TODO Auto-generated method stub
		if ("deleteUser".equals(dtu.getOpration())) {
			return this.deleteUser(dtu);
		} else if ("saveUser".equals(dtu.getOpration())) {
			return this.saveUser(dtu);
		} else if ("updateUserInfo".equals(dtu.getOpration())) {
			return this.updateUserInfo(dtu);
		}else if("saveDays".equals(dtu.getOpration())){
			return this.saveDays(dtu);
		}else if("saveArchive".equals(dtu.getOpration())){
			return this.saveArchive(dtu);
		}else if("saveTrank".equals(dtu.getOpration())){
			return this.saveTrank(dtu);
		}else if("saveArchiveXla".equals(dtu.getOpration())){
			return this.saveArchiveXla(dtu);
		}else if("saveTrankXla".equals(dtu.getOpration())){
			return this.saveTrankXla(dtu);
		}
		return false;
	}
	private boolean saveTrankXla(DataUtil dtu){
		JSONArray jsonArray = JSONArray.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			String content = "";
			User user = new User();
			String signTime = "";
			for (int i = 0; i < jsonArray.size(); i++) {
				ArchivesTrank trank = objMapper.readValue(jsonArray.getString(i), ArchivesTrank.class);
				ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesTrankService.class).save(trank);
				Archives ar = ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesService.class).get(trank.getFileId());
				content += ar.getCaseNum();
				if(i == 0){
					user = ContextLoader.getCurrentWebApplicationContext().getBean(SystemService.class).getUser(trank.getUser().getId());
					signTime = trank.getSignTime();
				}
			}
			JSONObject dataJson = new JSONObject();
			SysUserWechat sysUserWechat = ContextLoader.getCurrentWebApplicationContext().getBean(SysUserWechatService.class).findByName(user.getName());
			dataJson.put("name", user.getName());
			dataJson.put("caseNum", content);
			dataJson.put("openId", sysUserWechat.getOpenId());
			dataJson.put("trankTime", signTime);
			String result = new WechatUtil().sendTemplate(dataJson);
//			SMSMessageUtil.initAndSendBack(content, user);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	private boolean saveTrank(DataUtil dtu) {
		
		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			User user = new User();
			ArchivesTrank ar = objMapper.readValue(json.toString(), ArchivesTrank.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesTrankService.class).save(ar);
			user = ContextLoader.getCurrentWebApplicationContext().getBean(SystemService.class).getUser(ar.getUser().getId());
			String caseNum = ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesService.class).get(ar.getFileId()).getCaseNum();
			JSONObject dataJson = new JSONObject();
			SysUserWechat sysUserWechat = ContextLoader.getCurrentWebApplicationContext().getBean(SysUserWechatService.class).findByName(user.getName());
			dataJson.put("name", user.getName());
			dataJson.put("caseNum", caseNum);
			dataJson.put("openId", sysUserWechat.getOpenId());
			dataJson.put("trankTime", ar.getSignTime());
			String result = new WechatUtil().sendTemplate(dataJson);
			System.out.println(result);
//			SMSMessageUtil.initAndSendBack(caseNum, user);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private boolean saveArchiveXla(DataUtil dtu){
		JSONArray jsonArray = JSONArray.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				Archives ar = objMapper.readValue(jsonArray.getString(i), Archives.class);
				ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesService.class).save(ar);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	private boolean saveArchive(DataUtil dtu) {

		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			Archives ar = objMapper.readValue(json.toString(), Archives.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(ArchivesService.class).save(ar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	private boolean deleteUser(DataUtil dtu) {

		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			User ar = objMapper.readValue(json.toString(), User.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(SystemService.class).deleteUser(ar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private boolean updateUserInfo(DataUtil dtu) {

		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			User ar = objMapper.readValue(json.toString(), User.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(SystemService.class).updateUserInfo(ar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private boolean saveUser(DataUtil dtu) {

		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			User ar = objMapper.readValue(json.toString(), User.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(SystemService.class).saveUser(ar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	private boolean saveDays(DataUtil dtu) {
		
		JSONObject json = JSONObject.fromObject(dtu.getObj());
		objMapper = new ObjectMapper();
		try {
			MaxHaveDays ar = objMapper.readValue(json.toString(), MaxHaveDays.class);
			ContextLoader.getCurrentWebApplicationContext().getBean(MaxHaveDaysService.class).save(ar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
}
