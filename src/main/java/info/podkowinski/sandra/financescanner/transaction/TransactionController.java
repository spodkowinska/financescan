package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.bank.BankService;
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
import java.util.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final BankService bankService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;

    public TransactionController(TransactionService transactionService, UserService userService, BankService bankService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.bankService = bankService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
    }

    @RequestMapping("/list")
    public String transaction(Model model) {
        User user2 = userService.findById(2l);
        List<Transaction> transactionsList = transactionService.findByUsersId(2l);
        List<Bank> banksList = bankService.findByUserId(2l);
        List<Category> categoriesList = categoryService.findByUserId(2l);
        HashMap<Long, List<String>> transactionCategory = transactionService.transactionIdCategories(2l);
        model.addAttribute("tl", transactionsList);
        model.addAttribute("bl", banksList);
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("transCategories", transactionCategory);
        return "transactions-list";
    }
    @GetMapping("/fileimport")
    public String fileimport(Model model) {
        User user1 = userService.findById(1l);
        List<Bank>banksList = bankService.findByUserId(1l);
        List<CsvSettings> csvSettingsList = csvSettingsService.findByUserId(user1.getId());
        model.addAttribute("csvSettingsList", csvSettingsList);
        model.addAttribute("banksList", banksList);
        return "file-import";
    }

    @PostMapping("/fileimport")
    @ResponseBody
    public String fileimportPost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        User user1 = userService.findById(1l);
        int datePosition = Integer.parseInt(request.getParameter("datePosition"))-1;
        int descriptionPosition = Integer.parseInt(request.getParameter("descriptionPosition"))-1;
        int partyPosition = Integer.parseInt(request.getParameter("partyPosition"))-1;
        int amountPosition = Integer.parseInt(request.getParameter("amountPosition"))-1;
        int skippedLines = Integer.parseInt(request.getParameter("skipLines"));
        char separator = request.getParameter("separator").charAt(0);
        String importName = request.getParameter("importName");
        Long bank = Long.parseLong(request.getParameter("bank"));
        transactionService.scanDocument(filePart.getInputStream(), datePosition, descriptionPosition,
                partyPosition, amountPosition, separator, skippedLines, importName, bank, user1);
        return "good";
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        List<Bank> banks = bankService.findByUserId(2l);
        Transaction transaction = new Transaction();
        model.addAttribute("categories", categories);
        model.addAttribute("banks", banks);
        model.addAttribute("transaction", transaction);
        return "add-transaction";
    }
    //todo frontend validation
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        Date date = Date.valueOf(request.getParameter("date"));
        Float amount = Float.parseFloat(request.getParameter("amount"));
        String description = request.getParameter("description");
        String party = request.getParameter("party");
        Bank bank = bankService.findBankById(Long.parseLong(request.getParameter("bankId")));
        String importName = "Imported manually on " + LocalDate.now();
        //todo multiple select
        List <Category> categories = Arrays.asList(categoryService.findById(Long.parseLong(request.getParameter("category"))));
        Transaction transaction = new Transaction();
        transaction.transactionDate = date;
        transaction.amount = amount;
        transaction.description = description;
        transaction.categories = categories;
        transaction.bank = bank;
        transaction.party = party;
        transaction.importName = importName;
        transaction.user = user1;
        return "redirect:../transaction/list";
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
        Date date1 = Date.valueOf(str);
        String str2 = "2019-11-31";
        Date date2 = Date.valueOf(str2);
        User user1 = userService.findById(2l);
//        return String.valueOf(transactionService.balanceByDatesAndCategory(user1, date1, date2, 2l));
        return transactionService.balancesByDatesForAllCategories(user1.getId(), date1, date2).toString();
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
