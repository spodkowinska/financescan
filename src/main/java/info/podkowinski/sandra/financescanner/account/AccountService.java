package info.podkowinski.sandra.financescanner.account;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public void save(Account account){ accountRepository.save(account);}

    public Account findById(Long id){
       return accountRepository.getOne(id);
    }
    public List<Account>findByUserId(Long id){
       return accountRepository.findByUserId(id);
    }
    void delete(Account account){accountRepository.delete(account);}
}
