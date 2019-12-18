package info.podkowinski.sandra.financescanner.csvScanner;

import java.sql.Date;

public class Formatter  {

    public static Date sqlDate (String formatddMMyyyy) {
       char[] chars = formatddMMyyyy.trim().toCharArray();
       if(chars.length==10){
           StringBuilder sb = new StringBuilder();
           for(int i=7; i<chars.length; i++){
               sb.append(i);
           }
           sb.append(chars[6]);
           sb.append(chars[4]);
           sb.append(chars[5]);
           sb.append(chars[3]);
           sb.append(chars[1]);
           sb.append(chars[2]);
           return sqlDate(sb.toString());
       }
       return sqlDate(formatddMMyyyy);
    }

}

