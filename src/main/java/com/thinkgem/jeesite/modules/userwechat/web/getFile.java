package com.thinkgem.jeesite.modules.userwechat.web;

import java.io.BufferedReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Base64;

import com.thinkgem.jeesite.util.ConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.util.StringUtil;
import com.thinkgem.jeesite.util.getFirstFileUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "file")
public class getFile {
	@Autowired
	private ConfigurationService configurationService;

	Logger log = Logger.getLogger(getFile.class);
	@RequestMapping(value = "getfile", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String firstFileName() throws Exception {
		String fileName = new getFirstFileUtil().getFirst(configurationService.getInTaskPath());
		JSONObject json = new JSONObject();
		if (fileName == "" || fileName.equals("")) {
			json.put("status", "0");// 没有文件
			json.put("context", "没有文件");
		} else {
			json.put("status", "1");
			json.put("context", new getFirstFileUtil().write(fileName, configurationService.getInTaskPath()+"history/"));
		}
		log.info(json.toString());
		return json.toString();
	}
	@RequestMapping(value = "getfile2", produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String firstFileName2() throws Exception {
		String fileName = new getFirstFileUtil().getFirst(configurationService.getInTaskPath());
		JSONObject json = new JSONObject();
		if (fileName == "" || fileName.equals("")) {
			json.put("status", "0");// 没有文件
			json.put("context", "没有文件");
		} else {
			json.put("status", "1");
			json.put("context", new getFirstFileUtil().write2(fileName, configurationService.getInTaskPath()+"history/"));
		}
		log.info(json.toString());
		return json.toString();
	}
	@RequestMapping("open")
	public String open(Model model,@RequestParam(value = "downloadUrl",required = false) String downloadUrl){
		model.addAttribute("downloadUrl", downloadUrl);
		return "modules/frontend/download";
	}
	
}
