package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public int transactionDatePosition = 0;

    public static int descriptionPosition = 3;

    public static int partyPosition = 4;

    public static int amountPosition = 6;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void scanDocument(String path) throws IOException, ValidationException, CsvValidationException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        List<List<String>> transactions = parser.csvTransactions(path);
        for (List<String> trans :transactions) {
            Transaction newTransaction = new Transaction();
            newTransaction.transactionDate = trans.get(transactionDatePosition);
            newTransaction.party = trans.get(partyPosition);
            newTransaction.description = trans.get(descriptionPosition);
            newTransaction.amount = Float.parseFloat(trans.get(amountPosition).replace(',','.')
                    .replace(" ", ""));
            transactionRepository.save(newTransaction);
        }
    }


    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }



}
