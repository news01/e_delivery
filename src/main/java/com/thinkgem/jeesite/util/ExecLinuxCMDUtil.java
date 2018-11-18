package com.thinkgem.jeesite.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ExecLinuxCMDUtil {
	public static final  ExecLinuxCMDUtil instance = new ExecLinuxCMDUtil();
	public static Object exec(String cmd){
    	try {
    		String[] cmdA = { "cmd", "/c", cmd };
    		
			Process process = Runtime.getRuntime().exec(cmdA);
			
			LineNumberReader br = new LineNumberReader(
					new InputStreamReader(
							process.getInputStream()));
			
			StringBuffer sb = new StringBuffer();
			
			String line;
			
			while((line = br.readLine()) != null){
				System.out.println(line);
                sb.append(line).append("\n");
			}
			
			return sb.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	
    }
}
