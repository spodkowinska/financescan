package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.OpenCSVReadAndParse;
import info.podkowinski.sandra.financescanner.imports.Import;
import info.podkowinski.sandra.financescanner.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
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

    public void scanDocument(InputStream inputStream, CsvSettings csvSettings, Import import1, Project project)
            throws IOException, CsvValidationException, ParseException {
        OpenCSVReadAndParse parser = new OpenCSVReadAndParse();
        Long accountId = import1.getAccount().getId();
        // todo fix this ugly hack (detecting mBank,PKO) to pass encoding
        String inputCharset = (csvSettings.getId() == 1l)||(csvSettings.getId()==5l) ? "Cp1250" : "UTF-8";
        List<List<String>> transactions = parser.csvTransactions(inputStream, csvSettings.getCsvSeparator(),
                csvSettings.getSkipLines(), inputCharset);
        for (List<String> trans : transactions) {
            Transaction newTransaction = new Transaction();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // todo fix this ugly hack (detecting santander) to parse Date
            if (csvSettings.getId() == 3l) {
                newTransaction.transactionDate = LocalDate.parse(trans.get(csvSettings.getDatePosition()), formatter);
            } else {
                newTransaction.transactionDate = LocalDate.parse(trans.get(csvSettings.getDatePosition()));
            }
            newTransaction.party = trans.get(csvSettings.getPartyPosition());
            newTransaction.description = trans.get(csvSettings.getDescriptionPosition());
            newTransaction.account = import1.getAccount();
            //todo fix getting amount in Millennium two column version
            int amountPosition = csvSettings.getAmountPosition();
            if (trans.get(amountPosition).isEmpty()) {
                newTransaction.amount = Float.parseFloat(trans.get(amountPosition + 1)
                    .replace(',', '.')
                    .replaceAll("[^\\d.-]+", ""));
            } else {
                newTransaction.amount = Float.parseFloat(trans.get(amountPosition)
                    .replace(',', '.')
                    .replaceAll("[^\\d.-]+", ""));
            }
            newTransaction.importName = import1;
            newTransaction.project = project;
            transactionRepository.save(newTransaction);
        }
    }

    public List<Transaction> findByProjectId(long id) {
        return transactionRepository.findAllByProjectIdOrderByTransactionDateAsc(id);
    }

    public void assignDefaultCategoriesInTransactions(Project project) {
        List<Transaction> transactionList = transactionRepository.findAllByProjectId(project.getId());
        for (Transaction transaction : transactionList) {
            for (Category category : categoryRepository.findAllByProjectId(project.getId())) {
                for (String keyword : category.getKeywords()) {
                    if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase().trim())) {
                        if(!transaction.categories.contains(category) && !transaction.rejectedCategories.contains(category)){
                        Set<Category> pendingCategories = transaction.pendingCategories;
                        pendingCategories.add(category);
                        transaction.setPendingCategories(pendingCategories);
                        transactionRepository.save(transaction);
                        }
                        break;
                    }
                }
                for (String safeKeyword : category.getSafeKeywords()) {
                    if (transaction.getDescription().toLowerCase().contains(safeKeyword.toLowerCase().trim())) {
                        Set<Category> categories = transaction.categories;
                        categories.add(category);
                        transaction.setCategories(categories);
                        transactionRepository.save(transaction);
                        break;
                    }
                }
            }
        }
    }


    public double balanceByDates(Long projectId, Date start, Date end) {
        List<Transaction> transactionList = transactionRepository.findByDates(start, end, projectId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByDatesAndCategory(Project project, Date start, Date end, Long categoryId) {
        List<Transaction> transactionList = transactionRepository.findByDateAndProjectAndCategory
                (start, end, project, categoryRepository.getOne(categoryId));
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public Map<String, Double> lastYearBalances(Long projectId) {
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
        lastYearBalances.put(yearMonth, balanceByDates(projectId, Date.valueOf(monthBegin), dateNow));
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
            lastYearBalances.put(previousYearMonth, balanceByDates(projectId, Date.valueOf(previousMonthBegin), Date.valueOf(previousMonthEnd)));
            monthBegin = previousMonthBegin;
        }
        System.out.println(lastYearBalances);
        return lastYearBalances;
    }

//    public HashMap<String, Double> balancesByDatesForAllCategories(Long projectId, Date start, Date end) {
//        List<Transaction> transactionList = transactionRepository.findByDates(start, end, projectId);
//        HashMap<String, Double> balanceByCategory = new HashMap<>();
//        for (Category category : categoryRepository.findAllByProjectId(projectId)) {
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

    public HashMap<Long, List<String>> transactionIdCategories(Long projectId) {
        HashMap<Long, List<String>> transactionIdCategories = new HashMap<>();
        List<Transaction> transactionsList = findByProjectId(projectId);
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

    public HashMap<String, Double> mapTransactionsToCategoriesWithAmounts(List<Transaction> transactionsToBeMapped, Long projectId) {
        HashMap<String, Double> categoriesWithAmounts = new HashMap<>();
        List<Category> categoriesByProject = categoryRepository.findAllByProjectId(projectId);
        for (Category category : categoriesByProject) {
            categoriesWithAmounts.put(category.getName(), 0d);
        }
        for (Transaction transaction : transactionsToBeMapped) {
            if (transaction.amount < 0) {
                int numberOfCategoriesPerTransaction = transaction.categories.size();
                List<String> categoriesPerTransaction = new ArrayList<>();
                for (Category category : transaction.categories) {
                    categoriesPerTransaction.add(category.name);
                }
                for (String categoryName : categoriesPerTransaction) {
                    Double previousAmount = categoriesWithAmounts.get(categoryName);
                    Double newAmount = previousAmount + Math.abs(transaction.amount) / numberOfCategoriesPerTransaction;
                    categoriesWithAmounts.replace(categoryName, newAmount);
                }
            }
        }
        return categoriesWithAmounts;
    }

    void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public List<Transaction> findSpendings(Long projectId) {
        return transactionRepository.findSpendings(projectId);
    }

    Page <Transaction> transactionsByDate(String year, String month, Project project, Pageable pageable){
        Page<Transaction> transactionsList;
        if (year.equals("all")){
            transactionsList = transactionRepository.findAllByProjectIdOrderByTransactionDateAsc(project.getId(), pageable);
        } else if(month.equals("all")) {
            transactionsList = transactionRepository.findByYearPage(year, project.getId(), pageable);
        } else {
            transactionsList = transactionRepository.findByMonthPage(year, month, project.getId(), pageable);
        }
        return transactionsList;
    }

    public class YearsAndLastMonthResult {
        public List<Integer> years;
        public int lastMonth;
    }
    public YearsAndLastMonthResult findYearsAndLastMonthByProjectId(Long projectId){
        Transaction latestTransaction = transactionRepository.findLatestTransaction(projectId);
        Transaction oldestTransaction = transactionRepository.findOldestTransaction(projectId);
        List<Integer> years = new ArrayList<>();
        if(oldestTransaction!=null && latestTransaction!=null){
            int oldestYear = oldestTransaction.transactionDate.getYear();
            int latestYear = latestTransaction.transactionDate.getYear();
            for (int i = oldestYear; i<=latestYear; i++){
                years.add(i);
            }
        }

        YearsAndLastMonthResult ret = new YearsAndLastMonthResult();
        ret.years = years;
        ret.lastMonth = latestTransaction == null ? 0 : latestTransaction.transactionDate.getMonthValue();

        return ret;
    }

    public void removeCategoryFromTransactions(Long categoryId){
        transactionRepository.deleteAssignedCategoriesByCategoryId(categoryId);
        transactionRepository.deleteAssignedPendingCategoriesByCategoryId(categoryId);
        transactionRepository.deleteAssignedRejectedCategoriesByCategoryId(categoryId);
    }

}
