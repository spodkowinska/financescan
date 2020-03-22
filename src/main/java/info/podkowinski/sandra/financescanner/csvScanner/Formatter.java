package info.podkowinski.sandra.financescanner.csvScanner;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Formatter  {

    public Date sqlDate (String formatddMMyyyy) throws ParseException {

        if (formatddMMyyyy.getBytes()[2] == '-')
        {
            SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
            return new Date(fmt.parse(formatddMMyyyy).getTime());
        }
        else if (formatddMMyyyy.getBytes()[4] == '-')
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            return new Date(fmt.parse(formatddMMyyyy).getTime());
        }
        else
            throw new ParseException("Unrecognized date format", 0);

    }

}

