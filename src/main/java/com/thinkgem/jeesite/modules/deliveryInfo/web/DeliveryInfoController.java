/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.deliveryInfo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.hc77.utils.DataExporter;
import com.thinkgem.jeesite.common.utils.e.delivery.*;
import com.thinkgem.jeesite.modules.caseinfo.entity.CaseInfo;
import com.thinkgem.jeesite.modules.caseinfo.service.CaseInfoService;
import com.thinkgem.jeesite.modules.deliveryInfo.entity.DeliveryInfo;
import com.thinkgem.jeesite.modules.deliveryInfo.service.DeliveryInfoService;
import com.thinkgem.jeesite.modules.userwechat.entity.SysUser;
import com.thinkgem.jeesite.modules.wx.entity.EdeliveryExport;
import com.thinkgem.jeesite.util.ConfigurationService;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 网格员送达信息Controller
 *
 * @author niushi
 * @version 2018-08-15
 */
@Controller
public class DeliveryInfoController extends BaseController {

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    @Autowired
    private CaseInfoService caseInfoService;

    @Autowired
    private ConfigurationService configurationService;

    private DataExporter outDataExporter = null;

    /**
     * 描述：上传图片并保存送达信息,异步
     *
     * @return
     * @author： news
     * @time: 2018/8/16 15:00
     * @param: * @param null
     */
//    @RequestMapping(value = "/deliveryInfo/uploadDeliveryInfo")
    public void uploadDeliveryInfo(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {
                //获取文件需要上传到的路径
                String path = request.getRealPath("/upload") + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                logger.debug("path=" + path);

                request.setCharacterEncoding("utf-8");  //设置编码
                DiskFileItemFactory factory = new DiskFileItemFactory();

                factory.setRepository(dir);
                //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
                factory.setSizeThreshold(1024 * 1024);
                ServletFileUpload upload = new ServletFileUpload(factory);
                try {
                    List<FileItem> list = upload.parseRequest(request);
                    FileItem picture = null;
                    for (FileItem item : list) {
                        //获取表单的属性名字
                        String name = item.getFieldName();
                        //如果获取的表单信息是普通的 文本 信息
                        if (item.isFormField()) {
                            //获取用户具体输入的字符串
                            String value = item.getString();
                            request.setAttribute(name, value);
                            logger.debug("name=" + name + ",value=" + value);
                        } else {
                            picture = item;
                        }
                    }

                    //自定义上传图片的名字为userId.jpg
                    String fileName = request.getAttribute("userId") + ".jpg";
                    String destPath = path + fileName;
                    logger.debug("destPath=" + destPath);

                    //真正写到磁盘上
                    File file = new File(destPath);
                    OutputStream out = new FileOutputStream(file);
                    InputStream in = picture.getInputStream();
                    int length = 0;
                    byte[] buf = new byte[1024];
                    // in.read(buf) 每次读到的数据存放在buf 数组中
                    while ((length = in.read(buf)) != -1) {
                        //在buf数组中取出数据写到（输出流）磁盘上
                        out.write(buf, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (FileUploadException e1) {
                    logger.error("", e1);
                } catch (Exception e) {
                    logger.error("", e);
                }


                PrintWriter printWriter = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                HashMap<String, Object> res = new HashMap<String, Object>();
                res.put("success", true);
//        printWriter.write(JSON.toJSONString(res));
                printWriter.flush();

                return null;
            }
        }, request, response, sessionId);
    }


    /**
     * 描述：上传图片并保存送达信息
     *
     * @return
     * @author： news  wx：ns321456
     * @time: 2018/8/16 14:58
     * @param: * @param null
     */
    @RequestMapping("/deliveryInfo/uploadDeliveryInfo")
    public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {

        byte[] buf = new byte[1024];
        int result = 0;
//        String imgPath = null;
        String imgPath = null;
        String databaseImgPath = null;

        //获取文件需要上传到的路径
        String projectPath = request.getSession().getServletContext().getRealPath("/");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        imgPath = new String(dateFormat.format(calendar.getTime()));

//        String path = "D:\\openTools_on\\tomcat\\ideaServer\\service_1\\apache-tomcat-8.5.15\\webapps\\ROOT\\upload\\";

        File dir = Paths.get(projectPath, "upload", imgPath).toFile();
        if (!dir.exists()) {
            boolean a = dir.mkdirs();
            System.out.println(a);
        }
        logger.debug("path=" + dir.toString());

        request.setCharacterEncoding("utf-8");  //设置编码
        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setRepository(dir);
        factory.setSizeThreshold(1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            FileItem picture = null;
            for (FileItem item : list) {
                //获取表单的属性名字
                String name = item.getFieldName();
                //如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    //获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                    logger.debug("name=" + name + ",value=" + value);
                } else {
                    picture = item;
                }
            }

            //自定义上传图片的名字为时间戳
            String fileName = System.currentTimeMillis() + ".png";
            imgPath = Paths.get(imgPath, fileName).toString();
//            String destPath = path.toString() + fileName;
            databaseImgPath = Paths.get(dir.toString(), fileName).toString();
            logger.debug("destPath=" + Paths.get(dir.toString(), fileName).toString());

            //真正写到磁盘上
            File file = Paths.get(dir.toString(), fileName).toFile();
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
            int length = 0;

//            in.read(buf);
//            buf = IOUtils.toByteArray(in);
//            System.out.println(buf.toString());

            byte[] buf2 = new byte[1024];
//             in.read(buf2); //每次读到的数据存放在buf 数组中
            while ((length = in.read(buf2)) != -1) {
                //在buf数组中取出数据写到（输出流）磁盘上
                out.write(buf2, 0, length);
            }
            buf = buf2;
            in.close();
            out.close();
        } catch (FileUploadException e1) {
            logger.error("", e1);
        } catch (Exception e) {
            logger.error("", e);
        }

        /**
         * 获取上传的图片并转为database64
         */
        String databaseImg = new DeliveryInfoController().GetImageStrFromPath(databaseImgPath);

        String paramters = (String) request.getAttribute("paramters");
        String img_num = (String) request.getAttribute("img_num");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        DeliveryInfo deliveryInfo = new DeliveryInfo();

        if (StringUtils.isNotBlank(img_num) && img_num.equals("1")) {
//            仅在第一张图片图片上传的时候把用户数据提交
            if (StringUtils.isNotBlank(paramters)) {
                JSONObject jsonObject = JSONObject.fromObject(paramters);
                if (jsonObject.has("caseId")) {
                    String caseId = (String) jsonObject.get("caseId");
                    deliveryInfo.setCaseId(caseId);

                }
                String timeout = null;
                if (jsonObject.has("times")) {
                    String times = (String) jsonObject.get("times");
                    deliveryInfo.setDevNum(Integer.parseInt(times));
                    timeout = times;
                }
                if (jsonObject.has("girdWorker2") && jsonObject.has("girdWorker1")) {
                    String girdWorker2 = (String) jsonObject.get("girdWorker2");
                    girdWorker2 = URLDecoder.decode(girdWorker2, "UTF-8");

                    String girdWorker1 = (String) jsonObject.get("girdWorker1");
                    girdWorker1 = URLDecoder.decode(girdWorker1, "UTF-8");

                    //将两名网格员姓名已字符串同时存入数据库，左为网格员一，右为网格员二
                    String workers = girdWorker1 + "/" + girdWorker2;
                    deliveryInfo.setProveStaff(workers);
                }
                if (jsonObject.has("radioValue")) {
                    String radioValue = (String) jsonObject.get("radioValue");
//                    deliveryInfo.setAttribute1(radioValue);
                    if (radioValue.equals("2")) {
                        String chooseBox = null;
                        if (jsonObject.has("textAreaValue")) {
                            String textAreaValue = (String) jsonObject.get("textAreaValue");
                            textAreaValue = URLDecoder.decode(textAreaValue, "UTF-8");

                            JSONObject textArea = JSONObject.fromObject(textAreaValue);
                            if (textArea.has("status")) {
                                String status = (String) textArea.get("status");
                                if (status.equals("2")) {
                                    if (textArea.has("chooseBox")) {
                                        chooseBox = (String) textArea.get("chooseBox");
                                        //包含0、1、2时结束本次送达任务
                                        if (chooseBox.indexOf("0") != -1 || chooseBox.indexOf("1") != -1 || chooseBox.indexOf("2") != -1) {
                                            deliveryInfo.setAttribute1("3");
                                            deliveryInfo.setDevStatus("10");
                                        } else if (chooseBox.indexOf("6") != -1) {
                                            //如果为6则不强制送达三次
                                            deliveryInfo.setAttribute1("4");
                                            deliveryInfo.setDevStatus("11");
                                        } else {
//                                            deliveryInfo.setAttribute1(radioValue);

                                            /**
                                             * 2018/10/12
                                             * 修改送达状态
                                             */
                                            if (timeout.equals("1")) {
                                                deliveryInfo.setDevStatus("7");
                                            } else if (timeout.equals("2")) {
                                                deliveryInfo.setDevStatus("8");
                                            } else if (timeout.equals("3")) {
                                                deliveryInfo.setDevStatus("9");
                                                deliveryInfo.setAttribute1(radioValue);
                                            }
                                        }
                                    }

                                }
                            }

                            deliveryInfo.setAttribute2(textAreaValue);//失败证明
                        }
                        //当送达次数为第三次，且未被当事人接收时
                        if ("3".equals(timeout) || chooseBox.indexOf("0") != -1 || chooseBox.indexOf("1") != -1 || chooseBox.indexOf("2") != -1 || chooseBox.indexOf("6") != -1) {
                            if (jsonObject.has("c_id")) {
                                String c_id = (String) jsonObject.get("c_id");
                                int resp = caseInfoService.updateCaseInfoById(new CaseInfo() {
                                    {
                                        setId(c_id);
                                        setCaseStatus("4");//未被当事人接收
//                                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String date2 = simpleDateFormat.format(date);
                                        setAttribute3(date2);
                                        setAttribute2("4");//送达失败

                                    }
                                });

                                if (outDataExporter == null) {
                                    outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                                }

                                List<CaseInfo> caseInfos = caseInfoService.selectCaseInfo(new CaseInfo() {{
                                    setId(c_id);
                                }});
                                CaseInfo caseInfo1 = caseInfos.get(0);

                                EdeliveryExport edeliveryExport = new EdeliveryExport();
                                edeliveryExport.setCaseInfo(caseInfo1);
                                outDataExporter.export(edeliveryExport);

                            }

                        }


                    } else if (radioValue.equals("1")) {

                        if (jsonObject.has("textAreaValue")) {
                            String textAreaValue = (String) jsonObject.get("textAreaValue");
                            textAreaValue = URLDecoder.decode(textAreaValue, "UTF-8");

                            JSONObject textArea = JSONObject.fromObject(textAreaValue);
                            if (textArea.has("status")) {
                                String status = (String) textArea.get("status");
                                if (status.equals("1")) {

//                                        if (textArea.has("chooseBox")) {
//                                            String chooseBox = (String) textArea.get("chooseBox");
//                                        }
                                    deliveryInfo.setAttribute1("1");
                                    deliveryInfo.setAttribute2(textAreaValue);//成功证明

                                }


                            }


                        }

                        //当用户提交成功的送达信息时，修改案件状态
                        if (jsonObject.has("c_id")) {
                            String c_id = (String) jsonObject.get("c_id");

                            int resp = caseInfoService.updateCaseInfoById(new CaseInfo() {
                                {
                                    setId(c_id);
                                    setCaseStatus("5");//被送达人签收
                                    setDeliverSignTime(new Date());
                                    setAttribute2("3");//送达成功
                                }
                            });
                            System.out.println("当用户提交成功的送达信息时，修改案件状态resp=" + resp);
                            if (outDataExporter == null) {
                                outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
                            }
                            List<CaseInfo> caseInfos = caseInfoService.selectCaseInfo(new CaseInfo() {{
                                setId(c_id);
                            }});
                            CaseInfo caseInfo1 = caseInfos.get(0);
                            EdeliveryExport edeliveryExport = new EdeliveryExport();
                            edeliveryExport.setCaseInfo(caseInfo1);
                            outDataExporter.export(edeliveryExport);

                        }

                        /**
                         * 修改送达状态
                         */
                        if (timeout.equals("1")) {
                            deliveryInfo.setDevStatus("4");
                        } else if (timeout.equals("2")) {
                            deliveryInfo.setDevStatus("5");
                        } else if (timeout.equals("3")) {
                            deliveryInfo.setDevStatus("6");
                        }

                    }
                }
                if (jsonObject.has("local")) {
                    String local = (String) jsonObject.get("local");
                    local = URLDecoder.decode(local, "UTF-8");
                    deliveryInfo.setAttribute3(local);//送达定位位置
                }
            }

//            deliveryInfo.setDevPhoto1(buf);

            deliveryInfo.setDevTime(date);

            deliveryInfo.setAttribute4(imgPath.toString());//图片保存路径
            byte[] bytes = databaseImg.getBytes();
            deliveryInfo.setDevVideo1(bytes);


            result = deliveryInfoService.insertDeliveryInfo(deliveryInfo);


            if (outDataExporter == null) {
                outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
            }

            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                setCaseId(deliveryInfo.getCaseId());
            }});
            DeliveryInfo deliveryInfo1 = deliveryInfos.get(0);
            String time = simpleDateFormat.format(date);
            deliveryInfo1.setDevTime2(time);
            byte[] bytes1 = deliveryInfo1.getDevVideo1();

            String photo = new String(bytes);
