package com.thinkgem.jeesite.common.utils.e.delivery;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author news
 * @ClassName: com.thinkgem.jeesite.common.utils.e.delivery
 * @Desc: ${end}
 * @date 2018/9/11  23:33
 */
public class HttpsClientUtil {


    //日志
    public static Logger logger = Logger.getLogger(HttpsClientUtil.class);
    //编码方式
    public static String UTF8 = "UTF-8";
    //数据格式
    private final String APPLICATION_JSON = "application/json";
    //数据类型标识
    public final String CONTENT_TYPE = "Content-Type";
    //https请求客户端
    private CloseableHttpClient  httpclient = null;

    private HttpsClientUtil(){
        try{
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{
                    //证书信任管理器（用于https请求）
                    new X509TrustManager(){
                        @Override
                        public void checkClientTrusted(X509Certificate[] arg0,
                                                       String arg1) throws CertificateException {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] arg0,
                                                       String arg1) throws CertificateException {
                        }
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            }, new SecureRandom());
            //获取注册建造者
            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
            //注册http和https请求
            Registry<ConnectionSocketFactory> socketFactoryRegistry  = registryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();
            //获取HttpClient池管理者
            PoolingHttpClientConnectionManager connManager  = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            //初始化httpClient
            httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        }catch(KeyManagementException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    };

    /**
     * 描述：创建httpsClientUtil对象
     * @author： news
     * @time: 2018/9/11 23:34
     * @param:  * @param null
     * @return
     */
    private static class HttpsClientUtilHolder{
        private static HttpsClientUtil holder = new HttpsClientUtil();
    }
    public static HttpsClientUtil getInstance(){
        return HttpsClientUtilHolder.holder;
    }

    /**
     * 描述：发送post请求并获取结果
     * @author： news
     * @time: 2018/9/11 23:35
     * @param:  * @param null
     * @return
     */
    public CloseableHttpResponse sendPostRequest(String requestUrl, String outputStr){

        int statusCode = 0;
        CloseableHttpResponse execute = null;
        try{
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            // 将JSON字符串进行UTF-8编码,以便传输中文
            StringEntity requestEntity = new StringEntity(outputStr,HttpsClientUtil.UTF8);
            httpPost.setEntity(requestEntity);
            execute = httpclient.execute(httpPost);
            // 获得StatusCode
            statusCode = execute.getStatusLine().getStatusCode();
            logger.info("远程接口响应状态:"+statusCode);
        }catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("远程接口请求发生错误:",e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("远程接口请求发生错误:",e);
        }
        return execute;
    }

    /**
     * 描述：发送Get请求并获取结果
     * @author： news
     * @time: 2018/9/11 23:35
     * @param:  * @param null
     * @return
     */
    public CloseableHttpResponse sendGetRequest(String requestUrl){

        int statusCode = 0;
        CloseableHttpResponse execute = null;
        try{
            HttpGet httpGet = new HttpGet(requestUrl);
            httpGet.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            execute = httpclient.execute(httpGet);
            // 获得StatusCode
            statusCode = execute.getStatusLine().getStatusCode();
            logger.info("远程接口响应状态:"+statusCode);
        }catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("远程接口请求发生错误:",e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("远程接口请求发生错误:",e);
        }
        return execute;
    }

    /**
     * 描述：转换请求结果为JSONObject
     * @author： news
     * @time: 2018/9/11 23:35
     * @param:  * @param null
     * @return
     */
    public JSONObject  requestFormatJSON(CloseableHttpResponse response){
        String responseObj = null;
        HttpEntity entity = response.getEntity();
        if(entity!=null){
            try {
                responseObj = EntityUtils.toString(entity,HttpsClientUtil.UTF8);
                //关闭输出流
                response.close();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("远程接口响应:"+responseObj);
        return JSONObject.fromObject(responseObj);
    }


    /**
     * 描述：拼接URL后接参数
     * @author： news
     * @time: 2018/9/11 23:35
     * @param:  * @param null
     * @return
     */
    public String requestUrlParam(String requestUrl,Map<String,String> requestUrlParam){
        if(requestUrlParam==null){
            return requestUrl;
        }
        String requestParam = "";
        for (Entry<String, String> entry : requestUrlParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestParam += "&"+key+"="+value;
        }
        if(requestParam.length()>0){
            if(requestUrl.indexOf("?")==-1){
                requestUrl = requestUrl+"?"+requestParam.substring(1);
            }else{
                requestUrl = requestUrl+requestParam;
            }
        }
        return requestUrl;
    }

    /**
     * 描述：发送post请求并获取结果
     * @author： news
     * @time: 2018/9/11 23:35
     * @param:  * @param null
     * @return
     */
    public JSONObject sendPostRequestJson(String requestUrl, String outputStr){
        CloseableHttpResponse response = sendPostRequest(requestUrl, outputStr);
        return requestFormatJSON(response);
    }

   /**
    * 描述：发送get请求并获取结果
    * @author： news
    * @time: 2018/9/11 23:42
    * @param:  * @param null
    * @return
    */
    public JSONObject sendGetRequestJson(String requestUrl){
        CloseableHttpResponse response = sendGetRequest(requestUrl);
        return requestFormatJSON(response);
    }

    public static void main(String[] args) {
        HttpsClientUtil clientUtil = new HttpsClientUtil();
//		  AppId: 'wxfea587b0ec4ad58b',
//		    AppSecret: '658bd0d5408c81dcd0349f18f527848f',
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wxfea587b0ec4ad58b" + "&secret=" + "658bd0d5408c81dcd0349f18f527848f" + "&js_code=" + "061MeH1f15Dfxr05BA3f1zFl1f1MeH1P" + "&grant_type=authorization_code";

        JSONObject jsonObject = clientUtil.sendGetRequestJson(url);
        System.out.println(jsonObject.toString());

    }
}
