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

    public List<CsvSettings>findByUserId(Long userId){
        return csvSettingsRepository.findByUserId(userId);
    }
}
