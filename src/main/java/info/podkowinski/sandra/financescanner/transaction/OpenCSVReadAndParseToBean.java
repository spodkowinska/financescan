package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OpenCSVReadAndParseToBean {

    public List<Transaction> csvTransactions (String path) throws IOException {
        List<Transaction>transactions = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path))) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder(reader)
                    .withSeparator(';')
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<Transaction> csvTransactionIterator = csvToBean.iterator();

            while (csvTransactionIterator.hasNext()) {
                Transaction transaction = csvTransactionIterator.next();
                transactions.add(transaction);
                System.out.println(transaction.transactionDate);
                System.out.println(transaction.financeDate);
                System.out.println(transaction.party);
                System.out.println(transaction.amount);
                System.out.println(transaction.title);
            }
        }
        return transactions;
    }


}
