package info.podkowinski.sandra.financescanner.transaction;

import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParseToBean;
import info.podkowinski.sandra.financescanner.csvScanner.TransactionMBank;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void scanDocument(String path) throws IOException {
        OpenCSVReadAndParseToBean parser = new OpenCSVReadAndParseToBean();
        List<TransactionMBank> transactions = parser.csvTransactions(path);
        transactions.forEach(t -> this.saveTransaction(t));
    }


    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }



}
