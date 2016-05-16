package gupta.akhil.tools.indexer.calltrace;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import gupta.akhil.tools.indexer.calltrace.CallTraceDocument;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

public class CallTraceGsonBuilder {
	
	public static String traceDocsToJSON(List<CallTraceDocument> callTraces){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CallTraceDocument.class, JSON_ADAPTER_LITE);
		final Gson gson = gsonBuilder.create();
		return gson.toJson(callTraces);
	}

	public static String tracesToJSON(List<CallTrace> callTraces){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CallTrace.class, CALL_TRACE_JSON_ADAPTER);
		final Gson gson = gsonBuilder.create();
		return gson.toJson(callTraces);
	}

	public static String traceToJSON(CallTrace callTrace){
		return traceToJSON(callTrace, false);
	}

	public static String traceToJSON(CallTrace callTrace, boolean complete){
		GsonBuilder gsonBuilder = new GsonBuilder();
		if(complete){
			gsonBuilder.registerTypeAdapter(CallTrace.class, CALL_TRACE_JSON_ADAPTER_FULL);
		}else{
			gsonBuilder.registerTypeAdapter(CallTrace.class, CALL_TRACE_JSON_ADAPTER);
		}
		final Gson gson = gsonBuilder.create();
		return gson.toJson(callTrace);
	}

	public static String traceDocToJSON(CallTraceDocument callTrace){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CallTraceDocument.class, JSON_ADAPTER_LITE);
		final Gson gson = gsonBuilder.create();
		return gson.toJson(callTrace);
	}

	
	private static final JsonSerializer<CallTraceDocument> JSON_ADAPTER_LITE = new JsonSerializer<CallTraceDocument>() {
		@Override
		public JsonElement serialize(CallTraceDocument callTraceDocument, Type arg1, JsonSerializationContext arg2) {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", callTraceDocument.getKey());
	        jsonObject.addProperty("serverCluster", callTraceDocument.getServerCluster());
	        jsonObject.addProperty("entryPoint", callTraceDocument.getEntryPoint());
	        jsonObject.addProperty("callType", callTraceDocument.getCallType());
	        jsonObject.addProperty("snapshotCount", callTraceDocument.getSnapshotCount());
	        jsonObject.addProperty("lastObservationTime", callTraceDocument.getLastObservationTime());
	        return jsonObject;
		}
	};
	
	private static final JsonSerializer<CallTrace> CALL_TRACE_JSON_ADAPTER = new JsonSerializer<CallTrace>() {
		@Override
		public JsonElement serialize(CallTrace callTrace, Type arg1, JsonSerializationContext arg2) {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", callTrace.getIndexKey());
	        jsonObject.addProperty("serverCluster", callTrace.getServerCluster());
	        jsonObject.addProperty("entryPoint", callTrace.getEntryPoint());
	        jsonObject.addProperty("callType", callTrace.getCallType());
	        jsonObject.addProperty("snapshotCount", String.valueOf(callTrace.getSnapshotCount()));
	        if(callTrace.getLastObservationTime() != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        jsonObject.addProperty("lastObservationTime", dateFormat.format(callTrace.getLastObservationTime()));
			}else{
				jsonObject.addProperty("lastObservationTime", "N/A");
			}
	        return jsonObject;
		}
	};
	
	
	private static final JsonSerializer<CallTrace> CALL_TRACE_JSON_ADAPTER_FULL = new JsonSerializer<CallTrace>() {
		@Override
		public JsonElement serialize(CallTrace callTrace, Type arg1, JsonSerializationContext arg2) {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", callTrace.getIndexKey());
	        jsonObject.addProperty("serverCluster", callTrace.getServerCluster());
	        jsonObject.addProperty("entryPoint", callTrace.getEntryPoint());
	        jsonObject.addProperty("callType", callTrace.getCallType());
	        jsonObject.addProperty("snapshotCount", String.valueOf(callTrace.getSnapshotCount()));
	        if(callTrace.getLastObservationTime() != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        jsonObject.addProperty("lastObservationTime", dateFormat.format(callTrace.getLastObservationTime()));
			}else{
				jsonObject.addProperty("lastObservationTime", "N/A");
			}
	        jsonObject.addProperty("callGraph", HTMLPrettyPrinter.getPrettyHTMLTree(callTrace.getMergedCallGraph()));
	        return jsonObject;
		}
	};

}
