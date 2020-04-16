package info.podkowinski.sandra.financescanner.controllers;

import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.user.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HomeController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final CsvSettingsService csvSettingsService;
    private final AccountService accountService;
    private final UserServiceImpl userService;

    public HomeController(TransactionService transactionService, ProjectService projectService, AccountService accountService, CsvSettingsService csvSettingsService, UserServiceImpl userService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.csvSettingsService = csvSettingsService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }


    @GetMapping("/setDatabase")
    public String setProject() {

        csvSettingsService.createDefaultBanksSettings();
        userService.saveRole("ROLE_ADMIN");
        return "redirect:../category/list";
    }

    @GetMapping("/report")
    public String present(Model model) {
//        Project user2 = userService.findById(2l);
//        String str = "2019-10-31";
//        Date date1 = Date.valueOf(str);
//        String str2 = "2019-11-31";
//        Date date2 = Date.valueOf(str2);
        List<Transaction> allTransactions = transactionService.findByProjectId(2l);
        Map<String, Double> lastYearBalances = transactionService.lastYearBalances(2l);
        Map<String, Double> categoriesAndAmounts = transactionService.mapTransactionsToCategoriesWithAmounts(allTransactions, 2l);
        Map<String, Double> categoriesAndSpendings = transactionService.mapTransactionsToCategoriesWithAmounts(transactionService.findSpendings(2l), 2l);
        model.addAttribute("categoriesWithAmounts", categoriesAndAmounts);
        model.addAttribute("lastYearBalances", lastYearBalances);
        model.addAttribute("categoriesWithSpendings", categoriesAndSpendings);
        return "report";
    }


}

