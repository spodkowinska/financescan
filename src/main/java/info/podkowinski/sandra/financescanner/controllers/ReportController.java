package info.podkowinski.sandra.financescanner.controllers;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.imports.ImportService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;
    private final ImportService importService;

    public ReportController(TransactionService transactionService, ProjectService projectService, AccountService accountService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService, ImportService importService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
        this.importService = importService;
    }

    @GetMapping()
    public String yearsReport(Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        List <Integer> years =transactionService.findYearsByProjectId(project.getId());
        model.addAttribute("years", years);
        return "report-table";
    }

    @GetMapping("/{year}")
    public String year(Model model, @PathVariable Integer year, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        Integer numberOfTransactions = transactionService.numberOfTransactionsPerYear(year, project.getId());
        Double sumOfExpenses = transactionService.sumOfExpenses(project.getId(), Date.valueOf(year + "-01-01"), Date.valueOf(year + "-12-31"));
        Double sumOfIncomes = transactionService.sumOfIncomes(project.getId(), Date.valueOf(year + "-01-01"), Date.valueOf(year + "-12-31"));
        Double balance = transactionService.balanceByDates(project.getId(), Date.valueOf(year + "-01-01"), Date.valueOf(year + "-12-31"));

        model.addAttribute("year", year);
        model.addAttribute("numberOfTransactions", numberOfTransactions);
        model.addAttribute("sumOfExpenses", sumOfExpenses);
        model.addAttribute("sumOfIncomes", sumOfIncomes);
        model.addAttribute("balance", balance);

        return "report-table";
    }
}
