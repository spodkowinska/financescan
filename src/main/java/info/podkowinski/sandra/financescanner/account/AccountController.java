package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
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
    public String addPost(@ModelAttribute Account account) {
        Project project1 = projectService.findById(2l);
        account.project = project1;
        accountService.save(account);
        return "";
    }

    @GetMapping("/list")
    public String list (Model model) {
        List<Account> accountsList = accountService.findByProjectId(2l);
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
    public String editPost(@ModelAttribute Account account) {
        Project project1 = projectService.findById(2l);
        account.project = project1;
        accountService.save(account);
        return "";
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Project project1 = projectService.findById(2l);
        Account account = accountService.findById(id);
        accountService.delete(account);
        List<Account>accountsList = accountService.findByProjectId(2l);
        model.addAttribute("accountsList", accountsList);
        return "";
    }
}
