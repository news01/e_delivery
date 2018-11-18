package com.thinkgem.jeesite.common.utils.e.delivery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

/**
 * @Desc:JSON格式化工具类 
 * @author niushi
 * @date 2018年7月25日 下午6:10:00
 * @ClassName: JSONUtil.java
 */
public class JSONUtil {

	// 静态代码块
	static {

		// 注册时间格式
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd", "yyyy-MM-dd HH",
						"yyyy-MM-dd HH:mm" }));
	}

	/**
	 * 描述：返回时间格式Config
	 * 
	 * @return
	 */
	public static JsonConfig getDateConfig1111() {

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		jsonConfig.registerDefaultValueProcessor(String.class,
				new DefaultValueProcessor() {
					@SuppressWarnings("rawtypes")
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		return jsonConfig;
	}

	/**
	 * 描述：返回时间格式Config
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JsonConfig formatNullConfig() {

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		jsonConfig.registerDefaultValueProcessor(String.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		jsonConfig.registerDefaultValueProcessor(Integer.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		jsonConfig.registerDefaultValueProcessor(Short.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		jsonConfig.registerDefaultValueProcessor(Long.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		jsonConfig.registerDefaultValueProcessor(Double.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});
		jsonConfig.registerDefaultValueProcessor(BigDecimal.class,
				new DefaultValueProcessor() {
					@Override
					public Object getDefaultValue(Class arg0) {
						return null;
					}
				});

		return jsonConfig;
	}

	/**
	 * 
	 * 描述： 对象转JSON数据
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSON(Object object) {
		return JSONObject.fromObject(object).toString();
	}

	/**
	 * 
	 * 描述： 对象转JSON数据 带CONFIG
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSON(Object object, JsonConfig config) {
		return JSONObject.fromObject(object, config).toString();
	}
	
	/**
	 * 
	 * 描述：格式化详情（""-null,date-yyyyMMdd hh:mm:ss）
	 * @param object
	 * @return
	 */
	public static String toFormatJSON(Object object) {
		return JSONObject.fromObject(object, JSONUtil.formatNullConfig()).toString();
	}

	/**
	 * 描述：数组转JOSN数据
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toJSON(List list) {
		return JSONArray.fromObject(list).toString();
	}

}
