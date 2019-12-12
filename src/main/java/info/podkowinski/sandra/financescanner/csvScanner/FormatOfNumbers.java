package info.podkowinski.sandra.financescanner.csvScanner;


import com.opencsv.bean.processor.StringProcessor;


public class FormatOfNumbers implements StringProcessor {
    String defaultValue;

    @Override
    public String processString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        if(value.contains(",")){
           value = value.replace(",",".");
        }
        if(value.contains(" ")) {
            value = value.replace(" ", "");
        }
        return value;
    }

    @Override
    public void setParameterString(String value) {
        defaultValue = value;
    }
}

