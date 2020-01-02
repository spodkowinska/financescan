package info.podkowinski.sandra.financescanner.csvScanner;

import org.springframework.stereotype.Service;

@Service
public class CsvSettingsService {
    private final CsvSettingsRepository csvSettingsRepository;

    public CsvSettingsService(CsvSettingsRepository csvSettingsRepository) {
        this.csvSettingsRepository = csvSettingsRepository;
    }

    public void saveCsvSettings(CsvSettings csvSettings){
        csvSettingsRepository.save(csvSettings);
    }
}
