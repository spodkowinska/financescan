package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final CsvSettingsService csvSettingsService;


    public AccountController(AccountService accountService, UserService userService, TransactionService transactionService, CsvSettingsService csvSettingsService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.csvSettingsService = csvSettingsService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "add-account";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Account account) {
        User user1 = userService.findById(2l);
        account.user=user1;
        accountService.save(account);
        return "redirect:../account/list";
    }

    @GetMapping("/list")
    public String list (Model model) {
        List<Account> accountsList = accountService.findByUserId(2l);
        model.addAttribute("accountsList", accountsList);
        return "list-accounts";
    }

    @GetMapping("/edit/{accountId}")
    public String edit(Model model, @PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        model.addAttribute("account", account);
        return "edit-account";
    }

    @PostMapping("/edit/{accountId}")
    public String editPost(@ModelAttribute Account account) {
//        User user1 = userService.findById(2l);
//        account.user=user1;
        Account account1= account;
        accountService.save(account1);
        return "redirect:../account/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        Account account = accountService.findById(id);
        accountService.delete(account);
        List<Account>accountsList = accountService.findByUserId(2l);
        model.addAttribute("accountsList", accountsList);
        return "redirect:../list";
    }


}
