package com.thinkgem.jeesite.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.druid.util.HttpClientUtils;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class get3rdSessionIdUtil {
	@Autowired
	ConfigurationService configurationService;

	public Object get3rdSession(String code) {
		System.out.println(code);
		String openid=null;
		JSONObject json = new JSONObject();
		String Url = new StringUtil().SessionUrl+"?appid="+configurationService.getAppId()+"&secret="+configurationService.getAppSecret()
				+ "&js_code=" + code + "&grant_type=authorization_code";
		Object session = ExecLinuxCMDUtil.instance.exec("cat /dev/urandom |od -x | tr -d ' '| head -n 1");
		CloseableHttpClient httpClient = getHttpClient();
		try {
			json.put("session", session);
			HttpGet get = new HttpGet(Url);
			System.out.println("执行get请求:...." + get.getURI());
			CloseableHttpResponse httpResponse = null;
			httpResponse = httpClient.execute(get);
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity);
					System.out.println(result);

					JSONObject resultJson = JSONObject.fromObject(result);
					openid = resultJson.getString("openid");
					System.out.println("openid:" + openid);
				}
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}finally{
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return openid;

	}

	private CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}

	private void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}

}
