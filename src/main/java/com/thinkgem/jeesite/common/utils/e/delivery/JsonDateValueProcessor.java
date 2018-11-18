package com.thinkgem.jeesite.common.utils.e.delivery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * @ClassName: JsonDateValueProcessor
 * @version 1.0 
 * @Desc: 时间格式处理类
 *
 */

public class JsonDateValueProcessor implements JsonValueProcessor {  
	
    private String format ="yyyy-MM-dd HH:mm:ss";  //定义公共时间格式
      
    public JsonDateValueProcessor() {  
        super();  
    }  
      
    public JsonDateValueProcessor(String format) {  
        super();  
        this.format = format;  
    }  
  
    @Override  
    public Object processArrayValue(Object paramObject,  
            JsonConfig paramJsonConfig) {  
        return process(paramObject);  
    }  
  
    @Override  
    public Object processObjectValue(String paramString, Object paramObject,  
            JsonConfig paramJsonConfig) {  
        return process(paramObject);  
    }  
      
      
    private Object process(Object value){  
        if(value instanceof Date){    
        	//时间格式转化
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);    
            return sdf.format(value);  
        }    
        return value == null ? null : value.toString();    
    }  
  
}  
