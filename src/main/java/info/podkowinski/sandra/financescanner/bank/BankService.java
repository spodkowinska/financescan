package info.podkowinski.sandra.financescanner.bank;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }
    public void save(Bank bank){ bankRepository.save(bank);}

    public Bank findBankById(Long id){
       return bankRepository.getOne(id);
    }
    public List<Bank>findByUserId(Long id){
       return bankRepository.findByUserId(id);
    }
}
