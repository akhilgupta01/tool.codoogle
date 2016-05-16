package gupta.akhil.tools.common.utils;

public class ClassFilter {
	
	private static final String[] ignoredPackages = new String[]{
		"java.", 
		"org.", 
		"com.",
		"javax.",
		"schemacom_bea_xml", 
		"xmlns.soa_model"};
	
	public static boolean ignore(String className){
		boolean ignore = false;
		for(String ignoredPackage: ignoredPackages){
			if(className.contains(ignoredPackage)){
				ignore=true;
				break;
			}
		}
		return ignore;
	}
}
