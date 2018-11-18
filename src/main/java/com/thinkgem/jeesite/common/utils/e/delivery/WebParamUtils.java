package com.thinkgem.jeesite.common.utils.e.delivery;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author niushi
 * @Desc:web参数获取工具类
 * @date 2018年7月24日 下午4:33:31
 * @ClassName: WebParamUtils.java
 */
public class WebParamUtils {

    /**
     * @param
     * @return
     * @DESC:获取String型数据
     * @author: niushi
     * @date 2018年7月24日 下午4:34:17
     */
    public static String getString(String proptery, HttpServletRequest request,
                                   String defaultValue) {

        String temp = request.getParameter(proptery);
        if (StringUtils.isNotEmpty(temp))
            return temp;
        return defaultValue;
    }

    public static String getString(String proptery, HttpServletRequest request) {
        return getString(proptery, request, null);
    }

    /**
     * 描述：获取integer类型参数
     * @return
     * @author： news
     * @time: 2018/8/10 15:49
     * @param: * @param null
     */
    public static Integer getInteger(String proptery, HttpServletRequest request, Integer defaultValue) {
        String temp = request.getParameter(proptery);
        if (StringUtils.isNotEmpty(temp))
            return Integer.valueOf(temp);
        return defaultValue;
    }

    public static Integer getInteger(String propery, HttpServletRequest request) {
        return getInteger(propery,request,null);
    }

}
