package gupta.akhil.tools.indexer.calltrace;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.lucene.document.Document;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractDocument;
import gupta.akhil.tools.codeanalyzer.lucene.Indexable;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

public class CallTraceDocument extends AbstractDocument {
	public static final String SERVER_CLUSTER = "serverCluster";
	public static final String ENTRY_POINT = "entryPoint";
	public static final String CALL_TYPE = "callType";
	public static final String CALL_TRACE = "callTrace";
	public static final String SNAPSHOT_COUNT = "snapshotCount";
	public static final String LAST_OBSERVATION_TIME = "lastObservationTime";

	public CallTraceDocument(Document document){
		super(document);
	}

	public CallTraceDocument(CallTrace callTrace){
		super(callTrace);
	}
	
	public String getServerCluster() {
		return getDocument().get(SERVER_CLUSTER);
	}
	
	public String getEntryPoint() {
		return getDocument().get(ENTRY_POINT);
	}
	
	public String getCallType() {
		return getDocument().get(CALL_TYPE);
	}
	
	public String getSnapshotCount() {
		return getDocument().get(SNAPSHOT_COUNT);
	}
	
	public String getLastObservationTime() {
		return getDocument().get(LAST_OBSERVATION_TIME);
	}
	
	@Override
	protected void buildDocument(Indexable indexable) {
		CallTrace callTrace = (CallTrace)indexable;
		addTextField(SERVER_CLUSTER, callTrace.getServerCluster());
		addTextField(ENTRY_POINT, callTrace.getEntryPoint());
		addTextField(CALL_TRACE, callTrace.getMergedCallGraph().getCallGraph());
		addTextField(CALL_TYPE, callTrace.getCallType());
		addTextField(SNAPSHOT_COUNT, String.valueOf(callTrace.getSnapshotCount()));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		addStringField(LAST_OBSERVATION_TIME, dateFormat.format(callTrace.getLastObservationTime()));
	}
	
	@Override
	protected Indexable toIndexable(Document document) {
		CallTrace callTrace = new CallTrace(
				document.get(SERVER_CLUSTER), 
				document.get(ENTRY_POINT),
				document.get(CALL_TYPE),
				MergedCallGraph.parse(document.get(CALL_TRACE)));
		try {
			if(document.get(SNAPSHOT_COUNT) != null){
				callTrace.setSnapshotCount(Long.valueOf(document.get(SNAPSHOT_COUNT)));
			}
			if(document.get(LAST_OBSERVATION_TIME) != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				callTrace.setLastObservationTime(dateFormat.parse(document.get(LAST_OBSERVATION_TIME)));
			}
		} catch (ParseException e) {
			//ignore
		}
		return callTrace;
	}
	
	public CallTrace getCallTrace(){
		return (CallTrace)getIndexable();
	}
	
	@Override
	public String getKey() {
		return getCallTrace().getIndexKey(); 
	}
}
