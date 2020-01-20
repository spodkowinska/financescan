package info.podkowinski.sandra.financescanner.bank;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/account")
public class BankController {
    private final BankService bankService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final CsvSettingsService csvSettingsService;


    public BankController(BankService bankService, UserService userService, TransactionService transactionService, CsvSettingsService csvSettingsService) {
        this.bankService = bankService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.csvSettingsService = csvSettingsService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        Bank bank = new Bank();
        model.addAttribute("bank", bank);
        return "add-account";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Bank bank) {
        User user1 = userService.findById(2l);
        bankService.save(bank);
        return "redirect:../account/list";
    }

    @GetMapping("/list")
    public String list (Model model) {
        List<Bank> banksList = bankService.findByUserId(2l);
        model.addAttribute("banksList", banksList);
        return "list-accounts";
    }


}
