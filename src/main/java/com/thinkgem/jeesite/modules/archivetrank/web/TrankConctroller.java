package com.thinkgem.jeesite.modules.archivetrank.web;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.archive.entity.Archives;
import com.thinkgem.jeesite.modules.archive.service.ArchivesService;
import com.thinkgem.jeesite.modules.archivetrank.dao.ArchivesTrankDao;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;
import com.thinkgem.jeesite.modules.archivetrank.entity.Remark;
import com.thinkgem.jeesite.modules.archivetrank.entity.tongji;
import com.thinkgem.jeesite.modules.archivetrank.service.ArchivesTrankService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.util.JsonUtils;
import com.thinkgem.jeesite.util.SMSMessageUtil;
import com.thinkgem.jeesite.util.SessionUtil;
import com.thinkgem.jeesite.util.StatusUtil;
import com.thinkgem.jeesite.util.StringUtil;
import com.thinkgem.jeesite.util.TimeUtil;
import com.thinkgem.jeesite.util.fileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "trank")
public class TrankConctroller {

	@Autowired
	private SystemService userservice;

	@Autowired
	private ArchivesService archivesService;

	@Autowired
	private ArchivesTrankDao trankDao;

	@Autowired
	private ArchivesTrankService service;

	@RequestMapping(value = "save")
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
			Archives archives = archivesService.get(file_id);
			if (archives == null) {
				json.put("result", "no");
				return json.toString();
			} else {
				ArchivesTrank circul = trankDao.findLastCircul(file_id);

				User u = (User) session.getAttribute("user");
				if(u.getId().isEmpty()){
					json.put("result", "expired");
					return json.toString();
				}else{
					User user = userservice.getUser(u.getId());
					if (user == null) {
						json.put("result", "expired");
						return json.toString();
					} else {
						String status = new StatusUtil().GetSpcxZt(user.getCompany().getId(), archives.getRemarks());
						if (status == StringUtil.FIRST_INSTANCE2SECOND_INSTANCE) {
							json.put("result", "FIRST_INSTANCE2SECOND_INSTANCE");
							return json.toString();
						} else if (status == StringUtil.SECOND_INSTANCE2BACK) {
							json.put("result", "SECOND_INSTANCE2BACK");
							return json.toString();
						} else if (status == StringUtil.SHUTDOWN) {
							json.put("result", "finality");
							return json.toString();
						} else {
							if (circul != null) {
								System.out.println("id:    " + circul.getUser().getId());
								System.out.println(user.getId());
								if (circul.getUser().getId().equals(user.getId())) {
									json.put("result", "have");
									return json.toString();
								}

								// String RemarkRel = new
								// StatusUtil().getRemark(status);

								circul.setTurnTime(time);
								circul.setIsNewRecord(false);
								String opration = "save";
								String ta = "ArchivesTrankService";
								new fileUtil().expor(circul, opration, ta, user);
								service.save(circul);
								// trankDao.updateTurnTime(time, circul.getId());
							}
							Archives file = archivesService.get(file_id);
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
								trank.setCreateDate(new Date());
								trank.setUpdateBy(user);
								trank.setUpdateDate(new Date());
								trank.setCurrentUser(user);
								trank.setIsNewRecord(true);
								String opration = "save";
								String ta = "ArchivesTrankService";
								new fileUtil().expor(trank, opration, ta, user);
								service.save(trank);
								json.put("result", "success");
								return json.toString();
							}
						}
					}
				}
				}
				
		}

	}

	@RequestMapping(value = "confirm", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String Confirm(@RequestParam(value = "session_id", required = true) String session_id,
			@RequestParam(value = "file_id", required = true) String file_id) {
		HttpSession session = SessionUtil.getInstance().getSession(session_id);
		JSONObject json = new JSONObject();
		String time = new TimeUtil().getCurrentTime();
		if (session == null) {
			json.put("result", "expire");
			return json.toString();
		} else {
			Archives archives = archivesService.get(file_id);
			if (archives == null) {
				json.put("result", "no");
				return json.toString();
			} else {
				User u = (User) session.getAttribute("user");
				if(u.getId().isEmpty()){
					json.put("result", "expired");
					return json.toString();
				}
				User user = userservice.getUser(u.getId());
				String status = new StatusUtil().GetSpcxZt(user.getCompany().getId(), archives.getRemarks());
				String remarkRel = new StatusUtil().getRemark(status);
				ArchivesTrank circul = trankDao.findLastCircul(file_id);
				if (circul != null) {
					if (circul.getUser().getId().equals(user.getId())) {
						json.put("result", "have");
						return json.toString();
					} else {
						circul.setTurnTime(time);
						circul.setIsNewRecord(false);
						String opration = "save";
						String ta = "ArchivesTrankService";
						new fileUtil().expor(circul, opration, ta, user);
						service.save(circul);
					}

				}
				ArchivesTrank trank = new ArchivesTrank();
				trank.setUser(user);
				trank.setId(time);
				trank.setFileId(file_id);
				trank.setSignTime(time);
				trank.setTurnTime("0");
				trank.setCreateBy(user);
				trank.setCreateDate(new Date());
				trank.setUpdateBy(user);
				trank.setUpdateDate(new Date());
				trank.setCurrentUser(user);
				trank.setIsNewRecord(true);
				String opration = "save";
				String ta = "ArchivesTrankService";
				new fileUtil().expor(trank, opration, ta, user);
				service.save(trank);
				archives.setRemarks(remarkRel);
				archives.setIsNewRecord(false);
				String opt = "save";
				String ta2 = "ArchivesService";
				new fileUtil().expor(archives, opt, ta2, user);
				archivesService.save(archives);
				json.put("result", "success");
				return json.toString();
			}
		}
	}

	@RequestMapping(value = "myfile")
	@ResponseBody
	public String getMyFile(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "index", required = false) int index) {
		HttpSession session = SessionUtil.getInstance().getSession(session_id);

		if (session == null) {
			JSONObject json = new JSONObject();
			json.put("result", "expired");
			return json.toString();
		}
		int start = index;
		int end = 10;
		System.out.println(year + ":" + month + ":" + date);
		String upTime = new TimeUtil().getUpSeconds(year, month, date);
		String lowTime = new TimeUtil().getLowSeconds(year, month, date);
		String id = ((User) session.getAttribute("user")).getId();

		List<ArchivesTrank> c = trankDao.findMyFile(id, upTime, lowTime, start, end);

		String result = this.getTrankJsonString(c);
		return result;
	}

	@RequestMapping(value = "script")
	@ResponseBody
	public String script() {
		List<ArchivesTrank> c = trankDao.findByFileId("1498038544515", 0, 100);
		System.out.println(c.get(0).getUser().getId());
		return "ok";
	}

	@RequestMapping(value = "nowhave")
	@ResponseBody
	public String getNowHave(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "index", required = false) int index) {
		HttpSession session = SessionUtil.getInstance().getSession(session_id);

		if (session == null) {
			JSONObject json = new JSONObject();
			json.put("result", "expired");
			return json.toString();
		}
		int start = index;
		int end = 10;
		String id = ((User) session.getAttribute("user")).getId();
		
		List<ArchivesTrank> c = trankDao.findNowHave(id, start, end);

		String result = this.getTrankJsonString(c);
		return result;
	}

	@RequestMapping(value = "nowhave2")
	@ResponseBody
	public String getNowHave2(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "index", required = false) int index) {
		int start = index;
		int end = 10;
		List<ArchivesTrank> c = trankDao.findNowHave(id, start, end);
		String result = this.getTrankJsonString(c);
//		System.out.println(result);
		return result;
	}

	@RequestMapping(value = "myturn")
	@ResponseBody
	public String getMyTurn(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "index", required = false) int index) {
		HttpSession session = SessionUtil.getInstance().getSession(session_id);

		if (session == null) {
			JSONObject json = new JSONObject();
			json.put("result", "expired");
			return json.toString();
		}
		int start = index;
		int end = 10;
		System.out.println(year + ":" + month + ":" + date);
		String upTime = new TimeUtil().getUpSeconds(year, month, date);
		String lowTime = new TimeUtil().getLowSeconds(year, month, date);
		String id = ((User) session.getAttribute("user")).getId();

		List<ArchivesTrank> c = trankDao.findMyTurn(id, upTime, lowTime, start, end);

		String result = this.getTrankJsonString2(c);
		return result;
	}

	@RequestMapping(value = "myturn2")
	@ResponseBody
	public String getMyTurn2(@RequestParam(value = "session_id", required = false) String session_id,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "date", required = false) String date) {
		HttpSession session = SessionUtil.getInstance().getSession(session_id);

		if (session == null) {
			JSONObject json = new JSONObject();
			json.put("result", "expired");
			return json.toString();
		}
		System.out.println(year + ":" + month + ":" + date);
		String upTime = new TimeUtil().getUpSeconds(year, month, date);
		String lowTime = new TimeUtil().getLowSeconds(year, month, date);
		String id = ((User) session.getAttribute("user")).getId();
		List<ArchivesTrank> c = trankDao.findMyTurn2(id, upTime, lowTime);
		List<ArchivesTrank> t = this.getTrankJsonString4(c);
		List<tongji> tj = new ArrayList<tongji>();
		Map<String, Object> mp = new HashMap<String, Object>();

		for (int i = 0; i < t.size(); i++) {
			String qsr = t.get(i).getQsr();
			if (!mp.containsKey(qsr)) {
				mp.put(qsr, 1);
			} else {
				mp.put(qsr, (Integer.parseInt( (String)mp.get(qsr))) + 1);
			}
		}
		Set<String> keys = mp.keySet();
		String userName = userservice.getUser(id).getName();
		for (String key : keys) {
			tongji tongji = new tongji();
			tongji.setUserName(userName);
			tongji.setSqrName(userservice.getUser((String) key).getName());
			tongji.setNumber(Integer.parseInt( (String)mp.get(key)));
			tj.add(tongji);
		}
		System.out.println(mp.toString());

		JSONArray j = JSONArray.fromObject(tj);
		JSONObject json = new JSONObject();
		json.put("result", j);
		return json.toString();
	}

	@RequestMapping(value = "onecase")
	@ResponseBody
	public String getOneCase(@RequestParam(value = "file_id", required = false) String id,
			@RequestParam(value = "index", required = false) int index) {
		System.out.println(id);
		int start = index;
		int end = 10;
		List<ArchivesTrank> c = trankDao.findByFileId(id, start, end);
		String result = this.getTrankJsonString3(c);
		return result.toString();
	}

	@RequestMapping(value = "jzzj")
	@ResponseBody
	public String zjjz(@RequestParam(value = "ids", required = false) String[] ids) {
		System.out.println(ids);
		for (int i = 0; i < ids.length; i++) {
			System.out.println(ids[i]);
			Archives arc = archivesService.get(ids[i]);
			arc.setRemarks("999");
			arc.setIsNewRecord(false);
			String opration = "save";
			String ta = "ArchivesService";
			ArchivesTrank at = trankDao.findLastCircul(arc.getId());
			User user = userservice.getUser(at.getUser().getId());
			new fileUtil().expor(arc, opration, ta, user);
			archivesService.save(arc);
		}
		JSONObject json = new JSONObject();
		json.put("result", "success");
		return json.toString();

	}

	@RequestMapping(value = "txqs")
	@ResponseBody
	public String txqs(@RequestParam(value = "ids", required = false) String[] ids,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "sessionId", required = false) String sessionId) {
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		if (session == null) {
			JSONObject json = new JSONObject();
			json.put("result", "expired");
			return json.toString();
		}
		User fsr = (User) session.getAttribute("user");
		User jsr = (User) userservice.getUser(userId);
		Office office = jsr.getOffice();
		String caseNums = "";
		for (int i = 0; i < ids.length; i++) {
			Archives ar = archivesService.get(ids[i]);
			int remark = Integer.parseInt(ar.getRemarks());
			System.out.println(remark);
			if (remark != 999) {
				System.out.println(ar.getRemarks());
				caseNums += ar.getCaseNum() + ",";
				Date updateTime = new Date();
				ArchivesTrank at = trankDao.findLastCircul(ids[i]);
				at.setRemarks("2");
				at.setUpdateTime(updateTime);
				at.setIsNewRecord(false);
				String opration = "save";
				String ta = "ArchivesTrankService";
				new fileUtil().expor(at, opration, ta, fsr);
				service.save(at);
			}
		}
		caseNums = caseNums.substring(0, caseNums.length() - 1);
		SMSMessageUtil.initAndSend2(jsr.getMobile(), caseNums, office.getName(), fsr.getName(), jsr.getName());
		JSONObject json = new JSONObject();
		json.put("result", "success");
		return json.toString();

	}

	private String getTrankJsonString(List<ArchivesTrank> c) {
		System.out.println(c.size());

		StringBuffer result = new StringBuffer("{\"result\":[");

		for (int i = 0; i < c.size(); i++) {

			// int remark = Integer.parseInt(ar.getRemarks());
			// if (remark != 999) {
			User u = userservice.getUser(c.get(i).getUser().getId());
			System.out.println(u.toString());

			Archives a = archivesService.get(c.get(i).getFileId());
			System.out.println(a.toString());

			JSONObject json = new JsonUtils().CirculTOJson(c.get(i), u, a);
			System.out.println(json.toString());

			String str = "";
			if (i != c.size() - 1) {
				str = json.toString() + ",";
			} else {
				str = json.toString();
			}
			result.append(str);
			// }
		}
		boolean remark = false;
		if (c.size() < 10) {
			remark = true;
		} else {
			remark = false;
		}
		result.append("],\"remark\":" + remark + "}");
		System.out.println(result);
		return result.toString();
	}

	private String getTrankJsonString3(List<ArchivesTrank> c) {
		System.out.println(c.size());

		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < c.size(); i++) {
			// int remark = Integer.parseInt(ar.getRemarks());
			User u = userservice.getUser(c.get(i).getUser().getId());
			Archives a = archivesService.get(c.get(i).getFileId());
			JSONObject json = new JsonUtils().CirculTOJson(c.get(i), u, a);
			String str = "";
			if (i != c.size() - 1) {
				str = json.toString() + ",";
			} else {
				str = json.toString();
			}
			result.append(str);
		}
		boolean remark = false;
		if (c.size() < 10) {
			remark = true;
		} else {
			remark = false;
		}
		result.append("],\"remark\":" + remark + "}");
		System.out.println(result);
		return result.toString();
	}

	private String getTrankJsonString2(List<ArchivesTrank> c) {
		System.out.println(c.size());

		StringBuffer result = new StringBuffer("{\"result\":[");
		for (int i = 0; i < c.size(); i++) {
			// int remark = Integer.parseInt(ar.getRemarks());
			// if (remark != 999) {
			User u = userservice.getUser(c.get(i).getUser().getId());
			Archives a = archivesService.get(c.get(i).getFileId());
			// ArchivesTrank t = trankDao.findTurnTo(c.get(i).getFileId(),
			// c.get(i).getTurnTime());
			ArchivesTrank t = trankDao.findLastCircul(c.get(i).getFileId());
			JSONObject json = new JsonUtils().CirculTOJson(c.get(i), u, a);
			User u2 = userservice.getUser(t.getUser().getId());
			System.out.println("company:          " + u2.getCompany().getName());
			json.put("company", u2.getCompany().getId());
			String str = "";
			if (i != c.size() - 1) {
				str = json.toString() + ",";
			} else {
				str = json.toString();
			}
			result.append(str);
			// }
		}
		boolean remark = false;
		if (c.size() < 10) {
			remark = true;
		} else {
			remark = false;
		}
		result.append("],\"remark\":" + remark + "}");
		System.out.println(result);
		return result.toString();
	}

	private List<ArchivesTrank> getTrankJsonString4(List<ArchivesTrank> c) {
		System.out.println(c.size());
		for (int i = 0; i < c.size(); i++) {
			ArchivesTrank t = trankDao.findTurnTo(c.get(i).getFileId(), c.get(i).getTurnTime());
			User u2 = userservice.getUser(t.getUser().getId());
			System.out.println("company:          " + u2.getCompany().getName());
			c.get(i).setQsr(u2.getId());
			// }
		}
		return c;
	}

	/***
	 * 刑事案件扫描条形码
	 * 
	 * @param sessionId
	 * @param caseId
	 * 
	 */
	@RequestMapping(value = "xsbarcode", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String xsScanBarCode(@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "caseId", required = true) String caseId) {
		JSONObject json = new JSONObject();
		/***
		 * 判断sessionId存不存在
		 */
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		/***
		 * 如果不存在，则返回0
		 */
		if (session == null) {
			json.put("status", "0");
			return json.toString();
		} else {
			/***
			 * 通过caseId获取案件
			 */
			Archives archives = archivesService.get(caseId);
			/***
			 * 如果卷宗不存在，说明是新增案件，返回1
			 */
			if (archives == null) {
				json.put("status", "1");
				return json.toString();
			} else {
				/***
				 * 卷宗存在，说明不是新增案件，将案件信息转化为json
				 */
				JSONObject arjson = JSONObject.fromObject(archives);
				/***
				 * 获取最后一次签收(即上次签收)的备注
				 */
				ArchivesTrank lastTrank = trankDao.findLastCircul(caseId);

				String remarks = lastTrank.getAllPages();
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
				for (int i = 0; i < remark.length; i++) {
					String[] re = remark[i].split(":");
					Remark cl = new Remark();
					cl.setName(re[0]);
					cl.setNum(re[1]);
					list.add(cl);
				}
				JSONObject cljson = JSONObject.fromObject(list);
				json.put("status", "2");
				json.put("archive", arjson);
				json.put("cl", cljson);
				return json.toString();
			}
		}
	}

	/***
	 * 刑事案件扫描二维码
	 * 
	 * @param sessionId
	 * @param archive
	 * @return
	 */
	@RequestMapping(value = "xsqrcode", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String sxScanQRCode(@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "archive", required = true) String archive) {
		JSONObject json = new JSONObject();
		/***
		 * 判断sessionId存不存在
		 */
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		/***
		 * 如果不存在，则返回0
		 */
		if (session == null) {
			json.put("status", "0");
			return json.toString();
		} else {
			/***
			 * 通过session获取用户
			 */
			User user = (User) session.getAttribute("user");
			/***
			 * 将传过来的archive字符串反序列化
			 */
			Archives a = new JsonUtils().getFile(archive);
			/***
			 * 通过卷宗ID判断该卷宗是否是新增卷宗
			 */
			Archives archives = archivesService.get(a.getId());
			/***
			 * 如果卷宗不存在，则增加
			 */
			if (archives == null) {
				json.put("status", "3");
				a.setIsNewRecord(true);
				a.setCreateBy(user);
				a.setCreateDate(new Date());
				a.setUpdateBy(user);
				a.setUpdateTime(new Date());
				session.setAttribute("archive", a);
				return json.toString();
			} else {
				/***
				 * 卷宗存在，返回最后一次签收的备注信息
				 */
				ArchivesTrank lastTrank = trankDao.findLastCircul(a.getId());
				/***
				 * 如果这条记录中的user是当前用户，说明该案件就在当前用户手中，不必再次签收
				 */

				if (lastTrank.getUser().getId() == user.getId() || lastTrank.getUser().getId().equals(user.getId())) {
					json.put("status", "6");
					return json.toString();

				}

				String remarks = lastTrank.getAllPages();
				logger.info("材料清单========>" + remarks);
				/***
				 * 将获取的remark进行解读，remark的格式是：名称：数量；名称：数量
				 */
				List<Remark> list = new ArrayList<Remark>();
				if (remarks.indexOf(":") == -1) {
					json.put("remark", "0");
				} else {
					String[] remark = remarks.split(",");
					/***
					 * remark得到的是所有材料的集合
					 *
					 * 将材料和数量用list收集
					 */
					/***
					 * 通过循环来收集材料清单
					 */
					logger.info("材料清单大小==========>" + remark.length);
					for (int i = 0; i < remark.length; i++) {
						String[] re = remark[i].split(":");
						System.out.println(re.length);
						logger.info(re.toString());
						Remark cl = new Remark();
						cl.setName(re[0]);
						cl.setNum(re[1]);
						list.add(cl);
					}
				}

				// if (list.size()!=0) {
				JSONArray cljson = JSONArray.fromObject(list);
				json.put("cl", cljson);
				json.put("status", "4");
				// }

				return json.toString();
			}
		}
	}

	/***
	 * 添加remark
	 * 
	 * @param sessionId
	 * @param fileId
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value = "addremarks", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String addRemarks(@RequestParam(value = "sessionId", required = true) String sessionId,
			@RequestParam(value = "fileId", required = true) String fileId,
			@RequestParam(value = "remarks", required = true) String remarks) {
		JSONObject json = new JSONObject();
		/***
		 * 判断sessionId存不存在
		 */
		HttpSession session = SessionUtil.getInstance().getSession(sessionId);
		/***
		 * 如果不存在，则返回0
		 */
		if (session == null) {
			json.put("status", "999");
			return json.toString();
		} else {
			/***
			 * 通过session获取用户
			 */
			User user = (User) session.getAttribute("user");
			/***
			 * 通过卷宗ID判断该卷宗是否是新增卷宗
			 */
			Archives archives = archivesService.get(fileId);
			/***
			 * 获取当前时间作为id
			 */
			String time = new TimeUtil().getCurrentTime();
			/***
			 * 如果卷宗不存在说明是新增案件，则从session中取并保存
			 */
			if (archives == null) {
				archives = (Archives) session.getAttribute("archive");
				archives.setRemarks("10");
				archives.setIsNewRecord(true);
				String opration = "save";
				String ta = "ArchivesService";
				new fileUtil().expor(archives, opration, ta, user);
				archivesService.save(archives);
			} else {
				/***
				 * 如果不是新增案件，找出该案件的最后一条流转记录
				 */
				ArchivesTrank lastTrank = trankDao.findLastCircul(fileId);
				/***
				 * 如果这条记录中的user是当前用户，说明该案件就在当前用户手中，不必再次签收
				 */
				/*
				 * if(lastTrank.getUser().getId() ==
				 * user.getId()||lastTrank.getUser().getId().equals(user.getId()
				 * )){ json.put("status", "6"); return json.toString();
				 * 
				 * }
				 */
				lastTrank.setTurnTime(time);
				lastTrank.setIsNewRecord(false);
				String opration = "save";
				String ta = "ArchivesTrankService";
				new fileUtil().expor(lastTrank, opration, ta, user);
				service.save(lastTrank);
			}
			ArchivesTrank trank = new ArchivesTrank();
			trank.setUser(user);
			trank.setId(time);
			trank.setFileId(fileId);
			trank.setSignTime(time);
			trank.setTurnTime("0");
			trank.setCreateBy(user);

			trank.setUpdateBy(user);
			trank.setCurrentUser(user);
			trank.setAllPages(remarks);
			trank.setIsNewRecord(true);
			String opration = "save";
			String ta = "ArchivesTrankService";
			new fileUtil().expor(trank, opration, ta, user);
			service.save(trank);
			json.put("status", "5");
			return json.toString();
		}
	}

	private Logger logger = Logger.getLogger(TrankConctroller.class);
}
