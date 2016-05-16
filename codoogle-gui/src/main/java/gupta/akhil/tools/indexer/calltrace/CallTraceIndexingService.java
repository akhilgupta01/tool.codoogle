package gupta.akhil.tools.indexer.calltrace;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.indexer.calltrace.CallTraceGsonBuilder;
import gupta.akhil.tools.indexer.calltrace.CallTraceSearcher;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

@Path("calltrace")
public class CallTraceIndexingService {
	private static final Log logger = LogFactory.getLog(CallTraceIndexingService.class);
	
	public static void main(String[] args) {
		CallTraceIndexingService service = new CallTraceIndexingService();
	}

	@GET
	@Path("/byContent/{revision}/{searchTerm}")
	@Produces({"application/json"})
	public String searchCallTracesContaining(@PathParam("revision") String revision, 
											 @PathParam("searchTerm") String searchTerm){
		String result = "";
		try{
			List<CallTrace> callTracesFound = CallTraceSearcher.getInstance(revision).findCallTraceContaining(searchTerm);
			result = CallTraceGsonBuilder.tracesToJSON(callTracesFound);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}


	@GET
	@Path("/byServer/{revision}/{serverCluster}")
	@Produces({"application/json"})
	public String searchCallTracesByServer(@PathParam("revision") String revision,
											@PathParam("serverCluster") String serverCluster){
		String result = "";
		try{
			List<CallTrace> callTracesFound = CallTraceSearcher.getInstance(revision).findCallsByServer(serverCluster);
			result = CallTraceGsonBuilder.tracesToJSON(callTracesFound);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}
	
	@GET
	@Path("/{revision}/{callTraceId}")
	@Produces({"application/json"})
	public String getFullCallTrace(@PathParam("revision") String revision,
									@PathParam("callTraceId") String callTraceId){
		String result = "";
		try{
			CallTrace callTrace = CallTraceSearcher.getInstance(revision).getCallTraceByKey(callTraceId);
			result = CallTraceGsonBuilder.traceToJSON(callTrace, true);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}
	
}