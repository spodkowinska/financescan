package info.podkowinski.sandra.financescanner.bank;

import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public String add(Model model){
        Bank bank = new Bank();
        model.addAttribute("bank", bank);
        return "add-bank";
    }



}
