package info.podkowinski.sandra.financescanner.imports;

import org.springframework.stereotype.Service;

@Service
public class ImportService {

    private final ImportRepository importRepository;

    public ImportService(ImportRepository importRepository){
        this.importRepository = importRepository;
    }

    public void save(Import import1){
        importRepository.save(import1);
    }

}
