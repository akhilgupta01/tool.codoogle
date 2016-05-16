package gupta.akhil.tools.common.utils;

import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyReader {
    private Properties props = null;
    private String propertiesName = null;
    private boolean isClassPathResource = true;
    private static Object LOCK = new Object();

    PropertyReader(String propertiesName, boolean isClassPathResource){
    	this.propertiesName = propertiesName;
    	this.isClassPathResource = isClassPathResource;
        loadProperties();
    }


    void loadProperties() {
        if (props == null) {
            synchronized (LOCK) {
                if (props == null) {
                    try {
                        props = new Properties();
                        if(isClassPathResource){
                        	InputStream stream = getClass().getClassLoader().getResourceAsStream(propertiesName);
                            props.load(stream);
                        }else{
                        	FileInputStream fis = new FileInputStream(propertiesName);
                        	props.load(fis);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getProperty(String key) {
        loadProperties();
        return props.getProperty(key);
    }

    public String getProperty(String key, boolean acceptBlank) {
        String value = getProperty(key);

        if (!acceptBlank && StringUtils.isBlank(value)) {
            throw new PropertyValueUndefinedException(key);
        }

        return value;
    }

    public List<String> getPropertyAsList(String key, boolean acceptBlank) {
        String value = getProperty(key, acceptBlank);
        String[] tokens = value.split(",");
        ArrayList<String> list = new ArrayList<String>();

        for (String token : tokens) {
            list.add(token);
        }

        return list;
    }
    
    public List<String> getPropertyKeys() {
        return new ArrayList(props.keySet());
    }

}



