package info.podkowinski.sandra.financescanner.home;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HomeController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final CsvSettingsService csvSettingsService;
    private final AccountService accountService;

    public HomeController(TransactionService transactionService, UserService userService, AccountService accountService, CsvSettingsService csvSettingsService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.csvSettingsService = csvSettingsService;
        this.accountService = accountService;
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }
//
//    @PostMapping("/home")
//    @ResponseBody
//    public String homePost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
//        Part filePart = request.getPart("fileToUpload");
//        User user1 = userService.findById(2l);
//        CsvSettings mBankSettings = csvSettingsService.findById(1l);
//        CsvSettings santanderSettings = csvSettingsService.findById(2l);
////        transactionService.scanDocument(filePart.getInputStream(), mBankSettings.getDatePosition(), mBankSettings.getDescriptionPosition(),
////                mBankSettings.getPartyPosition(), mBankSettings.getAmountPosition(), mBankSettings.getCsvSeparator(), mBankSettings.getSkipLines(), 1l, user1);
////        transactionService.scanDocument(filePart.getInputStream(), santanderSettings.getDatePosition(), santanderSettings.getDescriptionPosition(),
////                santanderSettings.getPartyPosition(), santanderSettings.getAmountPosition(), santanderSettings.getCsvSeparator(), santanderSettings.getSkipLines(), 2l, user1);
//        return "good";
  //  }

    @GetMapping("/report")
    public String present(Model model) {
//        User user2 = userService.findById(2l);
//        String str = "2019-10-31";
//        Date date1 = Date.valueOf(str);
//        String str2 = "2019-11-31";
//        Date date2 = Date.valueOf(str2);
        List<Transaction> allTransactions = transactionService.findByUserId(2l);
        Map<String, Double> lastYearBalances = transactionService.lastYearBalances(2l);
        Map<String, Float> categoriesAndAmounts = transactionService.mapExpensesToCategoriesWithAmounts(allTransactions, 2l);
        model.addAttribute("categoriesWithAmounts", categoriesAndAmounts);
        model.addAttribute("lastYearBalances", lastYearBalances);
        return "report";
    }


}

