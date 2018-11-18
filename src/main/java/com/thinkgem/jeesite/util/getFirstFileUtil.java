package com.thinkgem.jeesite.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkgem.jeesite.modules.userwechat.web.getFile;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


public class getFirstFileUtil {
	@Autowired
	private ConfigurationService configurationService;

	static Logger logger = Logger.getLogger(getFirstFileUtil.class);

	public String getFirst(String dirPath) {
		List<String> fileNames = getFirstFileUtil.getFiles(dirPath);
		if (fileNames.size() == 0) {
			logger.debug("没有文件");
			return "";
		} else {
			if (this.IsRepead(configurationService.getInTaskPath()+"history/" ,fileNames.get(0))) {
				return fileNames.get(0);
			} else {
				try {
					logger.debug(fileNames.get(0) + "已被处理过，不再处理");
					new getFirstFileUtil().write(fileNames.get(0), configurationService.getInTaskPath()+"repeat/");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "";
			}

		}
	}

	public static List<String> getFiles(String dirPath) {
		List<String> file_names = null;
		File dir = new File(dirPath);
		if (dir.exists()) {
			file_names = new ArrayList<String>();
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
			}
			// 排序
			Arrays.sort(files, new getFirstFileUtil.CompratorByLastModified());
			for (int i = 0; i < files.length; i++) {
				// 获取文件最后修改时间
				String creatime = new getFirstFileUtil().format("yyyy-MM-dd hh:mm:ss:SSS",
						new Date(files[i].lastModified()));
				if (!files[i].isHidden() && !files[i].isDirectory()) {
					file_names.add(files[i].getName());
				}
				if (files[i].isHidden()) {
					logger.debug("创建时间：" + creatime + "<=它是一个隐藏文" + "=>" + files[i].getName());
				} else if (files[i].isDirectory()) {// 判断是目录
					logger.debug("创建时间：" + creatime + "<=它是一个文件夹" + "=>" + files[i].getName());
				} else {// 普通文件
					logger.debug("创建时间：" + creatime + "<=它是一个普文件" + "=>" + files[i].getName());
				}
			}
		} else {
			logger.debug("该目录没有任何文件信息！");
		}
		return file_names;
	}

	private String format(String format, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	private static class CompratorByLastModified implements Comparator<File> {

		@Override
		public int compare(File f1, File f2) {
			// TODO Auto-generated method stub
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}

		public boolean equals(Object obj) {
			return true;
		}
	}

	public boolean IsRepead(String dirPath, String fileName) {
		File dir = new File(dirPath);
		boolean flag = true;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName() == fileName || files[i].getName().equals(fileName)) {
					flag = false;
				}
			}
		}
		return flag;

	}
	public String write(String fileName, String dirPath) throws Exception {
		String filename = configurationService.getInTaskPath() + fileName;
		String filewritepath = dirPath + fileName;
		InputStream in = new FileInputStream(filename);
		byte[] b = new byte[in.available()];
        in.read(b);
        String str = Base64.getEncoder().encodeToString(b);
		File file2 = new File(filename);
		File wfilepath = new File(dirPath);
		if (!wfilepath.exists()) {
			wfilepath.mkdirs();
		}
		while (file2.exists()) {
			File file = new File(filewritepath);
			file2.renameTo(file);
		}
		JSONObject json = new JSONObject();
		json.put("data", str);
		json.put("fileName", fileName);
		json.put("length", b.length);
		return json.toString();
	}
	
	public String write2(String fileName, String dirPath) throws Exception{
		String filename = configurationService.getInTaskPath() + fileName;
		FileInputStream fis = new FileInputStream(filename);  
		/***
		 * 1.从FileInputStream对象获取文件通道FileChannel  
		 */
		FileChannel channel = fis.getChannel();  
		long size = channel.size();  
		/***
		 * 2.从通道读取文件内容
		 * 
		 * channel.read(ByteBuffer) 方法就类似于 inputstream.read(byte) 
		 * 
		 * 每次read都将读取 allocate 个字节到ByteBuffer  
		 */
		int allocate = 1024;
		byte[] bytes = new byte[allocate];  
		ByteBuffer byteBuffer = ByteBuffer.allocate(allocate); 
		int len; 
		while ((len = channel.read(byteBuffer)) != -1) {
			//注意先调用flip方法反转Buffer,再从Buffer读取数据
			byteBuffer.flip();  
			/***
			 * 有几种方式可以操作ByteBuffer
			 * 
			 * 1.可以将当前Buffer包含的字节数组全部读取出来
			 * bytes = byteBuffer.array();  
			 * System.out.print(new String(bytes));
			 * 
			 * 2.类似与InputStrean的read(byte[],offset,len)方法读取
			 */
			byteBuffer.get(bytes, 0, len);
			/***
			 *  3.也可以遍历Buffer读取每个字节数据
			 *  一个字节一个字节打印在控制台,但这种更慢且耗时
			 *  while(byteBuffer.hasRemaining()) {  
			 *  System.out.print((char)byteBuffer.get()); 
			 *  }
			 */
			byteBuffer.clear(); 
		}
		 // 关闭通道和文件流  
        channel.close();  
        fis.close();  
        JSONObject json = new JSONObject();
        String str = Base64.getEncoder().encodeToString(bytes);
        json.put("data", str);
        json.put("fileName", fileName);
        json.put("lenth", bytes.length);
        File file2 = new File(filename);
		File wfilepath = new File(dirPath);
		if (!wfilepath.exists()) {
			wfilepath.mkdirs();
		}
		while (file2.exists()) {
			File file = new File(dirPath + fileName);
			file2.renameTo(file);
		}
		return json.toString();
        
	}
}
