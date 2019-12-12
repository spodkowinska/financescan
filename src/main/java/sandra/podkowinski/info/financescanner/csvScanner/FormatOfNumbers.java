package sandra.podkowinski.info.financescanner.csvScanner;

import com.opencsv.bean.AbstractBeanField;


public class FormatOfNumbers extends AbstractBeanField<String> {
    String defaultValue=" ";

    @Override
    public String convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        if(value.contains(",")){
            value.replace(",",".");
        }
        if(value.contains(" ")) {
            value.replace(" ", "");
        }
        return value;
    }


}

