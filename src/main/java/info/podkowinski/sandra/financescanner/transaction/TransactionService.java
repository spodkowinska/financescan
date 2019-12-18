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


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void scanDocument(String path, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator)
            throws IOException, CsvValidationException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        List<List<String>> transactions = parser.csvTransactions(path, separator);
        for (List<String> trans : transactions) {
            Transaction newTransaction = new Transaction();
            newTransaction.transactionDate = trans.get(transactionDatePosition);
            newTransaction.party = trans.get(partyPosition);
            newTransaction.description = trans.get(descriptionPosition);
            newTransaction.amount = Float.parseFloat(trans.get(amountPosition)
                    .replace(',', '.')
                    .replace("\"","")
                    .replace(" ", ""));
            transactionRepository.save(newTransaction);
        }
    }


}
