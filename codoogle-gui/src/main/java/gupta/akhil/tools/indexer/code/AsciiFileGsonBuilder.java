package gupta.akhil.tools.indexer.code;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import gupta.akhil.tools.indexer.code.model.AsciiFile;

public class AsciiFileGsonBuilder {
	
	public static String toJsonLite(List<AsciiFile> asciiFiles){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(AsciiFile.class, JSON_ADAPTER_LITE);
		final Gson gson = gsonBuilder.create();
		return gson.toJson(asciiFiles);
	}
	
	public static String toJsonLite(AsciiFile asciiFiles){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(AsciiFile.class, JSON_ADAPTER_LITE);
		final Gson gson = gsonBuilder.create();
		return gson.toJson(asciiFiles);
	}

	
	private static final JsonSerializer<AsciiFile> JSON_ADAPTER_LITE = new JsonSerializer<AsciiFile>() {
		@Override
		public JsonElement serialize(AsciiFile asciiFile, Type arg1, JsonSerializationContext arg2) {
			final JsonObject jsonObject = new JsonObject();
	        jsonObject.addProperty("id", asciiFile.getIndexKey());
	        jsonObject.addProperty("filePath", asciiFile.getFilePath());
	        jsonObject.addProperty("fileName", asciiFile.getFileName());
	        jsonObject.addProperty("moduleName", asciiFile.getModuleName());
	        return jsonObject;
		}
	};
}
