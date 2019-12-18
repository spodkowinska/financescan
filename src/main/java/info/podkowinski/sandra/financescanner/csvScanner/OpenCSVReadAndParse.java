package info.podkowinski.sandra.financescanner.csvScanner;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;


import javax.xml.bind.ValidationException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class OpenCSVReadAndParse {

    public List<List<String>> csvTransactions(String path) throws IOException, ValidationException, CsvValidationException {
//        List<TransactionMBank>transactions = new ArrayList<>();
//        try (
//                Reader reader = Files.newBufferedReader(Paths.get(path))) {
//            CsvToBean<TransactionMBank> csvToBean = new CsvToBeanBuilder(reader)
//                    .withSeparator(';')
//                    .withType(Transaction.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//
//            Iterator<TransactionMBank> csvTransactionIterator = csvToBean.iterator();
//
//            while (csvTransactionIterator.hasNext()) {
//                TransactionMBank transactionMBank = csvTransactionIterator.next();
//                transactions.add(transactionMBank);
//            }
//        }
//        return transactions;
//    }
        List<List<String>> transactions = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();
            CSVReader reader = new CSVReaderBuilder(isr)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();
            List<String> nextLine;
            String[] row;
            while ((row = reader.readNext()) != null) {
                nextLine = Arrays.asList(row);
                transactions.add(nextLine);
            }
            reader.close();
        }
        return transactions;

    }
}
