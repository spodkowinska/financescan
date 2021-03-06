package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.category.KeywordDTO;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.imports.Import;
import info.podkowinski.sandra.financescanner.imports.ImportService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.websocket.server.PathParam;
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
    private final ImportService importService;

    public TransactionController(TransactionService transactionService, ProjectService projectService, AccountService accountService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService, ImportService importService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
        this.importService = importService;
    }

    @RequestMapping("/list")
    public String transaction(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        final Project project = currentUser.getUser().getCurrentProject();
        final List<Account> accountsList = accountService.findByProjectId(project.getId());
        final List<Category> categoriesList = categoryService.findByProjectId(project.getId());
        final var yearsAndLastMonth = transactionService.findYearsAndLastMonthByProjectId(project.getId());
//        final HashMap<Long, List<String>> transactionCategory = transactionService.transactionIdCategories(project.getId());
        model.addAttribute("bl", accountsList);
        model.addAttribute("keywordDTO", new KeywordDTO());
        model.addAttribute("categoriesList", categoriesList);
//        model.addAttribute("transCategories", transactionCategory);
        model.addAttribute("years", yearsAndLastMonth.years);
        model.addAttribute("lastMonth", yearsAndLastMonth.lastMonth);
        final int lastYear = yearsAndLastMonth.years.size() == 0 ?
                0 : yearsAndLastMonth.years.get(yearsAndLastMonth.years.size()-1);
        model.addAttribute("lastYear", lastYear);
        return "transactions/transaction-table";
    }

    @GetMapping("/fileimport")
    public String fileImport(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Account> accountsList = accountService.findByProjectId(project.getId());
        List<CsvSettings> csvSettingsList = csvSettingsService.findAllDefault();
        Import import1 = new Import();
        model.addAttribute("csvSettingsList", csvSettingsList);
        model.addAttribute("accountsList", accountsList);
        model.addAttribute("import", import1);
        return "file-import";
    }

    @PostMapping("/fileimport")
    public String fileImportPost(HttpServletRequest request, @AuthenticationPrincipal CurrentUser currentUser) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        String fileName = filePart.getSubmittedFileName();
        String importName = request.getParameter("importName");
        CsvSettings csvSettings = csvSettingsService.findById(Long.parseLong(request.getParameter("selectSettings")));
        Account account = accountService.findById(Long.parseLong(request.getParameter("selectAccount")));
        Project project = currentUser.getUser().getCurrentProject();
        Import import1 = new Import();
        import1.setName(importName);
        import1.setProject(project);
        import1.setFileName(fileName);
        import1.setUsedSettings(csvSettings);
        import1.setAccount(account);
        importService.save(import1);
        transactionService.scanDocument(filePart.getInputStream(), csvSettings, import1, project);
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
        String date = request.getParameter("transactionDate");
        transaction1.setTransactionDate(LocalDate.parse(date));
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
        String date = request.getParameter("transactionDate");
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);
        return "redirect:/transaction/table/gettransaction/" + transaction1.id;
    }

    @GetMapping("/changeaccount/{transactionId}/{accountId}")
    public String changeAccount(@PathVariable Long transactionId, @PathVariable Long accountId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction != null && project != null && transaction.project.getId().equals(project.getId())) {
            Account account = accountService.findById(accountId);
            if (account != null && account.getProject().getId().equals(project.getId())) {
                transaction.account = account;
                transactionService.save(transaction);
            }
        }
        return "redirect:/transaction/table/gettransaction/" + transactionId;
    }

    @ResponseBody
    @GetMapping("/removeallcategories/{transactionId}")
    public String removeAllCategories(@PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject().getId().equals(project.getId())) {
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
        if (transaction.getProject().getId().equals(project.getId())) {
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
        if (transaction.getProject().getId().equals(project.getId())) {
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
        if (transaction.getProject().getId().equals(project.getId())) {
            transaction.addCategory(categoryService.findById(categoryId));
            transactionService.save(transaction);
        }
        return transaction.categories.size() + "," + transaction.pendingCategories.size();
    }

    @ResponseBody
    @GetMapping("/removecategory/{transactionId}/{categoryId}")
    public String removeCategory(@PathVariable Long transactionId, @PathVariable Long categoryId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Category categoryToRemove = categoryService.findById(categoryId);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getProject().getId().equals(project.getId())) {
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
        Project currentProject = currentUser.getUser().getCurrentProject();
        Transaction transaction = transactionService.findById(id);
        if (currentProject.getId().equals(transaction.project.getId())){
            transaction.categories.clear();
            transaction.pendingCategories.clear();
            transaction.rejectedCategories.clear();
            transaction.importName=null;
            transaction.project=null;
            transaction.account=null;
            transactionService.save(transaction);
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
    public String table(Model model, @PathVariable String year, @PathVariable String month,
                        @AuthenticationPrincipal CurrentUser currentUser, @PathParam("page") Integer page, @PathParam("size") Integer size) {
        Project project = currentUser.getUser().getCurrentProject();
        if(page == null){
            page = 1;
        }
        if (size == null){
            size = 100;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionService.transactionsByDate(year, month, project, pageable);
        List<Category> categories = categoryService.findByProjectId(project.getId());
        model.addAttribute("categoriesList", categories);
        model.addAttribute("transactionsPage", transactionsPage);
        model.addAttribute("transactionsList", null);
        return "transactions/transaction-table-rows";
    }

    @GetMapping("/table/gettransaction/{transactionId}")
    public String tableRow(Model model, @PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Transaction> transactionsList = Collections.singletonList(transactionService.findById(transactionId));
        List<Category> categories = categoryService.findByProjectId(project.getId());
        model.addAttribute("categoriesList", categories);
        model.addAttribute("transactionsPage", null);
        model.addAttribute("transactionsList", transactionsList);
        return "transactions/transaction-table-rows";
    }
}
