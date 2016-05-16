package gupta.akhil.tools.common.utils;

public class PropertyValueUndefinedException extends RuntimeException {
    public PropertyValueUndefinedException(String property) {
        super("Value undefined for property: " + property);
    }
}



