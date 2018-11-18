package com.thinkgem.jeesite.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.util.json.JSONObject;

import com.thinkgem.jeesite.modules.archivesuser.entity.ArchivesUser;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

public class OfficeUtil {
	// 获取唯一的法院id
	public String[] getOrgen(List<Office> l){
		String orgenIds = "";
		for (int i = 0; i < l.size(); i++) {
			String[] parent = (l.get(i).getParentIds().replace("，", ",")).split(",");
			if(parent.length>=3){
				if(i!=l.size()-1){
					orgenIds += parent[2]+",";
				}else{
					orgenIds += parent[2];
				}
			}
		}
		String[] s = orgenIds.split(",");
		List list = Arrays.asList(s);
		Set set = new HashSet(list);
		String[] r = (String[]) set.toArray(new String[0]);
		
		return r;
		
	}

	// 获取二级部门json数据
	public String getBranch(String[] s) {
		String str = "";
		for (int i = 0; i < s.length; i++) {
			String off = s[i].replace("，", ",");
			String[] a = off.split(",");
			if (i != s.length - 1) {
				str += a[1] + ",";
			} else {
				str += a[1];
			}
		}
		String[] fy = str.split(",");
		List list = Arrays.asList(fy);
		Set set = new HashSet(list);
		String[] r = (String[]) set.toArray(new String[0]);
		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < r.length; i++) {
			JSONObject json = new JSONObject();
			json.put("office", r[i]);
			String st = "";
			if (i != r.length - 1) {
				st = json.toString() + ",";
			} else {
				st = json.toString();
			}
			result.append(st);
		}
		result.append("]}");

		return result.toString();
	}

	public String getUsers(List<User> u,String officeName) {
		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < u.size(); i++) {
			JSONObject json = new JSONObject();
			json.put("id", u.get(i).getId());
			json.put("username", u.get(i).getLoginName());
			json.put("office", officeName);
			json.put("phone_num", u.get(i).getMobile());
			json.put("name", u.get(i).getName());
			String str = "";
			if (i != u.size() - 1) {
				str = json.toString() + ",";
			} else {
				str = json.toString();
			}
			result.append(str);
		}
		result.append("]}");
		return result.toString();
	}
}
