package gupta.akhil.tools.indexer.calltrace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractSearcher;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

public class CallTraceSearcher extends AbstractSearcher{
	private static Map<String, CallTraceSearcher> INSTANCE_CACHE = new HashMap<String, CallTraceSearcher>();

	private CallTraceSearcher(String revision){
		super("CallTraceIndex", revision);
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				reOpen();
			}
		}, 10*1000);
	}
	
	public static CallTraceSearcher getInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			createInstance(revision);
		}
		return INSTANCE_CACHE.get(revision);
	}
	
	private synchronized static void createInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			INSTANCE_CACHE.put(revision, new CallTraceSearcher(revision));
		}
	}
	
	public List<CallTrace> findCallTrace(String entryMethod) {
		List<CallTrace> callTraces = new ArrayList<CallTrace>();
		try {
			List<Document> searchResult = search(CallTraceDocument.ENTRY_POINT + ":" + entryMethod);
			for(Document document:searchResult){
				callTraces.add(new CallTraceDocument(document).getCallTrace());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return callTraces;
	}
	
	public CallTrace getCallTraceByKey(String key) {
		CallTrace result = null;
		try {
			Document document = getDocumentByKey(key);
			if(document != null){
				result = new CallTraceDocument(document).getCallTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<CallTrace> findCallsByServer(String serverCluster) {
		List<CallTrace> callTraces = new ArrayList<CallTrace>();
		try {
			List<Document> searchResult = search(CallTraceDocument.SERVER_CLUSTER + ":" + serverCluster);
			for(Document document:searchResult){
				callTraces.add(new CallTraceDocument(document).getCallTrace());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return callTraces;
	}
	
	public List<CallTrace> findCallTraceContaining(String searchTerm) {
		List<CallTrace> callTraces = new ArrayList<CallTrace>();
		try {
			List<Document> searchResult = search(CallTraceDocument.CALL_TRACE + ":" + searchTerm);
			for(Document document:searchResult){
				callTraces.add(new CallTraceDocument(document).getCallTrace());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return callTraces;
	}

	@Override
	protected String[] getFieldNames() {
		// TODO Auto-generated method stub
		return new String[]{CallTraceDocument.CALL_TRACE,CallTraceDocument.CALL_TYPE,CallTraceDocument.ENTRY_POINT,CallTraceDocument.LAST_OBSERVATION_TIME,CallTraceDocument.SERVER_CLUSTER,CallTraceDocument.SNAPSHOT_COUNT};
	}
}
