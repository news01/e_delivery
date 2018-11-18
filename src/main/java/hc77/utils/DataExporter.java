package hc77.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DataExporter {
	/*
	 * strExportPath：保存导出文件的绝对路径
	 * strNamePrefix：导出文件的前缀
	 */
	private final String strExportPath, strNamePrefix;
	
	public DataExporter(String strExportPath, String strNamePrefix) {
		this.strExportPath = strExportPath;
		this.strNamePrefix = strNamePrefix;
	}
	
	/*
	 * obj：待导出的对象
	 */
	public void export(Object obj) {
		// 导出文件名，为"导出目录/前缀-不含分隔符的UUID.json"
		Path pathExportFile = Paths.get(strExportPath, strNamePrefix + "-" + UUID.randomUUID().toString().replace("-", "") + ".json");
		
		try {
			// 若导出目录不存在，则创建导出目录
			File dirExport = pathExportFile.getParent().toFile();
			if (!dirExport.exists())
				dirExport.mkdirs();
			
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.writeValue(pathExportFile.toFile(), obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}