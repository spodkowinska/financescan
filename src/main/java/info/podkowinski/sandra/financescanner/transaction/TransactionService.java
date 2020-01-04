package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.csvScanner.Formatter;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public void scanDocument(InputStream inputStream, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator, int skipLines, String importName, User user)
            throws IOException, CsvValidationException, ParseException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        List<List<String>> transactions = parser.csvTransactions(inputStream, separator, skipLines);
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
            newTransaction.importName = importName;
            newTransaction.user = user;
            transactionRepository.save(newTransaction);
        }
    }

    public List <Transaction> findByUsersId(long id){
       return transactionRepository.findAllByUserId(id);
    }

    public void assignDefaultCategoriesInTransactions(User user) {
        List<Transaction> transactionList = transactionRepository.findAllByUserId(user.getId());
        for (Transaction transaction : transactionList) {
            for (Category category : categoryRepository.findAllByUser(user)) {
                boolean keywordFound = false;
                for (String keyword : category.getKeywords().split(",")) {
                    if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase().trim())) {
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

    //todo what to do if transaction doesn't exist, itp?
    public void assignCategoryInTransaction(User user, Long transactionId, Long categoryId) {
        Transaction transaction = transactionRepository.getOne(transactionId);
        if (transaction.getUser().equals(user)) {
            transaction.setCategory(categoryRepository.getOne(categoryId));
            transactionRepository.save(transaction);
        }
    }

    public double balanceByDates(User user, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(start, end, user);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByDatesAndCategory(User user, Date start, Date end, Long categoryId) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateAfterAndTransactionDateBeforeAndUserAndCategory
                (start, end, user, categoryRepository.getOne(categoryId));
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public HashMap<String, Double> balancesByDatesForAllCategories(User user, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(start, end, user);
        HashMap<String, Double> balanceByCategory = new HashMap<>();
        for (Category category : categoryRepository.findAllByUser(user)) {
            Double categorySum = 0.0;
            for (Transaction transaction : transactionList) {
                if (transaction.getCategory() == category) {
                    categorySum += transaction.amount;
                }
            }

            balanceByCategory.put(category.getName(), categorySum);
        }
        Double sum = 0.0;
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory() == null) {
                sum += transaction.amount;
            }
        }
        balanceByCategory.put("Bez kategorii", sum);
        return balanceByCategory;
    }
}
