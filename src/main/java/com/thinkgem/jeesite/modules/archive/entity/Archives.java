/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archive.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 测试Entity
 * @author 朱明军
 * @version 2017-06-02
 */
public class Archives extends DataEntity<Archives> {
	
	private static final long serialVersionUID = 1L;
	private String caseNum;		// 案号
	private String caseCase;		// 案由
	private String pla;		// 原告
	private String defen;		// 被告
	private String bookNum;		// 册数
	private String allBooks;		// 总册数
	private String pageNum;		// 当册页数
	private Date updateTime;		// 更新时间
	private String ajid;		// ajid
	private String stid;		// stid
	
	public Archives() {
		super();
	}

	public Archives(String id){
		super(id);
	}

	@Length(min=0, max=255, message="案号长度必须介于 0 和 255 之间")
	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	
	@Length(min=0, max=255, message="案由长度必须介于 0 和 255 之间")
	public String getCaseCase() {
		return caseCase;
	}

	public void setCaseCase(String caseCase) {
		this.caseCase = caseCase;
	}
	
	@Length(min=0, max=255, message="原告长度必须介于 0 和 255 之间")
	public String getPla() {
		return pla;
	}

	public void setPla(String pla) {
		this.pla = pla;
	}
	
	@Length(min=0, max=255, message="被告长度必须介于 0 和 255 之间")
	public String getDefen() {
		return defen;
	}

	public void setDefen(String defen) {
		this.defen = defen;
	}
	
	@Length(min=0, max=11, message="册数长度必须介于 0 和 11 之间")
	public String getBookNum() {
		return bookNum;
	}

	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	
	@Length(min=0, max=11, message="总册数长度必须介于 0 和 11 之间")
	public String getAllBooks() {
		return allBooks;
	}

	public void setAllBooks(String allBooks) {
		this.allBooks = allBooks;
	}
	
	@Length(min=0, max=11, message="当册页数长度必须介于 0 和 11 之间")
	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Length(min=0, max=20, message="ajid长度必须介于 0 和 20 之间")
	public String getAjid() {
		return ajid;
	}

	public void setAjid(String ajid) {
		this.ajid = ajid;
	}
	
	@Length(min=0, max=2, message="stid长度必须介于 0 和 2 之间")
	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	
}