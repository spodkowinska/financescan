package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.bank.BankRepository;
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
import java.util.*;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final BankRepository bankRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository, BankRepository bankRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.bankRepository = bankRepository;
    }

    public void scanDocument(InputStream inputStream, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator, int skipLines, String importName, Long bankId, User user)
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
            newTransaction.bank = bankRepository.getOne(bankId);
            newTransaction.user = user;
            transactionRepository.save(newTransaction);
        }
    }

    public List<Transaction> findByUsersId(long id) {
        return transactionRepository.findAllByUserId(id);
    }

    public void assignDefaultCategoriesInTransactions(User user) {
        List<Transaction> transactionList = transactionRepository.findAllByUserId(user.getId());
        for (Transaction transaction : transactionList) {
            for (Category category : categoryRepository.findAllByUserId(user.getId())) {
                boolean keywordFound = false;
                for (String keyword : category.getKeywords().split(",")) {
                    if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase().trim())) {
                        transaction.setCategories(Arrays.asList(category));
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
            transaction.setCategories(Arrays.asList(categoryRepository.getOne(categoryId)));
            transactionRepository.save(transaction);
        }
    }

    public double balanceByDates(User user, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(start, end, user);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByDatesAndCategory(User user, Date start, Date end, Long categoryId) {
        List<Transaction> transactionList = transactionRepository.findByDateAndUserAndCategory
                (start, end, user, categoryRepository.getOne(categoryId));
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public HashMap<String, Double> balancesByDatesForAllCategories(User user, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(start, end, user);
        HashMap<String, Double> balanceByCategory = new HashMap<>();
        for (Category category : categoryRepository.findAllByUserId(user.getId())) {
            Double categorySum = 0.0;
            for (Transaction transaction : transactionList) {
                if (transaction.getCategories().contains(category)) {
                    categorySum += transaction.amount;
                }
            }

            balanceByCategory.put(category.getName(), categorySum);
        }
        Double sum = 0.0;
        for (Transaction transaction : transactionList) {
            if (transaction.getCategories() == null) {
                sum += transaction.amount;
            }
        }
        balanceByCategory.put("Bez kategorii", sum);
        return balanceByCategory;
    }

    public HashMap<Long, List<String>> transactionIdCategories(Long userId) {
        HashMap<Long, List<String>> transactionIdCategories = new HashMap<>();
        List<Transaction> transactionsList = findByUsersId(userId);
        for (Transaction t : transactionsList) {
            List<String>categoriesNames=new ArrayList<>();
            t.getCategories().forEach(c->categoriesNames.add(c.getName()));
            transactionIdCategories.put(t.getId(),categoriesNames);
        }
        return transactionIdCategories;
    }
}
