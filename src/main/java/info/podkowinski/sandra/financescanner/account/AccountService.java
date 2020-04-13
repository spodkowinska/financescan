package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    ServletContext servletContext;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void save(Account account){ accountRepository.save(account);}

    public Account findById(Long id){
       return accountRepository.findById(id).orElse(null);
    }

    public List<Account>findByProjectId(Long id){
       return accountRepository.findByProjectId(id);
    }

    void delete(Account account){accountRepository.delete(account);}

    Long findNumberOfTransactionsPerAccount(Long accountId) {
        return accountRepository.findNumberOfTransactionsPerAccount(accountId);
    }

    boolean isNotOnlyAccount(Project project){
        if (accountRepository.numberOfAccountsPerProject(project.getId()) > 1l){
            return true;
        } else return false;
    }

    boolean hasNoTransactions(Account account){
        if (accountRepository.numberOfTransactions(account.getId()) > 0){
            return false;
        } else return true;
    }

    Long findNumberOfImportsPerAccount(Long accountId){
        return accountRepository.findNumberOfImportsPerAccount(accountId);
    }

    List <String> findImages(){
        List <String> images;
        File f = new File ( servletContext.getRealPath("/img/banks"));
        images = Arrays.asList(f.list());
        return images;
    }
}
