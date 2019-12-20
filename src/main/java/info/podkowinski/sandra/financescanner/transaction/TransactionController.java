package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final String path = "/Users/sandracoderslab/Desktop/simple.csv";
    private final String path2 = "/Users/sandracoderslab/Desktop/historia.csv";

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @RequestMapping("/home/btn")
    @ResponseBody
    public String homeBtn() throws IOException, ParseException, CsvValidationException {
//        User user1 = new User();
//        user1.setMail("mail@mail");
//        user1.setName("user1");
//        userService.saveUser(user1);
        User user1 = userService.findById(2l);
//        transactionService.scanDocument(path2, 0, 2,
//                3, 5, ',', 1, user1);
//        transactionService.scanDocument(path, 0, 3,
//                4, 6, ';', 0, user1);
        transactionService.assignDefaultCategoriesInTransactions(user1);
//        transactionService.assignCategoryInTransaction(user1, 1l, 6l);
        return "Udało się";
    }

    @RequestMapping("/home/sum")
    @ResponseBody
    public String sumBtn() {

        String str="2019-10-31";
        Date date1=Date.valueOf(str);
        String str2="2019-11-31";
        Date date2=Date.valueOf(str2);
        User user1 = userService.findById(2l);
        return String.valueOf(transactionService.balanceByDatesAndCategory(user1, date1, date2, 2l));
    }
}
