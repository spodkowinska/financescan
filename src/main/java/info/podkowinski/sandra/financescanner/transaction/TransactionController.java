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
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String transaction(Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        List<Transaction> transactionsList = transactionService.findByProjectId(project.getId());
        List<Account> accountsList = accountService.findByProjectId(project.getId());
        List<Category> categoriesList = categoryService.findByProjectId(project.getId());
        List<Integer> years = transactionService.findYearsByProjectId(project.getId());
        HashMap<Long, List<String>> transactionCategory = transactionService.transactionIdCategories(project.getId());
        model.addAttribute("tl", transactionsList);
        model.addAttribute("bl", accountsList);
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("transCategories", transactionCategory);
        model.addAttribute("years", years);
        return "transactions/transaction-table";
    }

    @GetMapping("/fileimport")
    public String fileimport(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Account> accountsList = accountService.findByProjectId(project.getId());
        List<CsvSettings> csvSettingsList = csvSettingsService.findByProjectId(project.getId());
        model.addAttribute("csvSettingsList", csvSettingsList);
        model.addAttribute("accountsList", accountsList);
        return "file-import";
    }

    @PostMapping("/fileimport")
    public String fileimportPost(HttpServletRequest request, @AuthenticationPrincipal CurrentUser currentUser) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        Project project = currentUser.getUser().getCurrentProject();
        int datePosition = Integer.parseInt(request.getParameter("datePosition")) - 1;
        int descriptionPosition = Integer.parseInt(request.getParameter("descriptionPosition")) - 1;
        int partyPosition = Integer.parseInt(request.getParameter("partyPosition")) - 1;
        int amountPosition = Integer.parseInt(request.getParameter("amountPosition")) - 1;
        int skippedLines = Integer.parseInt(request.getParameter("skipLines"));
        char separator = request.getParameter("separator").charAt(0);
        String importName = request.getParameter("importName");
        Long account = Long.parseLong(request.getParameter("selectAccount"));
        transactionService.scanDocument(filePart.getInputStream(), datePosition, descriptionPosition,
                partyPosition, amountPosition, separator, skippedLines, importName, account, project);
        return "redirect:/transaction/list";
    }

    @GetMapping("/add")
    public String add(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categories = categoryService.findByProjectId(project.getId());
        List<Account> accounts = accountService.findByProjectId(project.getId());
        Transaction transaction = new Transaction();
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "transactions/transaction-edit";
    }

    //todo frontend validation
    @PostMapping("/add")
    public String addPost(HttpServletRequest request, @ModelAttribute Transaction transaction1, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        transaction1.setProject(project);
        transaction1.setImportName("Imported manually on " + LocalDate.now());
        String date = request.getParameter("transactionDate");
        System.out.println(date);
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);

        return "redirect:/transaction/table/gettransaction/" + transaction1.id;
    }

    @GetMapping("/edit/{transactionId}")
    public String edit(Model model, @PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categories = categoryService.findByProjectId(project.getId());
        List<Account> accounts = accountService.findByProjectId(project.getId());
        Transaction transaction = transactionService.findById(transactionId);
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "transactions/transaction-edit";
    }

    //todo frontend validation
    @PostMapping("/edit/{transactionId}")
    public String editPost(HttpServletRequest request, @ModelAttribute Transaction transaction1, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        transaction1.setProject(project);
        transaction1.setImportName("Imported manually on " + LocalDate.now());
        String date = request.getParameter("transactionDate");
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);
        return "redirect:/transaction/table/gettransaction/" + transaction1.id;
    }


    @ResponseBody
    @GetMapping("/removeallcategories/{transactionId}")
    public String removeAllCategories(@PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project) {
            transaction.rejectedCategories.addAll(transaction.getPendingCategories());
            transaction.rejectedCategories.addAll(transaction.getCategories());
            transaction.setCategories(null);
            transaction.setPendingCategories(null);
            transactionService.save(transaction);
        }
        return "";
    }

    @ResponseBody
    @GetMapping("/acceptallsuggestions/{transactionId}")
    public String acceptAllSuggestions(@PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project) {
            transaction.categories.addAll(transaction.getPendingCategories());
            transaction.setPendingCategories(null);
            transactionService.save(transaction);
        }
        return "" + transaction.categories.size();
    }

    @ResponseBody
    @GetMapping("/rejectallsuggestions/{transactionId}")
    public String rejectAllSuggestions(@PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project) {
            transaction.rejectedCategories.addAll(transaction.getPendingCategories());
            transaction.setPendingCategories(null);
            transactionService.save(transaction);
        }
        return "" + transaction.categories.size();
    }

    @ResponseBody
    @GetMapping("/addcategory/{transactionId}/{categoryId}")
    public String addCategory(@PathVariable Long transactionId, @PathVariable Long categoryId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project) {
            transaction.addCategory(categoryService.findById(categoryId));
            transactionService.save(transaction);
        }
        return transaction.categories.size() + "," + transaction.pendingCategories.size();
    }

    @ResponseBody
    @GetMapping("/removecategory/{transactionId}/{categoryId}")
    public String removeCategory(@PathVariable Long transactionId, @PathVariable Long categoryId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = projectService.findCurrentByUser(currentUser.getUser());
        Category categoryToRemove = categoryService.findById(categoryId);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject() == project) {
            if (transaction.pendingCategories.contains(categoryToRemove)) {
                transaction.removeCategory(categoryToRemove);
            }
            transactionService.save(transaction);
        }
        return transaction.categories.size() + "," + transaction.pendingCategories.size();
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = projectService.findCurrentByUser(currentUser.getUser());
        Transaction transaction = transactionService.findById(id);
        if (transaction.project.equals(project)) {
            transaction.categories.clear();
            transaction.pendingCategories.clear();
            transaction.rejectedCategories.clear();
            transactionService.delete(transaction);
        }
        return "";
    }

    @GetMapping("/assign")
    public String assign(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        transactionService.assignDefaultCategoriesInTransactions(project);
        List<Transaction> transactionsList = transactionService.findByProjectId(project.getId());
        model.addAttribute("tl", transactionsList);
        return "redirect:/transaction/list";
    }

    @GetMapping("/table/{year}/{month}")
    public String table(Model model, @PathVariable String year, @PathVariable String month, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Transaction> transactionsList = transactionService.transactionsByDate(year, month, project);
        List<Category> categories = categoryService.findByProjectId(project.getId());
        model.addAttribute("categoriesList", categories);
        model.addAttribute("tl", transactionsList);
        return "transactions/transaction-table-rows";
    }

    @GetMapping("/table/gettransaction/{transactionId}")
    public String tableRow(Model model, @PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Transaction> transactions = Arrays.asList(transactionService.findById(transactionId));
        List<Category> categories = categoryService.findByProjectId(project.getId());
        model.addAttribute("categoriesList", categories);
        model.addAttribute("tl", transactions);
        return "transactions/transaction-table-rows";
    }
}
