/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.wx.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.wx.dao.WxDsrDao;
import com.thinkgem.jeesite.modules.wx.entity.WxDsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 绑定微信账号的当事人Service
 * @author zmj
 * @version 2017-09-16
 */
@Service
@Transactional(readOnly = true)
public class WxDsrService extends CrudService<WxDsrDao, WxDsr> {

	@Autowired
	private WxDsrDao wxDsrDao;

	private WxDsr dummyWxDsr = null;

//	@Autowired
//	private WxMbService wxMbService;

	public WxDsrService() {
		dummyWxDsr = new WxDsr();
	}

	public WxDsr get(String id) {
		return super.get(id);
	}

	public String getUnionIdByZjhm(String zjhm) {
		WxDsr wxDsr = new WxDsr();
		wxDsr.setZjhm(zjhm);
		wxDsr = wxDsrDao.getByZjhm(wxDsr);
		return (wxDsr == null) ? null : wxDsr.getZjhm();
	}

	public List<WxDsr> findList(WxDsr wxDsr) {
		return super.findList(wxDsr);
	}
	
	public Page<WxDsr> findPage(Page<WxDsr> page, WxDsr wxDsr) {
		return super.findPage(page, wxDsr);
	}




	public Page<WxDsr> findPageFuzzy(Page<WxDsr> page, WxDsr wxDsr) {
		wxDsr.setPage(page);
		page.setList(dao.findPageFuzzy(wxDsr));
		return page;
	}



	public List<WxDsr> findAuthenticatedList() {
		return wxDsrDao.findAuthenticatedList(dummyWxDsr);
	}
	
	@Transactional(readOnly = false)
	public void save(WxDsr wxDsr) {
		super.save(wxDsr);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxDsr wxDsr) {
		super.delete(wxDsr);
	}



//	public byte[] getDzsdqrs(String id,String mbName,String lx,String ah,String ay){
//		mbName = "/templates/j36/modules/dzqrs/"+mbName;
//		return replaceDzsdqrs(id,mbName,lx,ah,ay);
//	}


//	private byte[]replaceDzsdqrs(String id,String path,String lx,String ah,String ay){
//		Resource resource = new ClassPathResource(path);
//		try (InputStream is = resource.getInputStream();
//			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//			HWPFDocument doc2 = new HWPFDocument(is);
//			// 读取资料，根据模版信息生成文书信息
//			Range range = doc2.getRange();
//			if("dsr".equals(lx)){
//				wxMbService.replace(wxMbService.getDsrInfo(id,ah,ay),range);
//			}else {
//				wxMbService.replace(wxMbService.getDlrInfo(id,ah,ay),range);
//			}
//
//			doc2.write(out);
//			return out.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("", e);
//			throw new RuntimeException(e);
//		}
//	}

    public WxDsr getByZjhm(String zjhm) {
		WxDsr wxDsr = new WxDsr();
		wxDsr.setZjhm(zjhm);
		return wxDsrDao.getByZjhm(wxDsr);
    }

    public WxDsr getByUnionId(String unionId){

		return wxDsrDao.getByUnionId(unionId);

	}
}