//            BASE64Encoder encoder = new BASE64Encoder();
            // 返回Base64编码过的字节数组字符串
//            String aaa = encoder.encode(bytes1);

//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] key = decoder.decodeBuffer(aaa);
//            String test2 = new String(key);
//            System.out.println(test2);

            EdeliveryExport edeliveryExport = new EdeliveryExport();
            edeliveryExport.setDeliveryInfo(deliveryInfo1);

            outDataExporter.export(edeliveryExport);
        } else if (img_num.equals("2")) {
            JSONObject jsonObject = JSONObject.fromObject(paramters);
            if (jsonObject.has("caseId")) {
                String caseId = (String) jsonObject.get("caseId");
                deliveryInfo.setCaseId(caseId);
            }
            if (jsonObject.has("times")) {
                String times = (String) jsonObject.get("times");
                deliveryInfo.setDevNum(Integer.parseInt(times));
            }
//            deliveryInfo.setDevPhoto2(buf);
            deliveryInfo.setAttribute5(imgPath.toString());//图片保存路径

            byte[] bytes = databaseImg.getBytes();
            deliveryInfo.setDevVideo2(bytes);
            result = deliveryInfoService.updateDeliveryInfo(deliveryInfo);

            if (outDataExporter == null) {
                outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
            }

            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                setCaseId(deliveryInfo.getCaseId());
            }});
            DeliveryInfo deliveryInfo1 = deliveryInfos.get(0);
            String time = simpleDateFormat.format(date);
            deliveryInfo1.setDevTime2(time);

            EdeliveryExport edeliveryExport = new EdeliveryExport();
            edeliveryExport.setDeliveryInfo(deliveryInfo1);

            outDataExporter.export(edeliveryExport);
        } else if (img_num.equals("3")) {
            JSONObject jsonObject = JSONObject.fromObject(paramters);
            if (jsonObject.has("caseId")) {
                String caseId = (String) jsonObject.get("caseId");
                deliveryInfo.setCaseId(caseId);
            }
            if (jsonObject.has("times")) {
                String times = (String) jsonObject.get("times");
                deliveryInfo.setDevNum(Integer.parseInt(times));
            }
//            deliveryInfo.setDevPhoto3(buf);
            deliveryInfo.setAttribute6(imgPath.toString());//图片保存路径

            byte[] bytes = databaseImg.getBytes();
            deliveryInfo.setDevVideo3(bytes);

            result = deliveryInfoService.updateDeliveryInfo(deliveryInfo);

            if (outDataExporter == null) {
                outDataExporter = new DataExporter(configurationService.getInTaskPath(), configurationService.getPrefixNw());
            }

            List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {{
                setCaseId(deliveryInfo.getCaseId());
            }});
            DeliveryInfo deliveryInfo1 = deliveryInfos.get(0);
            String time = simpleDateFormat.format(date);
            deliveryInfo1.setDevTime2(time);

            EdeliveryExport edeliveryExport = new EdeliveryExport();
            edeliveryExport.setDeliveryInfo(deliveryInfo1);

            outDataExporter.export(edeliveryExport);
        }


        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        if (result <= 0) {
            res.put("fail", false);
        } else {
            res.put("success", true);
        }
        JSONObject jsonObject = new JSONObject();
        printWriter.write(JSON.toJSONString(res));
        printWriter.flush();
    }


    /**
     * 描述：查看送达资料
     *
     * @return
     * @author： news
     * @time: 2018/8/16 19:25
     * @param: * @param null
     */
    @RequestMapping(value = "/deliveryInfo/getDeliveryInfo")
    public void getDeliveryInfo(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        HandlerProxy.assembleAjax(new ControlHandler() {
            @Override
            public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, SysUser user, ResultVo resultVo, String sessionId) throws Exception {

                String caseId = WebParamUtils.getString("caseId", request);
                String times = WebParamUtils.getString("times", request);

                List<DeliveryInfo> deliveryInfos = deliveryInfoService.getDelInfoByCaseId(new DeliveryInfo() {
                    {
                        setCaseId(caseId);
                        setDevNum(Integer.parseInt(times));
                    }
                });

                if (deliveryInfos.size() > 1) {
                    resultVo.setErrCode(2);
                    resultVo.setErrMsg("大于1");
                    return null;
                }

                //获取文件需要上传到的路径
/*
                String projectPath = request.getSession().getServletContext().getRealPath("/");
                StringBuffer stringBuffer = new StringBuffer(new File(projectPath).getParentFile().getParentFile().getAbsolutePath());
                stringBuffer.append("\\webapps\\ROOT\\upload\\");
*/
                DeliveryInfo deliveryInfo = deliveryInfos.get(0);
                List<Map> caseInfoList1 = new ArrayList<Map>();
                caseInfoList1.add(new HashMap() {
                    {
                        put("devId", deliveryInfo.getDevId());
                        put("caseId", deliveryInfo.getCaseId());
                        put("devNum", deliveryInfo.getDevNum());
                        put("devTime", deliveryInfo.getDevTime());

//                        String devPhoto1 = deliveryInfo.getAttribute4();
//                        String devPhoto2 = deliveryInfo.getAttribute5();
//                        String devPhoto3 = deliveryInfo.getAttribute6();

//                        byte[] data = new byte[is.available()];
//                        is.read(data);
//                        is.close();
//                        BASE64Encoder encoder = new BASE64Encoder();
//                        String devPhoto1Base64Img = encoder.encode(devPhoto1);
//                        String devPhoto2Base64Img = encoder.encode(devPhoto2);
//                        String devPhoto3Base64Img = encoder.encode(devPhoto3);
//                        System.out.print(devPhoto1Base64Img);

//                        String addr = request.getLocalAddr();
//                        Integer port = request.getLocalPort();
                        //StringBuffer addressIp = new StringBuffer(request.getServerName()+":"+request.getServerPort()+request.getContextPath());
                        String addressIp = "/upload/";

                        if (StringUtils.isNotBlank(deliveryInfo.getAttribute4())) {
                            String path1 = deliveryInfo.getAttribute4();
                            put("devPhoto1", addressIp + path1.replaceAll("\\\\", "/"));//图片存储路径
                        }
                        if (StringUtils.isNotBlank(deliveryInfo.getAttribute5())) {
                            String path2 = deliveryInfo.getAttribute5();
                            put("devPhoto2", addressIp + path2.replaceAll("\\\\", "/"));
                        }
                        if (StringUtils.isNotBlank(deliveryInfo.getAttribute6())) {
                            String path3 = deliveryInfo.getAttribute6();
                            put("devPhoto3", addressIp + path3.replaceAll("\\\\", "/"));
                        }

                        put("proveStaff", deliveryInfo.getProveStaff());

                        String resp = deliveryInfo.getAttribute1();
                        if (StringUtils.isBlank(resp)) {
                            put("attribute1", 2);//是否已送达成功1:成功;2:未成功
                        } else {
                            put("attribute1", deliveryInfo.getAttribute1());//是否已送达成功1:成功;2:未成功
                        }

                        put("attribute2", deliveryInfo.getAttribute2());//'失败原因证明
                        put("attribute3", deliveryInfo.getAttribute3());//'送达定位地址

                    }
                });

                System.out.println(deliveryInfo.toString());
                resultVo.setErrCode(1);
                resultVo.setResult(caseInfoList1);

                return null;
            }
        }, request, response, sessionId);
    }



    /**
     * 描述：将本地图片转为database64
     *
     * @return
     * @author： news
     * @time: 2018/10/10 19:55
     * @param: * @param null
     */
    public String GetImageStrFromPath(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

}