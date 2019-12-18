package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.csvScanner.Formatter;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void scanDocument(String path, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator, int skipLines)
            throws IOException, CsvValidationException, ParseException{
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        List<List<String>> transactions = parser.csvTransactions(path, separator, skipLines);
        for (List<String> trans : transactions) {
            Transaction newTransaction = new Transaction();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            newTransaction.transactionDate = Formatter.sqlDate(trans.get(transactionDatePosition));
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
