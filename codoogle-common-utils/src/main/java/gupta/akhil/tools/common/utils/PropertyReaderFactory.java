package gupta.akhil.tools.common.utils;

import java.util.HashMap;
import java.util.Map;

public class PropertyReaderFactory {
    private static final Map<String, PropertyReader> PROPERTY_READER_MAP = new HashMap<String, PropertyReader>();

    public static final PropertyReader get(String propertiesName, boolean isClassPathResource) {
    	PropertyReader reader = PROPERTY_READER_MAP.get(propertiesName);
    	if(reader == null){
    		synchronized (PROPERTY_READER_MAP) {
				if(reader == null){
		    		reader = new PropertyReader(propertiesName, isClassPathResource);
		    		PROPERTY_READER_MAP.put(propertiesName, reader);
				}
			}
    	}
        return reader;
    }
    
    public static final PropertyReader get(String propertiesName) {
        return get(propertiesName, true);
    }
}
