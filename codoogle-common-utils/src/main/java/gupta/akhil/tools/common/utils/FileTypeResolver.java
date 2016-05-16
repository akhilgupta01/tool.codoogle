package gupta.akhil.tools.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileTypeResolver {
	private static final Log logger = LogFactory.getLog(FileTypeResolver.class);

	private static final Map<String, Boolean> FILE_TYPE_TABLE = new HashMap<String, Boolean>();
	
	private static FileTypeResolver INSTANCE;
	
	private FileTypeResolver(){
		//Singleton
	}
	
	public static final FileTypeResolver getInstance(){
		if(INSTANCE == null){
			createInstance();
		}
		return INSTANCE;
	}
	
	
	private static synchronized void createInstance() {
		if(INSTANCE == null){
			INSTANCE = new FileTypeResolver();
			FILE_TYPE_TABLE.put(".jar", Boolean.FALSE);
			FILE_TYPE_TABLE.put(".war", Boolean.FALSE);
			FILE_TYPE_TABLE.put(".ear", Boolean.FALSE);
			FILE_TYPE_TABLE.put(".tar", Boolean.FALSE);
			FILE_TYPE_TABLE.put(".zip", Boolean.FALSE);
		}
	}


	public boolean isAsciiFile(File file) {
		Boolean isAscii = false;
		String fileExtn = getFileExtn(file);
		if(file.isFile() && (isAscii = FILE_TYPE_TABLE.get(fileExtn)) == null){
			logger.debug("Checking file extension - '" + fileExtn + "'");
			isAscii = checkIfAscii(file);
			logger.debug("isAscii = " + isAscii);
			FILE_TYPE_TABLE.put(fileExtn, Boolean.valueOf(isAscii));
		}
		return isAscii;
	}
	
	private String getFileExtn(File file){
		String fileName = file.getName();
		String fileExtn = null;
		if(StringUtils.isNotBlank(fileName)){
			int extIndex = fileName.lastIndexOf(".");
			if(extIndex > 0){
				fileExtn = fileName.substring(extIndex);
			}
		}
		return fileExtn;
	}
	
	private boolean checkIfAscii(File file)  {
		boolean isAscii = true;
		try {
			FileInputStream is = new FileInputStream(file);
			int counter=0;
			while(counter<20 && isAscii){
				int charRead = is.read();
				counter++;
				if(charRead > 127){
					isAscii = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isAscii = false;
		}
		return isAscii;
	}
}
