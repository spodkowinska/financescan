package info.podkowinski.sandra.financescanner.bank;

import org.springframework.stereotype.Service;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank getBankById(Long id){
       return bankRepository.getOne(id);
    }
}
