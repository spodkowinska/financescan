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

    @Query(value = "SELECT Count(*) FROM accounts a WHERE a.project_id = ?", nativeQuery = true)
    Long numberOfAccountsPerProject(Long id);

    @Query(value = "SELECT Count(*) FROM transactions t WHERE t.account_id = ?", nativeQuery = true)
    Long numberOfTransactions(Long accountId);

    @Query(value = "SELECT COUNT(DISTINCT t.import_id) AS amountImports FROM transactions t WHERE t.account_id = ?;", nativeQuery = true)
    Long findNumberOfImportsPerAccount(Long id);
}
