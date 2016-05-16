package gupta.akhil.tools.wls.tda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ThreadDumpParser {
	public static List<ThreadSnapshot> parse(String serverName, String threadDump) {
		return parse(serverName, threadDump, false);
	}

	public static List<ThreadSnapshot> parse(String serverName, String threadDump, boolean stripSourceInfo) {
		BufferedReader br = new BufferedReader(new StringReader(threadDump));
		Stack<String> callStack = new Stack<String>();
		List<ThreadSnapshot> threadSnapshots = new ArrayList<ThreadSnapshot>();
		String line = null;
		try {
			while((line = br.readLine())!=null){
				line = line.trim();
				if(StackTraceFilter.allow(line)){
					if(stripSourceInfo){
						line = stripSourceInfo(line);
						//line = maskCode(line);
					}
					callStack.push(line);
				}else if(reachedEndOfThread(line)){
					if(callStack.size() > 1){
						ThreadSnapshot threadSnapshot = ThreadSnapshotBuilder.buildThreadSnapshot(serverName, callStack);
						if(threadSnapshot.getEntryPoint() != null){
							threadSnapshots.add(threadSnapshot);
						}else{
							//System.out.println("Dropping threaddump - "  + threadSnapshot);
						}
					}
					callStack.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return threadSnapshots;
	}

	private static String stripSourceInfo(String callTraceLine) {
		int indexOfSourceInfo = callTraceLine.indexOf('(');
		if(indexOfSourceInfo > 0){
			return callTraceLine.substring(0, indexOfSourceInfo);
		}
		return callTraceLine;
	}
	
	private static boolean reachedEndOfThread(String line) {
		return line.trim().isEmpty();
	}
}
