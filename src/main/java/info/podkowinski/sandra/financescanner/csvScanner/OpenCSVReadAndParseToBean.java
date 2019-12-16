package info.podkowinski.sandra.financescanner.csvScanner;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import info.podkowinski.sandra.financescanner.transaction.Transaction;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OpenCSVReadAndParseToBean {

    public List<TransactionMBank> csvTransactions (String path) throws IOException {
        List<TransactionMBank>transactions = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path))) {
            CsvToBean<TransactionMBank> csvToBean = new CsvToBeanBuilder(reader)
                    .withSeparator(';')
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<TransactionMBank> csvTransactionIterator = csvToBean.iterator();

            while (csvTransactionIterator.hasNext()) {
                TransactionMBank transactionMBank = csvTransactionIterator.next();
                transactions.add(transactionMBank);
                System.out.println(transactionMBank.transactionDate);
                System.out.println(transactionMBank.financeDate);
                System.out.println(transactionMBank.party);
                System.out.println(transactionMBank.amount);
                System.out.println(transactionMBank.title);
            }
        }
        return transactions;
    }


}
