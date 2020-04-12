package info.podkowinski.sandra.financescanner.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account>findByProjectId(Long id);

    @Query(value = "SELECT Count(*) FROM transactions t WHERE t.account_id = ?", nativeQuery = true)
    Long findNumberOfTransactionsPerAccount(Long id);

}
