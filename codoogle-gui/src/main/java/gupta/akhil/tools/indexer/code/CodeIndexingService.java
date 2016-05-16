package gupta.akhil.tools.indexer.code;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import gupta.akhil.tools.indexer.code.AsciiFileGsonBuilder;
import gupta.akhil.tools.indexer.code.AsciiFileSearcher;
import gupta.akhil.tools.indexer.code.model.AsciiFile;

@Path("searcher")
public class CodeIndexingService {
	private static final Log logger = LogFactory.getLog(CodeIndexingService.class);

	@GET
	@Path("/artifact/byName/{revision}/{searchTerm}")
	@Produces({"application/json"})
	public String searchArtifactByName(@PathParam("revision") String revision,
										@PathParam("searchTerm") String searchTerm){
		String result = "";
		try{
			List<AsciiFile> filesFound = AsciiFileSearcher.getInstance(revision).searchFilesByName(searchTerm);
			result = AsciiFileGsonBuilder.toJsonLite(filesFound);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}

	@GET
	@Path("/artifact/byQuery/{revision}/{searchQuery}")
	@Produces({"application/json"})
	public String searchArtifactByAnyField(@PathParam("revision") String revision,
										@PathParam("searchQuery") String searchQuery){
		String result = "";
		try{
			List<AsciiFile> filesFound = AsciiFileSearcher.getInstance(revision).searchFiles(searchQuery);
			result = AsciiFileGsonBuilder.toJsonLite(filesFound);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}

	@GET
	@Path("/artifact/byContent/{revision}/{searchTerm}")
	@Produces({"application/json"})
	public String searchArtifactByContent(@PathParam("revision") String revision,
											@PathParam("searchTerm") String searchTerm){
		String result = "";
		try{
			List<AsciiFile> filesFound = AsciiFileSearcher.getInstance(revision).searchFilesByContent(searchTerm);
			result = AsciiFileGsonBuilder.toJsonLite(filesFound);
		}catch(Exception e){
			
			result = "Error - " + e.getMessage();
		}
		return result;
	}
	
	@GET
	@Path("/suggest/fileName/{revision}/")
	@Produces({"application/json"})
	public String suggestFileName(@PathParam("revision") String revision,
										@QueryParam("term") String searchTerm){
		String result = "";
		try{
			List<AsciiFile> filesFound = AsciiFileSearcher.getInstance(revision).searchFilesByName(searchTerm + "*");
			List<String> suggestedFileNames = new ArrayList<String>();
			for(AsciiFile asciiFile: filesFound){
				suggestedFileNames.add(asciiFile.getFileName());
			}
			Gson gson = new Gson();
			result = gson.toJson(suggestedFileNames);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}

	
	@GET
	@Path("/artifact/{revision}/{id}")
	@Produces({"application/json"})
	public String getArtifact(@PathParam("revision") String revision, @PathParam("id") String id){
		String result = "";
		try{
			AsciiFile filesFound = AsciiFileSearcher.getInstance(revision).getFile(id);
			Gson gson = new Gson();
			result = gson.toJson(filesFound);
		}catch(Exception e){
			result = "Error - " + e.getMessage();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		CodeIndexingService service = new CodeIndexingService();
		System.out.println(service.searchArtifactByAnyField("branch_rel_16_10", "moduleName:comm*"));
	}
}