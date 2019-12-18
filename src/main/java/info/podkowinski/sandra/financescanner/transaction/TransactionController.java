package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final String path = "/Users/sandracoderslab/Desktop/simple.csv";
    private final String path2 = "/Users/sandracoderslab/Desktop/historia.csv";

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping("/home/btn")
    @ResponseBody
    public String homeBtn() throws IOException, ParseException, CsvValidationException {
        transactionService.scanDocument(path2, 0, 2, 3, 5, ',',1);
        transactionService.scanDocument(path, 0, 3, 4, 6, ';', 0);
        return "Udało się";
    }
}
