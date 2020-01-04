package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @RequestMapping("/list")
    public String transaction(Model model) {
        User user1 = userService.findById(2l);
        List<Transaction> transactionsList = transactionService.findByUsersId(2l);
        model.addAttribute("transactionslist", transactionsList);
        return "transactions-list";
    }


    @RequestMapping("/home/sum")
    @ResponseBody
    public String sumBtn() {

        String str="2019-10-31";
        Date date1=Date.valueOf(str);
        String str2="2019-11-31";
        Date date2=Date.valueOf(str2);
        User user1 = userService.findById(2l);
//        return String.valueOf(transactionService.balanceByDatesAndCategory(user1, date1, date2, 2l));
        return transactionService.balancesByDatesForAllCategories(user1, date1, date2).toString();
    }
    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
