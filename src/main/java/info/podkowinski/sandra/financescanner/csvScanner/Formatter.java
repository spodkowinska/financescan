package info.podkowinski.sandra.financescanner.csvScanner;

import java.sql.Date;

public class Formatter  {

    public Date sqlDate (String formatddMMyyyy) {
       char[] chars = formatddMMyyyy.toCharArray();
       if(chars.length==10 && (chars[5]>'9'||chars[5]<'0')){
           StringBuilder sb = new StringBuilder();
           sb.append(chars[6]);
           sb.append(chars[7]);
           sb.append(chars[8]);
           sb.append(chars[9]);
           sb.append(chars[5]);
           sb.append(chars[3]);
           sb.append(chars[4]);
           sb.append(chars[2]);
           sb.append(chars[0]);
           sb.append(chars[1]);
           System.out.println(sb.toString());
           return Date.valueOf(sb.toString());
       }
        System.out.println("nie ");
       return Date.valueOf(formatddMMyyyy);
    }

}

