package gupta.akhil.tools.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {
	private static final Log logger = LogFactory.getLog(FileUtils.class);


	public static void extractToFolder(File file, String targetLocation) throws ZipException, IOException 
	{
	    int BUFFER = 4096;

	    ZipFile zip = new ZipFile(file);

	    new File(targetLocation).mkdirs();
	    Enumeration zipFileEntries = zip.entries();

	    // Process each entry
	    while (zipFileEntries.hasMoreElements())
	    {
	        // grab a zip file entry
	        ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	        String currentEntry = entry.getName();
	        File destFile = new File(targetLocation, currentEntry);
	        //destFile = new File(newPath, destFile.getName());
	        File destinationParent = destFile.getParentFile();

	        // create the parent directory structure if needed
	        destinationParent.mkdirs();

	        if (!entry.isDirectory())
	        {
	            BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
	            int currentByte;
	            // establish buffer for writing file
	            byte data[] = new byte[BUFFER];

	            // write the current file to disk
	            FileOutputStream fos = new FileOutputStream(destFile);
	            BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

	            // read and write until last byte is encountered
	            while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                dest.write(data, 0, currentByte);
	            }
	            dest.flush();
	            dest.close();
	            is.close();
	        }
	    }
	}
	
	public static void  downloadFile(String fileURL, String localFileName, String destinationDir) throws IOException {
	    OutputStream outStream = null;
	    InputStream is = null;
	    try{
	        URL Url = new URL(fileURL);
	        outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + '/' + localFileName));
		    URLConnection  uCon = Url.openConnection();
	        is = uCon.getInputStream();
	        int byteRead=0;
	        byte[] buf = new byte[10000];
	        while ((byteRead = is.read(buf)) != -1) {
	            outStream.write(buf, 0, byteRead);
	        }
	    }
	    finally {
	    	if(is!=null){is.close();}
            if(outStream!=null){outStream.close();};
        }
	}

	public static void downloadFile(String fileURL, String destinationDir) throws IOException{    
	    int slashIndex =fileURL.lastIndexOf('/');
	    int periodIndex =fileURL.lastIndexOf('.');
	    String fileName=fileURL.substring(slashIndex + 1);
	    if (periodIndex >=1 &&  slashIndex >= 0 && slashIndex < fileURL.length()-1){
	        downloadFile(fileURL,fileName,destinationDir);
	    }else{
	        System.err.println("path or file name.");
	    }
	}
	
	public static boolean fileExists(String filePath){
		File file = new File(filePath);
		return file.exists() && file.getTotalSpace() > 0;
	}
	
	public static String toUnixFilePath(String filePath) {
		return filePath.replace('\\', '/');
	}
	
	public static String resolveFileName(String filePath) {
		String fileName = toUnixFilePath(filePath);
		if(filePath.indexOf('/') > -1){
			fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
		}
		logger.trace("File name resolved is - " + fileName);
		return fileName;
	}

	/**
	 * Recursively lists all the files in a given file.
	 * @param filePath
	 * @return
	 */
	public static List<String> listFiles(String filePath) {
		return listFiles(filePath, Collections.EMPTY_LIST);
	}
	
	/**
	 * Recursively lists all the files in a given file.
	 * @param filePath
	 * @return
	 */
	public static List<String> listFiles(String filePath, List<String> ignoredFiles) {
		File file = new File(filePath);
		List<String> files = new ArrayList<String>();
		if(!ignoredFile(toUnixFilePath(file.getAbsolutePath()), ignoredFiles)){
			if(file.isDirectory()){
				for(String fileName : file.list()){
					String childFile = filePath + '/' + fileName; 
					files.addAll(listFiles(childFile, ignoredFiles));
				}
			}else{
				files.add(filePath);
			}
		}
		return files;
	}

	
	private static boolean ignoredFile(String fileAbsolutePath, List<String> ignoredFilePathPatterns) {
		boolean isIgnored=false;
		for(String ignoredFilePathPattern: ignoredFilePathPatterns){
			if(fileAbsolutePath.matches(ignoredFilePathPattern)){
				isIgnored = true;
				break;
			}
		}
		return isIgnored;
	}

	public static void delete(File file) throws IOException{
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}
				file.delete();
	    	}
		}else{
    		file.delete();
    	}
    }
}
