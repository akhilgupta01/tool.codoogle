package gupta.akhil.tools.indexer.calltrace;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractIndexer;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

public class CallTraceIndexer extends AbstractIndexer{
	private static final Log logger = LogFactory.getLog(CallTraceIndexer.class);

	private static Map<String, CallTraceIndexer> INSTANCE_CACHE = new HashMap<String, CallTraceIndexer>();

	private CallTraceIndexer(String revision){
		super("CallTraceIndex", revision);
	}
	
	public static CallTraceIndexer getInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			createInstance(revision);
		}
		return INSTANCE_CACHE.get(revision);
	}
	
	private synchronized static void createInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			INSTANCE_CACHE.put(revision, new CallTraceIndexer(revision));
		}
	}
	
	public void addToIndex(CallTrace callTrace) throws IOException{
		logger.debug("Indexing CallTrace [serverCluster=" + callTrace.getServerCluster() + ", entryPoint=" + callTrace.getEntryPoint() + "]");
		updateDocument(new CallTraceDocument(callTrace));
		commit();
	}
}
