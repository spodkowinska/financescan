package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.bank.BankService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final BankService bankService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, UserService userService, BankService bankService,
                                 CategoryService categoryService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.bankService = bankService;
        this.categoryService = categoryService;
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

    @GetMapping("/setcategories/{transactionId}/{categories}")
    public String setCategories(@PathVariable Long transactionId, @PathVariable String categories) {
        User user1 = userService.findById(2l);
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction.getUser() == user1) {
            if (categories.equals("0")) {
                transaction.setCategories(null);
            } else {
                transaction.setCategories(transactionService.categoriesfromUrlString(categories));
                transactionService.save(transaction);
            }
        }
        return "transactions-list";
    }

    @GetMapping("/present")
    public String present(Model model) {
        User user2 = userService.findById(2l);
        String str = "2019-10-31";
        Date date1 = Date.valueOf(str);
        String str2 = "2019-11-31";
        Date date2 = Date.valueOf(str2);
        List<Transaction> allTransactions = transactionService.findByUsersId(2l);
        Map<String, Float> categoriesAndAmounts = transactionService.mapExpensesToCategoriesWithAmounts(allTransactions, 2l);
        model.addAttribute("categoriesWithAmounts", categoriesAndAmounts);
        return "present";
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
        return transactionService.balancesByDatesForAllCategories(user1, date1, date2).toString();
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
