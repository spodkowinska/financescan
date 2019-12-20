package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.csvScanner.Formatter;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public void scanDocument(String path, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator, int skipLines, User user)
            throws IOException, CsvValidationException, ParseException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        List<List<String>> transactions = parser.csvTransactions(path, separator, skipLines);
        for (List<String> trans : transactions) {
            Transaction newTransaction = new Transaction();
            Formatter formatter = new Formatter();
            newTransaction.transactionDate = formatter.sqlDate(trans.get(transactionDatePosition));
            newTransaction.party = trans.get(partyPosition);
            newTransaction.description = trans.get(descriptionPosition);
            newTransaction.amount = Float.parseFloat(trans.get(amountPosition)
                    .replace(',', '.')
                    .replace("\"", "")
                    .replace(" ", ""));
            newTransaction.user = user;
            transactionRepository.save(newTransaction);
        }
    }
//todo keywords not null
    public void assignDefaultCategoriesInTransactions(User user) {
        List<Transaction> transactionList = transactionRepository.findAllByUserId(user.getId());
        for (Transaction transaction : transactionList) {
            for(Category category: categoryRepository.findAllByUser(user)){
                boolean keywordFound = false;
                for (String keyword : category.getKeywords().split(",")) {
                    if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                        transaction.setCategory(category);
                        System.out.println("Found " + transaction.getId() + " Category: " + transaction.getCategory().getName());
                        transactionRepository.save(transaction);
                        keywordFound = true;
                        break;
                    }
                }
                if (keywordFound)
                    break;
            }
        }
    }
}
