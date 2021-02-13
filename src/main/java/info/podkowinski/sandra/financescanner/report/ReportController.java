package info.podkowinski.sandra.financescanner.report;

import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.imports.ImportService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/report")
public class ReportController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;
    private final ImportService importService;
    private final ReportService reportService;

    public ReportController(TransactionService transactionService, ProjectService projectService, AccountService accountService,
                                 CategoryService categoryService, CsvSettingsService csvSettingsService, ImportService importService, ReportService reportService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
        this.importService = importService;
        this.reportService = reportService;
    }

    @GetMapping("")
    public String yearsReport(Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categories = categoryService.findByProjectId(project.getId());
        var yearsAndLastMonth = transactionService.findYearsAndLastMonthByProjectId(project.getId());
        model.addAttribute("categories", categories);
        model.addAttribute("years", yearsAndLastMonth.years);
        return "reports/report-table";
    }

    @GetMapping("/{year}")
    public String year(Model model, @PathVariable String year, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        Integer numberOfTransactions = reportService.numberOfTransactions(year, project.getId());
        Double sumOfExpenses = reportService.sumOfExpenses(project.getId(), String.valueOf(year));
        Double sumOfIncomes = reportService.sumOfIncomes(project.getId(), String.valueOf(year));
        Double balance = reportService.balanceByYear(project.getId(), year);
        Map<Long, ReportRepository.CategoryStats> categoriesWithStatisticsByYear = reportService.categoriesWithStatisticsByYear(project.getId(), year);

        model.addAttribute("year", Integer.parseInt(year));
        model.addAttribute("numberOfTransactions", numberOfTransactions);
        model.addAttribute("sumOfExpenses", sumOfExpenses);
        model.addAttribute("sumOfIncomes", sumOfIncomes);
        model.addAttribute("balance", balance);
        model.addAttribute("categoriesWithStatistics", categoriesWithStatisticsByYear);

        return "reports/report-stats-json";
    }

    @GetMapping("{year}/{month}")
    public String month(Model model, @PathVariable String year, @PathVariable String month, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        Integer numberOfTransactions = reportService.numberOfTransactionsPerMonth(year, month, project.getId());
        Double sumOfExpenses = reportService.sumOfExpensesPerMonth(project.getId(), year, month);
        Double sumOfIncomes = reportService.sumOfIncomesPerMonth(project.getId(),year, month);
        Double balance = reportService.balanceByMonth(project.getId(), year, month);
        Map<Long, ReportRepository.CategoryStats> categoriesWithStatisticsByMonth = reportService.categoriesWithStatisticsByMonth(project.getId(), year, month);
        boolean isValid = reportService.isValid(project.getId(), year, month);
        boolean current = reportService.isCurrent(year, month);

        model.addAttribute("current", current);
        model.addAttribute("valid", isValid);
        model.addAttribute("year", Integer.parseInt(year));
        model.addAttribute("month", Integer.parseInt(month));
        model.addAttribute("numberOfTransactions", numberOfTransactions);
        model.addAttribute("sumOfExpenses", sumOfExpenses);
        model.addAttribute("sumOfIncomes", sumOfIncomes);
        model.addAttribute("balance", balance);

        model.addAttribute("categoriesWithStatistics", categoriesWithStatisticsByMonth);

        return "reports/report-stats-json";
    }
}