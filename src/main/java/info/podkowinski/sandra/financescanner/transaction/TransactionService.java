package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.account.AccountRepository;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserRepository;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository,
                              AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Set<Category> categoriesFromUrlString(String categoriesIds) {
        String[] categoriesIdsList = categoriesIds.split(",");
        Set<Category> categoriesList = new HashSet<>();
        for (String id : categoriesIdsList) {
            categoriesList.add(categoryRepository.getOne(Long.parseLong(id)));
        }
        return categoriesList;
    }

    public void scanDocument(InputStream inputStream, int transactionDatePosition, int descriptionPosition, int partyPosition, int amountPosition, char separator, int skipLines, String importName, Long bankId, User user)
            throws IOException, CsvValidationException, ParseException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        // todo fix this ugly hack (detecting mBank) to pass encoding
        String inputCharset = bankId == 5 ? "Cp1250" : "UTF-8";
        List<List<String>> transactions = parser.csvTransactions(inputStream, separator, skipLines, inputCharset);
        System.out.println(bankId);
        for (List<String> trans : transactions) {
            Transaction newTransaction = new Transaction();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // todo fix this ugly hack (detecting santander) to parse Date
            if (bankId == 1l) {
                newTransaction.transactionDate = LocalDate.parse(trans.get(transactionDatePosition), formatter);
            } else {
                newTransaction.transactionDate = LocalDate.parse(trans.get(transactionDatePosition));
            }
            newTransaction.party = trans.get(partyPosition);
            newTransaction.description = trans.get(descriptionPosition);
            //todo fix getting amount in Millennium two column version
            if (trans.get(amountPosition).isEmpty()) {
                newTransaction.amount = Float.parseFloat(trans.get(amountPosition + 1)
                        .replace(',', '.')
                        .replace("\"", "")
                        .replace(" ", ""));
            } else {
                newTransaction.amount = Float.parseFloat(trans.get(amountPosition)
                        .replace(',', '.')
                        .replace("\"", "")
                        .replace(" ", ""));
            }
            newTransaction.importName = importName;
            newTransaction.account = accountRepository.getOne(bankId);
            newTransaction.user = user;
            transactionRepository.save(newTransaction);
        }
    }

    public List<Transaction> findByUserId(long id) {
        return transactionRepository.findAllByUserIdOrderByTransactionDateAsc(id);
    }

    public void assignDefaultCategoriesInTransactions(User user) {
        User user1 = userRepository.getOne(2l);
        List<Transaction> transactionIdList = transactionRepository.findAllByUserId(2l);
        for (Transaction transaction : transactionIdList) {
            for (Category category : categoryRepository.findAllByUserId(user.getId())) {
//                boolean keywordFound = false;
                for (String keyword : category.getKeywords()) {
                    if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase().trim())) {
                        Set<Category> pendingCategories = transaction.pendingCategories;
                        pendingCategories.add(category);
                        transaction.setPendingCategories(pendingCategories);
                        transactionRepository.save(transaction);
//                        keywordFound = true;
                        break;
                    }
                }
                for (String safeKeyword : category.getSafeKeywords()) {
                    if (transaction.getDescription().toLowerCase().contains(safeKeyword.toLowerCase().trim())) {
                        Set<Category> categories = transaction.categories;
                        categories.add(category);
                        transaction.setPendingCategories(categories);
                        transactionRepository.save(transaction);
//                        keywordFound = true;
                        break;
                    }
                }
//                if (keywordFound)
//                    break;
            }
        }
    }

    //todo what to do if transaction doesn't exist, itp?
    public void assignCategoryInTransaction(User user, Long transactionId, Long categoryId) {
        Transaction transaction = transactionRepository.getOne(transactionId);
        if (transaction.getUser().equals(user)) {
            transaction.addCategory(categoryRepository.getOne(categoryId));
            transactionRepository.save(transaction);
        }
    }

    public double balanceByDates(Long userId, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findByDates(start, end, userId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByDatesAndCategory(User user, Date start, Date end, Long categoryId) {
        List<Transaction> transactionList = transactionRepository.findByDateAndUserAndCategory
                (start, end, user, categoryRepository.getOne(categoryId));
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public Map<String, Double> lastYearBalances(Long userId) {
        SortedMap<String, Double> lastYearBalances = new TreeMap<>();
        Date dateNow = Date.valueOf(LocalDate.now());
        StringBuilder sb = new StringBuilder();
        sb.append(dateNow.toLocalDate().getYear()).append("-").append(dateNow.toLocalDate().getMonthValue()).append("-").append("01");
        String monthBegin = sb.toString();
        StringBuilder sb1 = new StringBuilder();
        int month = dateNow.toLocalDate().getMonthValue();
        String yearMonth = "";
        if (month < 10) {
            yearMonth = sb1.append(dateNow.toLocalDate().getYear()).append(" ").append("0").append(month).toString();
        } else {
            yearMonth = sb1.append(dateNow.toLocalDate().getYear()).append(" ").append(month).toString();
        }
        lastYearBalances.put(yearMonth, balanceByDates(userId, Date.valueOf(monthBegin), dateNow));
        for (int i = 0; i < 11; i++) {
            Date monthBeginDate = Date.valueOf(monthBegin);
            sb.delete(0, sb.length());
            sb1.delete(0, sb1.length());
            LocalDate previousMonthEnd = monthBeginDate.toLocalDate().minusDays(1l);
            String previousMonthBegin = sb.append(previousMonthEnd.getYear()).append("-").append(previousMonthEnd.getMonthValue()).append("-").append("01").toString();
            int previousMonth = Date.valueOf(previousMonthBegin).toLocalDate().getMonthValue();
            String previousYearMonth;
            if (previousMonth < 10) {
                previousYearMonth = sb1.append(previousMonthEnd.getYear()).append(" ").append("0").append(previousMonth).toString();
            } else {
                previousYearMonth = sb1.append(previousMonthEnd.getYear()).append(" ").append(previousMonth).toString();
            }
            lastYearBalances.put(previousYearMonth, balanceByDates(userId, Date.valueOf(previousMonthBegin), Date.valueOf(previousMonthEnd)));
            monthBegin = previousMonthBegin;
        }
        System.out.println(lastYearBalances);
        return lastYearBalances;
    }

//    public HashMap<String, Double> balancesByDatesForAllCategories(Long userId, Date start, Date end) {
//        List<Transaction> transactionList = transactionRepository.findByDates(start, end, userId);
//        HashMap<String, Double> balanceByCategory = new HashMap<>();
//        for (Category category : categoryRepository.findAllByUserId(userId)) {
//            Double categorySum = 0.0;
//            for (Transaction transaction : transactionList) {
//                if (transaction.getCategories().contains(category)) {
//                    categorySum += transaction.amount;
//                }
//            }
//
//            balanceByCategory.put(category.getName(), categorySum);
//        }
//        Double sum = 0.0;
//        for (Transaction transaction : transactionList) {
//            if (transaction.getCategories() == null) {
//                sum += transaction.amount;
//            }
//        }
//        balanceByCategory.put("Bez kategorii", sum);
//        return balanceByCategory;
//    }

    public HashMap<Long, List<String>> transactionIdCategories(Long userId) {
        HashMap<Long, List<String>> transactionIdCategories = new HashMap<>();
        List<Transaction> transactionsList = findByUserId(userId);
        for (Transaction t : transactionsList) {
            List<String> categoriesNames = new ArrayList<>();
            t.getCategories().forEach(c -> categoriesNames.add(c.getName()));
            transactionIdCategories.put(t.getId(), categoriesNames);
        }
        return transactionIdCategories;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public HashMap<String, Float> mapTransactionsToCategoriesWithAmounts(List<Transaction> transactionsToBeMapped, Long userId) {
        HashMap<String, Float> categoriesWithAmounts = new HashMap<>();
        List<Category> categoriesByUser = categoryRepository.findAllByUserId(userId);
        for (Category category : categoriesByUser) {
            categoriesWithAmounts.put(category.getName(), 0f);
        }
        for (Transaction transaction : transactionsToBeMapped) {
            if (transaction.amount < 0) {
                int numberOfCategoriesPerTransaction = transaction.categories.size();
                List<String> categoriesPerTransaction = new ArrayList<>();
                for (Category category : transaction.categories) {
                    categoriesPerTransaction.add(category.name);
                }
                for (String categoryName : categoriesPerTransaction) {
                    Float previousAmount = categoriesWithAmounts.get(categoryName);
                    Float newAmount = previousAmount + Math.abs(transaction.amount) / numberOfCategoriesPerTransaction;
                    categoriesWithAmounts.replace(categoryName, newAmount);
                }
            }
        }
        return categoriesWithAmounts;
    }

    void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public List<Transaction> findSpendings(Long userId) {
        return transactionRepository.findSpendings(userId);
    }

    List <Transaction> transactionsByDate(String year, String month, User user){
        List <Transaction> transactionsList= new ArrayList<>();
        if (year.equals("all")){
            transactionsList = transactionRepository.findAllByUserIdOrderByTransactionDateAsc(user.getId());
        } else if(month.equals("all")) {
            transactionsList = transactionRepository.findByYear(year, user.getId());
        } else {
            transactionsList = transactionRepository.findByMonth(year, month, user.getId());
        }
        return transactionsList;
    }
    List<Integer> findYearsByUserId(Long userId){
        Integer currentYear = LocalDate.now().getYear();
        Transaction lastTransaction = transactionRepository.findLastTransaction(userId);
        Integer lastYear;
        if(lastTransaction!=null){
            lastYear = lastTransaction.transactionDate.getYear();
        } else {
            lastYear = currentYear;
        }

        List<Integer> years = new ArrayList<>();
        for (int i = lastYear; i<=currentYear; i++){
            years.add(i);
        }
        return years;
    }

}
