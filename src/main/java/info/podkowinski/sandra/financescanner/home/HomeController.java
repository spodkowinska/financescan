package info.podkowinski.sandra.financescanner.home;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.text.ParseException;

@Controller
public class HomeController {

    private final TransactionService transactionService;
    private final UserService userService;

    public HomeController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/home")
    @ResponseBody
    public String homePost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        User user1 = userService.findById(1l);
        transactionService.scanDocument(filePart.getInputStream(), 0, 2,
                3, 5, ';', 40, user1);
        return "good";
    }
}
