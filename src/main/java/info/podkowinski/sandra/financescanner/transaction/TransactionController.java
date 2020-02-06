package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


import static java.sql.Date.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;

    public TransactionController(TransactionService transactionService, UserService userService, AccountService accountService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
    }

    @RequestMapping("/list")
    public String transaction(Model model) {
        User user2 = userService.findById(2l);
        List<Transaction> transactionsList = transactionService.findByUserId(2l);
        List<Account> accountsList = accountService.findByUserId(2l);
        List<Category> categoriesList = categoryService.findByUserId(2l);
        HashMap<Long, List<String>> transactionCategory = transactionService.transactionIdCategories(2l);
        model.addAttribute("tl", transactionsList);
        model.addAttribute("bl", accountsList);
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("transCategories", transactionCategory);
        return "list-transactions";
    }

    @GetMapping("/fileimport")
    public String fileimport(Model model) {
        User user1 = userService.findById(2l);
        List<Account> accountsList = accountService.findByUserId(2l);
        List<CsvSettings> csvSettingsList = csvSettingsService.findByUserId(user1.getId());
        model.addAttribute("csvSettingsList", csvSettingsList);
        model.addAttribute("accountsList", accountsList);
        return "file-import";
    }

    @PostMapping("/fileimport")
    public String fileimportPost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        User user1 = userService.findById(2l);
        int datePosition = Integer.parseInt(request.getParameter("datePosition")) - 1;
        int descriptionPosition = Integer.parseInt(request.getParameter("descriptionPosition")) - 1;
        int partyPosition = Integer.parseInt(request.getParameter("partyPosition")) - 1;
        int amountPosition = Integer.parseInt(request.getParameter("amountPosition")) - 1;
        int skippedLines = Integer.parseInt(request.getParameter("skipLines"));
        char separator = request.getParameter("separator").charAt(0);
        String importName = request.getParameter("importName");
        Long account = Long.parseLong(request.getParameter("selectAccount"));
        transactionService.scanDocument(filePart.getInputStream(), datePosition, descriptionPosition,
                partyPosition, amountPosition, separator, skippedLines, importName, account, user1);
        return "redirect:/transaction/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        List<Account> accounts = accountService.findByUserId(2l);
        Transaction transaction = new Transaction();
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "add-transaction";
    }

    //todo frontend validation
    @PostMapping("/add")
    public String addPost(HttpServletRequest request, @ModelAttribute Transaction transaction1) {
        User user1 = userService.findById(2l);
        transaction1.setUser(user1);
        transaction1.setImportName("Imported manually on " + LocalDate.now());
        String date = request.getParameter("transactionDate");
        System.out.println("------");
        System.out.println(date);
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);
        return "redirect:/transaction/list";
    }

    @GetMapping("/edit/{transactionId}")
    public String edit(Model model, @PathVariable Long transactionId) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        List<Account> accounts = accountService.findByUserId(2l);
        Transaction transaction = transactionService.findById(transactionId);
        model.addAttribute("categories", categories);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "edit-transaction";
    }

    //todo frontend validation
    @PostMapping("/edit/{transactionId}")
    public String editPost(HttpServletRequest request, @ModelAttribute Transaction transaction1) {
        User user1 = userService.findById(2l);
        transaction1.setUser(user1);
        transaction1.setImportName("Imported manually on " + LocalDate.now());
        String date = request.getParameter("transactionDate");
        System.out.println("------");
        System.out.println(date);
        transaction1.setTransactionDate(LocalDate.parse(date));
        System.out.println(transaction1.transactionDate);
        transactionService.save(transaction1);
        return "redirect:/transaction/list";
    }

    @GetMapping("/setcategories/{transactionId}/{categories}")
    public String setCategories(@PathVariable Long transactionId, @PathVariable String categories) {
        User user1 = userService.findById(2l);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getUser() == user1) {
            if (categories.equals("0")) {
                transaction.setCategories(null);
            } else {
                transaction.setCategories(transactionService.categoriesFromUrlString(categories));
                transactionService.save(transaction);
            }
        }
        return "transactions-list";
    }

    @RequestMapping("/home/sum")
    @ResponseBody
    public String sumBtn() {

        String str = "2019-10-31";
        Date date1 = valueOf(str);
        String str2 = "2019-11-31";
        Date date2 = valueOf(str2);
        User user1 = userService.findById(2l);
//        return String.valueOf(transactionService.balanceByDatesAndCategory(user1, date1, date2, 2l));
        return transactionService.balancesByDatesForAllCategories(user1.getId(), date1, date2).toString();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        Transaction transaction = transactionService.findById(id);
        transaction.categories.clear();
        transactionService.delete(transaction);
        List<Transaction> transactionsList = transactionService.findByUserId(2l);
        model.addAttribute("tl", transactionsList);
        return "redirect:/transaction/list";
    }
    @GetMapping("/assign")
    public String assign( Model model) {
        User user1 = userService.findById(2l);
        transactionService.assignDefaultCategoriesInTransactions(user1);
        List<Transaction> transactionsList = transactionService.findByUserId(2l);
        model.addAttribute("tl", transactionsList);
        return "redirect:/transaction/list";
    }
}
