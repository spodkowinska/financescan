package info.podkowinski.sandra.financescanner.csvScanner;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvSettingsService {
    private final CsvSettingsRepository csvSettingsRepository;

    public CsvSettingsService(CsvSettingsRepository csvSettingsRepository) {
        this.csvSettingsRepository = csvSettingsRepository;
    }

    public void saveCsvSettings(CsvSettings csvSettings){
        csvSettingsRepository.save(csvSettings);
    }
    char parseToChar(String stringToParse){
        char charSeparator = stringToParse.charAt(0);
        return charSeparator;
    }
    int parseToInt(String stringToParse){
        int parsed = Integer.parseInt(stringToParse);
        return parsed;
    }
    public CsvSettings findById(Long id){
        return csvSettingsRepository.findById(id).orElse(null);
    }

    public List<CsvSettings>findByProjectId(Long projectId){
        return csvSettingsRepository.findByProjectId(projectId);
    }

    public void createDefaultBanksSettings(){
        CsvSettings csvSettingsMBank = new CsvSettings();
        csvSettingsMBank.setCsvSeparator(';');
        csvSettingsMBank.setSkipLines(38);
        csvSettingsMBank.setAmountPosition(6);
        csvSettingsMBank.setDescriptionPosition(3);
        csvSettingsMBank.setPartyPosition(4);
        csvSettingsMBank.setDatePosition(0);
        csvSettingsMBank.setName("mBank");

        CsvSettings csvSettingsMillennium = new CsvSettings();
        csvSettingsMillennium.setCsvSeparator(',');
        csvSettingsMillennium.setSkipLines(1);
        csvSettingsMillennium.setAmountPosition(7);
        csvSettingsMillennium.setDescriptionPosition(6);
        csvSettingsMillennium.setPartyPosition(5);
        csvSettingsMillennium.setDatePosition(1);
        csvSettingsMillennium.setName("Millennium");

        CsvSettings csvSettingsSantander = new CsvSettings();
        csvSettingsSantander.setCsvSeparator(',');
        csvSettingsSantander.setSkipLines(1);
        csvSettingsSantander.setAmountPosition(5);
        csvSettingsSantander.setDescriptionPosition(2);
        csvSettingsSantander.setPartyPosition(3);
        csvSettingsSantander.setDatePosition(0);
        csvSettingsSantander.setName("Santander");

        csvSettingsRepository.save(csvSettingsMBank);
        csvSettingsRepository.save(csvSettingsMillennium);
        csvSettingsRepository.save(csvSettingsSantander);
    }
}
