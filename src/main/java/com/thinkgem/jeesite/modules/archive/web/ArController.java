package com.thinkgem.jeesite.modules.archive.web;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.archive.dao.ArchivesDao;
import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.service.ArchivesService;
import com.thinkgem.jeesite.modules.archivetrank.dao.ArchivesTrankDao;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.entity.Remark;
import com.thinkgem.jeesite.modules.archivetrank.service.ArchivesTrankService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.util.JsonUtils;
import com.thinkgem.jeesite.util.SessionUtil;
import com.thinkgem.jeesite.util.StatusUtil;
import com.thinkgem.jeesite.util.TimeUtil;
import com.thinkgem.jeesite.util.fileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "archive")
public class ArController {

	@Value(value = "${inTaskPath}")
	private String inTaskPath;

	@Autowired
	private ArchivesService service;

	@Autowired
	private ArchivesTrankService trankservice;

	@Autowired
	private ArchivesTrankDao trankDao;

	@Autowired
	private ArchivesDao arDao;

	@RequestMapping(value = "add")
	@ResponseBody
	public String CreateNew(@RequestParam(value = "res", required = false) String res,
			@RequestParam(value = "session_id", required = false) String sessionId) throws ParseException {
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		JSONObject json = new JSONObject();

		Archives a = new JsonUtils().getFile(res);
		if (session == null) {
			json.put("result", "expired");
			return json.toString();
		} else {
			User user = (User) session.getAttribute("user");
			if (user == null) {
				json.put("result", "expired");
				return json.toString();
			} else {
				Archives ar = service.get(a.getId());
				if (ar != null) {
					json.put("result", "have");
					return json.toString();
				} else {

					List<Archives> old = arDao.findByCaseNum(a.getCaseNum());
					if (old.size() == 0) {
						a.setAllBooks("1");
					} else {
						String all = old.size() + 1 + "";
						a.setAllBooks(all);
						for (int i = 0; i < old.size(); i++) {
							old.get(i).setAllBooks(all);
							old.get(i).setIsNewRecord(false);
							String opration = "save";
							String ta = "ArchivesService";
							new fileUtil().expor(old.get(i), opration, ta, user);
							service.save(old.get(i));
						}
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String curTime = new TimeUtil().getCurrentTime();
					Date createDate = formatter.parse(new TimeUtil().getDateTime(curTime));
					Date updateTime = formatter.parse(new TimeUtil().getDateTime(curTime));
					String remarks = "1";
					String delFlag = "0";
					a.setUpdateBy(user);
					a.setUpdateDate(updateTime);
					a.setCreateBy(user);
					a.setCreateDate(createDate);
					a.setDelFlag(delFlag);
					if (a.getRemarks() == null) {
						if (user.getCompany().getId() == "J30") {
							remarks = "10";
						} else {
							remarks = "20";
						}
						a.setRemarks(remarks);
					}
					a.setIsNewRecord(true);
					String ta = "ArchivesService";
					String opration = "save";
					new fileUtil().expor(a, opration, ta, user);
					service.save(a);
					String time = new TimeUtil().getCurrentTime();
					ArchivesTrank trank = new ArchivesTrank();
					trank.setId(time);
					trank.setUser(user);
					trank.setFileId(a.getId());
					trank.setSignTime(time);
					trank.setCreateBy(user);
					trank.setAllPages(a.getPageNum());
					trank.setTurnTime("0");
					trank.setCreateDate(createDate);
					trank.setUpdateBy(user);
					trank.setUpdateDate(updateTime);
					trank.setDelFlag(delFlag);
					trank.setRemarks(remarks);
					trank.setIsNewRecord(true);
					String opration1 = "save";
					String ta2 = "ArchivesTrankService";
					new fileUtil().expor(trank, opration1, ta2, user);
					trankservice.save(trank);
					json.put("result", "ok");
					return json.toString();
				}
			}

		}

	}

	@RequestMapping(value = "mohu")
	@ResponseBody
	public String findMoHu(@RequestParam(value = "case_num", required = false) String caseNum) {
		List<Archives> ar = arDao.findMoHu(caseNum);
		String result = this.getArchivesJsonString(ar);
		return result;

	}

	@RequestMapping(value = "detail")
	@ResponseBody
	public String createOne(@RequestParam(value = "file_id", required = false) String file_id,
			@RequestParam(value = "session_id", required = false) String session_id) {
		System.out.println("session_id:     " + session_id);

		String time = new TimeUtil().getCurrentTime();
		HttpSession session = SessionUtil.getInstance().getSession(session_id);
		JSONObject json = new JSONObject();
		if (session == null) {
			json.put("result", "expired");
			return json.toString();
		} else {
			System.out.println("file_id:    " + file_id);
			Archives archives = service.get(file_id);
			if (archives == null) {
				json.put("result", "no");
				return json.toString();
			} else {
				ArchivesTrank circul = trankDao.findLastCircul(file_id);

				User user = (User) session.getAttribute("user");
				if (user == null) {
					json.put("result", "have");
					return json.toString();
				} else {
					if (circul != null) {
						System.out.println("id:    " + circul.getUser().getId());
						System.out.println(user.getId());
						if (circul.getUser().getId().equals(user.getId())) {
							json.put("result", "have");
							return json.toString();
						}
						circul.setTurnTime(time);
						circul.setIsNewRecord(false);
						String opration = "save";
						String ta = "ArchivesTrankService";
						new fileUtil().expor(circul, opration, ta, user);
						trankservice.save(circul);
						// trankDao.updateTurnTime(time, circul.getId());
					}
					Archives file = service.get(file_id);
					System.out.println("remark:" + file.getRemarks());
					if (file.getRemarks().equals("999")) {
						json.put("result", "finality");
						return json.toString();
					} else {
						ArchivesTrank trank = new ArchivesTrank();
						trank.setUser(user);
						trank.setId(time);
						trank.setFileId(file.getId());
						trank.setSignTime(time);
						trank.setTurnTime("0");
						trank.setCreateBy(user);
						trank.setAllPages(file.getPageNum());
						trank.setUpdateBy(user);
						trank.setCurrentUser(user);
						trank.setIsNewRecord(true);
						String opration = "save";
						String ta = "ArchivesTrankService";
						new fileUtil().expor(trank, opration, ta, user);
						trankservice.save(trank);
						json.put("result", "success");
						return json.toString();
					}
				}
			}
		}

	}

	@RequestMapping(value = "addRemark", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String addRemark(@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "cl", required = true) String remark,
			@RequestParam(value = "fileId", required = true) String fileId) {
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		JSONObject json = new JSONObject();
		if (session == null) {
			json.put("status", "999");
			return json.toString();
		} else {
			ArchivesTrank trank = trankDao.findLastCircul(fileId);
			User user = (User) session.getAttribute("user");
			if (user.getId() != trank.getUser().getId() && !user.getId().equals(trank.getUser().getId())) {
				json.put("status", "99");
				return json.toString();
			}else{
				trank.setAllPages(remark);
				trank.setIsNewRecord(false);
				String opration = "save";
				String ta = "ArchivesTrankService";
				new fileUtil().expor(trank, opration, ta, user);
				trankservice.save(trank);
				json.put("status", "1");
				return json.toString();
			}
		}

	}

	@RequestMapping(value = "findFile", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String findFile(@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "fileId", required = true) String fileId) {
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		JSONObject json = new JSONObject();
		if (session == null) {
			json.put("status", "999");
			return json.toString();
		} else {
			Archives archives = service.get(fileId);
			json.put("caseNum", archives.getCaseNum());
			ArchivesTrank trank = trankDao.findLastCircul(fileId);
			if (trank.getAllPages() == null) {
				System.out.println("---------");
				json.put("status", "0");
			} else if (trank.getAllPages().indexOf(":") == -1) {
				System.out.println("+++++++++++");
				json.put("status", "1");
			} else {
				System.out.println("——+——+——+——+——+——+——");
				if (trank.getAllPages().indexOf(":") == -1) {
					json.put("status", "0");
				}
				String[] remarks = trank.getAllPages().split(",");
				List<Remark> list = new ArrayList<Remark>();
				if (remarks.length>=1) {
					for (int i = 0; i < remarks.length; i++) {
						String[] re = remarks[i].split(":");
						if (re.length >= 1) {
							Remark cl = new Remark();
							cl.setName(re[0]);
							cl.setNum(re[1]);
							list.add(cl);
							
						}else{
							json.put("status", "0");
						}
					}
				}else{
					json.put("status", "0");
				}
				JSONArray cljson = JSONArray.fromObject(list);
				json.put("cl", cljson);
				json.put("status", "2");
				System.out.println(json.toString());
			}
			return json.toString();
		}

	}

	private String getArchivesJsonString(List<Archives> a) {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < a.size(); i++) {
			JSONObject json = new JsonUtils().TestFileToJson(a.get(i));
			String str = "";
			if (i != a.size() - 1) {
				str = json.toString() + ",";
			} else {
				str = json.toString();
			}
			result.append(str);
		}
		result.append("]}");
		return result.toString();
	}

	public String Files(@RequestParam(value = "caseId", required = false) String caseId,
			@RequestParam(value = "detail", required = false) String detail,
			@RequestParam(value = "sessionId", required = true) String sessionId) {
		System.out.println(caseId);
		System.out.println(detail);
		return null;
	}

}
