package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.ValidationException;
import java.io.IOException;
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
        User user1 = new User();
        user1.setMail("mail@mail");
        user1.setName("user1");
        userService.saveUser(user1);
        transactionService.scanDocument(path2, 0, 2,
                3, 5, ',', 1, user1);
        transactionService.scanDocument(path, 0, 3,
                4, 6, ';', 0, user1);
        return "Udało się";
    }
}
