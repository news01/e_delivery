package com.thinkgem.jeesite.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hc77.utils.DataExporter;
import com.hc77.utils.DataImporter;
import com.thinkgem.jeesite.modules.sys.entity.User;

import net.sf.json.JSONObject;

public class fileUtil implements ServletContextListener{

	ConfigurationService configurationService;
	private DataExporter dataExportor = null;

	public fileUtil(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		if(wac!=null) {
			configurationService =wac.getBean(ConfigurationService.class);
		}
	}


	DataImporter<DataUtil> dataImporter =null;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		if(configurationService==null) {
			ServletContext ctx = sce.getServletContext();
			WebApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(ctx);
			configurationService = springCtx.getBean(ConfigurationService.class);
		}
		dataImporter = new DataImporter<DataUtil>(configurationService.getImportPath(), configurationService.getPrefixNw(), new wechatService(), DataUtil.class);
		dataImporter.init();
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		 if (dataImporter != null)
	            dataImporter.deinit();
	    
	}
	
	
	public void expor( Object obj, String opration, String service,User user){
		DataUtil dtu = new DataUtil();
		dtu.setIsNewRecord(true);
		dtu.setObj(obj);
		dtu.setOpration(opration);
		dtu.setTable(service);
		if (dataExportor == null) {
			dataExportor = new DataExporter(configurationService.getInTaskPath(),configurationService.getLsajPrefix() );
			dataExportor.export(dtu);
		}
		
		dataExportor=null;
	}
}
