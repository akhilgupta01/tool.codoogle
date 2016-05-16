package gupta.akhil.tools.wls.tda;

import gupta.akhil.tools.common.config.SystemConfig;

public class StackTraceFilter {
	
	public static boolean allow(String line) {
		boolean allowed = false;
		for(String allowedPattern: SystemConfig.getThreadDumpConfig().getAllowedPatterns()){
			if(line.matches(allowedPattern)){
				allowed = true;
				break;
			}
		}
		if(allowed){
			for(String ignoredPattern: SystemConfig.getThreadDumpConfig().getExcludedPatterns()){
				if(line.matches(ignoredPattern)){
					allowed = false;
					break;
				}
			}
		}
		return allowed;
	}
}
