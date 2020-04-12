package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final ProjectService projectService;
    private final TransactionService transactionService;
    private final CsvSettingsService csvSettingsService;

    public AccountController(AccountService accountService, ProjectService projectService, TransactionService transactionService, CsvSettingsService csvSettingsService) {
        this.accountService = accountService;
        this.projectService = projectService;
        this.transactionService = transactionService;
        this.csvSettingsService = csvSettingsService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "accounts/account-edit";
    }

    @ResponseBody
    @PostMapping("/add")
    public String addPost(@ModelAttribute Account account, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        account.project = project;
        accountService.save(account);
        return "";
    }

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Account> accountsList = accountService.findByProjectId(project.getId());
        model.addAttribute("accountsList", accountsList);
        return "accounts/account-table";
    }

    @GetMapping("/edit/{accountId}")
    public String edit(Model model, @PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        model.addAttribute("account", account);
        return "accounts/account-edit";
    }

    @ResponseBody
    @PostMapping("/edit/{accountId}")
    public String editPost(@ModelAttribute Account account, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        account.project = project;
        accountService.save(account);
        return "";
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Account account = accountService.findById(id);
        if (project.getId() == account.project.getId() && accountService.isNotOnlyAccount(project) &&
                accountService.hasNoTransactions(account)) {
                accountService.delete(account);
                List<Account> accountsList = accountService.findByProjectId(project.getId());
                model.addAttribute("accountsList", accountsList);
            }
        return "";
    }

    @ResponseBody
    @GetMapping("/numberoftransactions/{accountId}")
    public String numberOfTransactions(@PathVariable Long accountId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        if (project.getId() == accountService.findById(accountId).project.getId()) {
            Long numberOTransactionsPerAccount = accountService.findNumberOfTransactionsPerAccount(accountId);
            return numberOTransactionsPerAccount + "";
        }
        return "";
    }
}
