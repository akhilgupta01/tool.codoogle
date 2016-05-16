package gupta.akhil.tools.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Config {
	private static final PropertyReader propertyReader = PropertyReaderFactory.get("config.properties", true);
	
	public static String getCVSRoot(){
		return propertyReader.getProperty("cvs.root");
	}
	
	public static String getCVSPassword(){ 
		String cvsPasswd = System.getProperty("cvsPasswd");
		if(StringUtils.isBlank(cvsPasswd)){
			cvsPasswd = propertyReader.getProperty("cvsPasswd"); 
		}
		return new Cipher().decrypt(cvsPasswd);
	}
	
	public static String getWLSUser(){
		String wlsUser = System.getProperty("wlsUser");
		if(StringUtils.isBlank(wlsUser)){
			wlsUser = propertyReader.getProperty("wlsUser"); 
		}
		return wlsUser;
	}

	public static String getWLSPassword(){
		String wlsPasswd = System.getProperty("wlsPasswd");
		if(StringUtils.isBlank(wlsPasswd)){
			wlsPasswd = propertyReader.getProperty("wlsPasswd"); 
		}
		return new Cipher().decrypt(wlsPasswd);
	}

	public static String getWorkDirectory(){
		return propertyReader.getProperty("work.folder");
	}

	public static String getRevision(){
		String revision = System.getProperty("checkoutBranch");
		if(StringUtils.isBlank(revision)){
			revision = propertyReader.getProperty("checkoutBranch"); 
		}
		return revision;
	}

	public static String getCheckoutLocation(String revision){
		return getWorkDirectory() + "/" + revision + "/checkout/";
	}

	public static String getDownloadLocation(String revision){
		return getWorkDirectory() + "/" + revision + "/downloads/";
	}

	public static String getIndexLocation(String revision){
		return getWorkDirectory() + "/" + revision + "/indexes/";
	}

	public static String getDefaultVersion() {
		return propertyReader.getProperty("default.version");
	}

	public static String getResourcesDir(){
		return propertyReader.getProperty("teleResourcesDir");
	}

	public static List<String> getIndexedRevisions(){
		String indexedRevisions = propertyReader.getProperty("indexedRevisions");
		if(!StringUtils.isEmpty(indexedRevisions)){
			return Arrays.asList(indexedRevisions.split(","));
		}
		
		return Collections.EMPTY_LIST;
	}
	
	public static List<String> getIgnoredFiles(){
		String indexedFileExtensions = propertyReader.getProperty("ignoredFiles");
		List<String> indexedFileExtensionsList = new ArrayList<String>();
		for(String extn: indexedFileExtensions.split(",")){
			indexedFileExtensionsList.add(extn.trim());
		}
		return indexedFileExtensionsList;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(new String[]{"asd","asdasdd"}));

		//System.out.println(Config.getIndexedFileExtensions());
	}


}
