package info.podkowinski.sandra.financescanner.transaction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final String path = "/Users/sandracoderslab/Desktop/simple.csv";

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping("/home/btn")
    @ResponseBody
    public String homeBtn() throws IOException {
        transactionService.scanDocument(path);
        return "Udało się";
    }
}
