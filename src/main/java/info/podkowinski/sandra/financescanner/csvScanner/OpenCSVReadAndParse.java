package info.podkowinski.sandra.financescanner.csvScanner;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;


import javax.xml.bind.ValidationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class OpenCSVReadAndParse {

    public List<List<String>> csvTransactions(InputStream inputStream, char separator, int skipLines) throws IOException, CsvValidationException {
        List<List<String>> transactions = new ArrayList<>();
        try (InputStream fis = inputStream;
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            CSVParser parser;
            if (separator == ',') {
                parser = new CSVParserBuilder()
                        .build();
            } else {
                parser = new CSVParserBuilder()
                        .withSeparator(separator)
                        .build();
            }
            CSVReader reader = new CSVReaderBuilder(isr)
                    .withSkipLines(skipLines)
                    .withCSVParser(parser)
                    .build();
            List<String> nextLine;
            String[] row;

            while ((row = reader.readNext()) != null && !row[0].isEmpty()) {
                nextLine = Arrays.asList(row);
                transactions.add(nextLine);
            }


            reader.close();
        }
        return transactions;

    }
}
