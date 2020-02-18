package info.podkowinski.sandra.financescanner.csvScanner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class CsvSettingsController {
    private final CsvSettingsService csvSettingsService;

    public CsvSettingsController(info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService csvSettingsService) {
        this.csvSettingsService = csvSettingsService;
    }

    @GetMapping("/csvsettings")
    public String csvsettings(Model model) {
        CsvSettings csvSettings = new CsvSettings();
        model.addAttribute("csvSettings", csvSettings);
        return "csv-settings";
    }

    @PostMapping("/csvsettings")
    public String csvsettingsForm(@Valid @ModelAttribute CsvSettings csvSettings) {
        csvSettingsService.saveCsvSettings(csvSettings);
        return "csv-settings";
    }

    @PostMapping("file/csvsettings")
    public String fileCsvsettingsForm(HttpServletRequest request) {
        CsvSettings csvSettings = new CsvSettings();
        csvSettings.setAmountPosition(Integer.parseInt(request.getParameter("datePosition"))-1);
        csvSettings.setDescriptionPosition(Integer.parseInt(request.getParameter("descriptionPosition")) - 1);
        csvSettings.setPartyPosition(Integer.parseInt(request.getParameter("partyPosition")) - 1);
        csvSettings.setAmountPosition(Integer.parseInt(request.getParameter("amountPosition")) - 1);
        csvSettings.setSkipLines(Integer.parseInt(request.getParameter("skipLines")));
        csvSettings.setCsvSeparator(request.getParameter("separator").charAt(0));
        csvSettingsService.saveCsvSettings(csvSettings);
        return "csv-settings";
    }
}
