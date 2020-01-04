package info.podkowinski.sandra.financescanner.home;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
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
import java.util.List;

@Controller
public class HomeController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final CsvSettingsService csvSettingsService;

    public HomeController(TransactionService transactionService, UserService userService, CsvSettingsService csvSettingsService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.csvSettingsService = csvSettingsService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home")
    @ResponseBody
    public String homePost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        User user1 = userService.findById(1l);
        CsvSettings mBankSettings = csvSettingsService.findById(1l);
        CsvSettings santanderSettings = csvSettingsService.findById(2l);
        transactionService.scanDocument(filePart.getInputStream(), mBankSettings.getDatePosition(), mBankSettings.getDescriptionPosition(),
                mBankSettings.getPartyPosition(), mBankSettings.getAmountPosition(), mBankSettings.getCsvSeparator(), mBankSettings.getSkipLines(), user1);
//        transactionService.scanDocument(filePart.getInputStream(), santanderSettings.getDatePosition(), santanderSettings.getDescriptionPosition(),
//                santanderSettings.getPartyPosition(), santanderSettings.getAmountPosition(), santanderSettings.getCsvSeparator(), santanderSettings.getSkipLines(), user1);
        return "good";
    }

    @GetMapping("/fileimport")
    public String fileimport(Model model) {
        User user1 = userService.findById(1l);
        List<CsvSettings> csvSettingsList = csvSettingsService.findSettings(user1);
        model.addAttribute("csvSettingsList", csvSettingsList);
        return "file-import";
    }

    @PostMapping("/fileimport")
    @ResponseBody
    public String fileimportPost(HttpServletRequest request) throws IOException, ServletException, ParseException, CsvValidationException {
        Part filePart = request.getPart("fileToUpload");
        User user1 = userService.findById(1l);
        int datePosition = Integer.parseInt(request.getParameter("datePosition"));
        int descriptionPosition = Integer.parseInt(request.getParameter("descriptionPosition"));
        int partyPosition = Integer.parseInt(request.getParameter("partyPosition"));
        int amountPosition = Integer.parseInt(request.getParameter("amountPosition"));
        int skippedLines = Integer.parseInt(request.getParameter("skipLines"));
        char separator = request.getParameter("separator").charAt(0);
        transactionService.scanDocument(filePart.getInputStream(), datePosition, descriptionPosition,
                partyPosition, amountPosition, separator, skippedLines, user1);
        return "good";
    }
}

