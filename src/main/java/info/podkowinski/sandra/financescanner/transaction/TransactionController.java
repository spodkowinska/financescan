package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;



@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;

    public TransactionController(TransactionService transactionService, ProjectService projectService, AccountService accountService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
    }

    @RequestMapping("/list")
    public String transaction(Model model) {
        Project project2 = projectService.findById(2l);
        List<Transaction> transactionsList = transactionService.findByProjectId(2l);
        List<Account> accountsList = accountService.findByProjectId(2l);
        List<Category> categoriesList = categoryService.findByProjectId(2l);
        List<Integer> years = transactionService.findYearsByProjectId(2l);
        HashMap<Long, List<String>> transactionCategory = transactionService.transactionIdCategories(2l);
        model.addAttribute("tl", transactionsList);
        model.addAttribute("bl", accountsList);
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("transCategories", transactionCategory);
        model.addAttribute("years", years);
        return "transactions/transaction-table";
    }

    @GetMapping("/fileimport")
    public String fileimport(Model model) {
        Project project1 = projectService.findById(2l);
        List<Account> accountsList = accountService.findByProjectId(2l);
        List<CsvSettings> csvSettingsList = csvSettingsService.findByProjectId(project1.getId());
        model.addAttribute("csvSettingsList", csvSettingsList);
        model.addAttribute("accountsList", accountsList);
        return "file-import";
    }

    @PostMapping("/fileimport")
    public String fileimportPost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        Project project1 = projectService.findById(2l);
        int datePosition = Integer.parseInt(request.getParameter("datePosition")) - 1;
        int descriptionPosition = Integer.parseInt(request.getParameter("descriptionPosition")) - 1;
        int partyPosition = Integer.parseInt(request.getParameter("partyPosition")) - 1;
        int amountPosition = Integer.parseInt(request.getParameter("amountPosition")) - 1;
        int skippedLines = Integer.parseInt(request.getParameter("skipLines"));
        char separator = request.getParameter("separator").charAt(0);
        String importName = request.getParameter("importName");
        Long account = Long.parseLong(request.getParameter("selectAccount"));
        transactionService.scanDocument(filePart.getInputStream(), datePosition, descriptionPosition,
                partyPosition, amountPosition, separator, skippedLines, importName, account, project1);
        return "redirect:/transaction/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Project project1 = projectService.findById(2l);
        List<Category> categories = categoryService.findByProjectId(2l);
        List<Account> accounts = accountService.findByProjectId(2l);
        Transaction transaction = new Transaction();
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "transactions/transaction-edit";
    }

    //todo frontend validation
    @PostMapping("/add")
    public String addPost(HttpServletRequest request, @ModelAttribute Transaction transaction1) {
        Project project1 = projectService.findById(2l);
        transaction1.setProject(project1);
        transaction1.setImportName("Imported manually on " + LocalDate.now());

        String date = request.getParameter("transactionDate");
        System.out.println(date);
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);

        transactionService.save(transaction1);

        return "redirect:/transaction/table/gettransaction/" + transaction1.id;
    }

    @GetMapping("/edit/{transactionId}")
    public String edit(Model model, @PathVariable Long transactionId) {
        Project project1 = projectService.findById(2l);
        List<Category> categories = categoryService.findByProjectId(2l);
        List<Account> accounts = accountService.findByProjectId(2l);
        Transaction transaction = transactionService.findById(transactionId);
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "transactions/transaction-edit";
    }

    //todo frontend validation
    @PostMapping("/edit/{transactionId}")
    public String editPost(HttpServletRequest request, @ModelAttribute Transaction transaction1) {
        Project project1 = projectService.findById(2l);
        transaction1.setProject(project1);
        transaction1.setImportName("Imported manually on " + LocalDate.now());

        String date = request.getParameter("transactionDate");
        transaction1.setTransactionDate(LocalDate.parse(date));

        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);
        return "redirect:/transaction/table/gettransaction/" + transaction1.id;
    }

    @ResponseBody
    @GetMapping("/setcategories/{transactionId}/{categories}")
    public String setCategories(@PathVariable Long transactionId, @PathVariable String categories) {
        Project project1 = projectService.findById(2l);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project1) {
            if (categories.equals("0")) {
                transaction.setCategories(null);
            } else {
                transaction.setCategories(transactionService.categoriesFromUrlString(categories));
                transactionService.save(transaction);
            }
        }
        return "";
    }

    @ResponseBody
    @GetMapping("/addcategory/{transactionId}/{categoryId}")
    public String addCategory(@PathVariable Long transactionId, @PathVariable Long categoryId) {
        Project project1 = projectService.findById(2l);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project1) {
            transaction.addCategory(categoryService.findById(categoryId));
            transactionService.save(transaction);
        }
        return transaction.categories.size() + "," + transaction.pendingCategories.size();
    }

    @ResponseBody
    @GetMapping("/removecategory/{transactionId}/{categoryId}")
    public String removeCategory(@PathVariable Long transactionId, @PathVariable Long categoryId) {
        Project project1 = projectService.findById(2l);
        Category categoryToRemove = categoryService.findById(categoryId);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project1) {
            if(transaction.pendingCategories.contains(categoryToRemove)){
                transaction.rejectCategory(categoryToRemove);
            } else {
                transaction.removeCategory(categoryService.findById(categoryId));
            }
            transactionService.save(transaction);
        }
        return transaction.categories.size() + "," + transaction.pendingCategories.size();
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Project project1 = projectService.findById(2l);
        Transaction transaction = transactionService.findById(id);
        transaction.categories.clear();
        transaction.pendingCategories.clear();
        transaction.rejectedCategories.clear();
        transactionService.delete(transaction);
        return "";
    }
    @GetMapping("/assign")
    public String assign( Model model) {
        Project project2 = projectService.findById(2l);
        transactionService.assignDefaultCategoriesInTransactions(project2);
        List<Transaction> transactionsList = transactionService.findByProjectId(2l);
        model.addAttribute("tl", transactionsList);
        return "redirect:/transaction/list";
    }

    @GetMapping("/table/{year}/{month}")
    public String table( Model model, @PathVariable String year, @PathVariable String month) {
        Project project2 = projectService.findById(2l);
        List<Transaction> transactionsList = transactionService.transactionsByDate(year, month, project2);
        List<Category> categories = categoryService.findByProjectId(2l);
        model.addAttribute("categoriesList", categories);
        model.addAttribute("tl", transactionsList);
        return "transactions/transaction-table-rows";
    }

    @GetMapping("/table/gettransaction/{transactionId}")
    public String tableRow( Model model, @PathVariable Long transactionId) {
        Project project2 = projectService.findById(2l);
        List <Transaction> transactions = Arrays.asList(transactionService.findById(transactionId));
        List<Category> categories = categoryService.findByProjectId(2l);
        model.addAttribute("categoriesList", categories);
        model.addAttribute("tl", transactions);
        return "transactions/transaction-table-rows";
    }
}
