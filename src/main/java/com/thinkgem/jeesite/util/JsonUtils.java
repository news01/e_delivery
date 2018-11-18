package com.thinkgem.jeesite.util;

import java.util.ArrayList;
import java.util.List;

import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.entity.Remark;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtils {

	public JSONObject UserToJson(User c) {
		JSONObject json = new JSONObject();
		json.put("id", c.getId());
		json.put("username", c.getLoginName());
		json.put("office", c.getOffice().getName());
		json.put("phone_num", c.getMobile());
		json.put("name", c.getName());
		json.put("company", c.getCompany().getName());
		return json;
	}

	/**
	 * 描述：E网送达用户转json
	 * @author： news
	 * @time: 2018/8/10 15:21
	 * @param:  * @param null
	 * @return
	 */
	public JSONObject sysUserToJson(SysUser c) {
		JSONObject json = new JSONObject();
		json.put("id", c.getId());
		json.put("username", c.getLoginName());
		json.put("realname", c.getName());
		json.put("phone_num", c.getPhone());
		json.put("usertype", c.getUserType());
		json.put("community_id",c.getOffice().getId());
		json.put("community_name",c.getOffice().getName());
		return json;
	}

	public JSONObject CirculTOJson(ArchivesTrank c, User u, Archives a) {
		JSONObject json = new JSONObject();
		json.put("id", c.getId());
		json.put("userId", u.getId());
		json.put("fileId", a.getId());
		json.put("signTime", new TimeUtil().getDateTime(c.getSignTime()));
		if (!"0".equals(c.getTurnTime())) {
			json.put("turnTime", new TimeUtil().getDateTime(c.getTurnTime()));
		} else {
			json.put("turnTime", c.getTurnTime());
		}

		json.put("case_num", a.getCaseNum());
		json.put("case_case", a.getCaseCase());
		json.put("pla", a.getPla());
		json.put("defen", a.getDefen());
		json.put("book_num", a.getBookNum());
		json.put("all_books", a.getAllBooks());
		json.put("page", a.getPageNum());
		json.put("username", u.getLoginName());
		json.put("remarks", a.getRemarks());
		json.put("office", u.getOffice().getName());
		if (c.getAllPages() == null || c.getAllPages().equals("")) {
			json.put("remark", "0");
		} else {
			String remarks = c.getAllPages();
			/***
			 * 将获取的remark进行解读，remark的格式是：名称：数量；名称：数量
			 */
			String[] remark = remarks.split(",");
			/***
			 * remark得到的是所有材料的集合
			 *
			 * 将材料和数量用list收集
			 */
			List<Remark> list = new ArrayList<Remark>();
			/***
			 * 通过循环来收集材料清单
			 */
			System.out.println(remarks);
			System.out.println(remark.length);
			if (remark.length >= 1) {
				for (int i = 0; i < remark.length; i++) {
					String[] re = remark[i].split(":");
					System.out.println(re.length);
					if (re.length > 1) {
						Remark cl = new Remark();
						cl.setName(re[0]);
						cl.setNum(re[1]);
						list.add(cl);
						JSONArray cljson = JSONArray.fromObject(list);

						json.put("remark", cljson);
					}else{
						json.put("remark", "0");
					}
				}
			}else{
				json.put("remark", "0");
			}

			
		}

		json.put("phone_num", u.getMobile());
		json.put("name", u.getName());
		return json;

	}

	public JSONObject codeJson(String caseNum, String caseCase, String pla, String defen, String pageNum) {
		JSONObject json = new JSONObject();

		String id = new TimeUtil().getStringTime();
		json.put("id", id);
		json.put("case_num", caseNum);
		json.put("case_case", caseCase);
		json.put("pla", pla);
		json.put("defen", defen);

		json.put("page_num", pageNum);
		System.out.println(json.toString());

		return json;
	}

	public JSONObject TestFileToJson(Archives c) {
		JSONObject json = new JSONObject();
		json.put("id", c.getId());
		json.put("case_num", c.getCaseNum());
		json.put("case_case", c.getCaseCase());
		json.put("pla", c.getPla());
		json.put("defen", c.getDefen());
		json.put("book_num", c.getBookNum());
		json.put("all_books", c.getAllBooks());
		json.put("page_num", c.getPageNum());
		return json;

	}

	/*
	 * public JSONArray getJson2(JSONObject j, int i) { JSONArray ja = new
	 * JSONArray(); ja.put(i, j); return ja; }
	 */

	// {"book_num":"1","pla":"测试原告","case_case":"测试案由","page_num":"1","id":"201705230618186293915","case_num":"测试案号","defen":"测试被告","all_books":"1"}
	public Archives getFile(String res) {
		JSONObject json = JSONObject.fromObject(res);

		res = res.substring(1, res.length() - 1);
		System.out.println(res);
		String[] file = res.split(",");
		// "book_num":"1","pla":"测试原告","case_case":"测试案由","page_num":"1","id":"201705230618186293915","case_num":"测试案号","defen":"测试被告","all_books":"1","ajid":"123456789","stid":"12345"
		String book_num = json.getString("book_num"), pla = json.getString("pla"),
				case_case = json.getString("case_case"), page_num = json.getString("page_num"),
				id = json.getString("id"), case_num = json.getString("case_num"), defen = json.getString("defen"),
				all_books = json.getString("all_books"), stid = json.getString("stid"),
				remarks = "";
		/*
		 * for (int i = 0; i < file.length; i++) { String[] item =
		 * file[i].split(":"); item[0] = item[0].substring(1, item[0].length() -
		 * 1); item[1] = item[1].substring(1, item[1].length() - 1); if
		 * (item[0].equals("book_num")) { book_num = item[1]; } else if
		 * (item[0].equals("pla")) { pla = item[1]; } else if
		 * (item[0].equals("case_case")) { case_case = item[1]; } else if
		 * (item[0].equals("page_num")) { page_num = item[1]; } else if
		 * (item[0].equals("id")) { id = item[1]; } else if
		 * (item[0].equals("case_num")) { case_num = item[1]; } else if
		 * (item[0].equals("defen")) { defen = item[1]; } else if
		 * (item[0].equals("all_books")) { all_books = item[1]; } else if
		 * (item[0].equals("ajid")) { ajid = item[1]; } else if
		 * (item[0].equals("stid")) { stid = item[1]; }
		 * 
		 * }
		 */
		if (json.has("spcx")) {
			remarks = json.getString("spcx");
		}else{
			remarks = "10";
		}
		Archives ar = new Archives();
		ar.setAllBooks(all_books);
		ar.setBookNum(book_num);
		ar.setCaseCase(case_case);
		ar.setCaseNum(case_num);
		ar.setId(id);
		ar.setPla(pla);
		ar.setPageNum(page_num);
		ar.setDefen(defen);
		if (json.has("ajid")) {
			ar.setAjid(json.getString("ajid"));
		} else {
			ar.setAjid(id);
		}
		ar.setStid(stid);
		ar.setRemarks(remarks);
		return ar;
	}
}
