package info.podkowinski.sandra.financescanner.csvScanner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CsvSettingsRepository extends JpaRepository<CsvSettings, Long> {

    List<CsvSettings>findByProjectId(Long projectId);
}
