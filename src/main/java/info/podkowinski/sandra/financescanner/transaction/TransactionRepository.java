package info.podkowinski.sandra.financescanner.transaction;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    ArrayList<Transaction> findAllByProjectIdOrderByTransactionDateAsc(Long projectId);

    @Query(value = "SELECT t.id FROM transactions t LEFT OUTER JOIN transactions_categories tc ON t.id = tc.transaction_id WHERE tc.categories_id IS NULL AND t.project_id=?", nativeQuery = true)
    ArrayList<Long> findAllNoncategorizedByProjectId(Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE t.amount<0", nativeQuery = true)
    ArrayList<Transaction>findSpendings(Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and project_id=? and t.categories like '%?%'", nativeQuery = true)
    List<Transaction> findByDateAndProjectAndCategory
            (Date after, Date before, Project project, Category category);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and project_id=? ORDER BY transaction_date ASC", nativeQuery = true)
    List<Transaction> findByDates(Date after, Date before, Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE project_id=? ORDER BY transaction_date ASC LIMIT 1", nativeQuery = true)
    Transaction findOldestTransaction(Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE project_id=?  ORDER BY transaction_date DESC LIMIT 1", nativeQuery = true)
    Transaction findLatestTransaction(Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE YEAR(t.transaction_date)=? and MONTH(t.transaction_date)=? and project_id=? ORDER BY transaction_date ASC", nativeQuery = true)
    List<Transaction> findByMonth(String year, String month, Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE YEAR(t.transaction_date)=? and project_id=? ORDER BY transaction_date ASC", nativeQuery = true)
    List<Transaction> findByYear(String year, Long projectId);

    List<Transaction> findAllByProjectId(Long projectId);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "DELETE FROM transactions_categories tc WHERE tc.categories_id =?", nativeQuery = true)
    void deleteAssignedCategoriesByCategoryId(Long categoryId);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "DELETE FROM transactions_pending_categories tpc WHERE tpc.pending_categories_id =?", nativeQuery = true)
    void deleteAssignedPendingCategoriesByCategoryId(Long categoryId);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value = "DELETE FROM transactions_rejected_categories trc WHERE trc.rejected_categories_id =?", nativeQuery = true)
    void deleteAssignedRejectedCategoriesByCategoryId(Long categoryId);

    @Query(value = "SELECT SUM(CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? ) THEN 1 ELSE 0 END) AS num_trans FROM transactions t", nativeQuery = true)
    Integer numberOfTransactionsPerYear(Long projectId, String year);

    @Query(value = "SELECT SUM(CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? AND t.amount>0) THEN t.amount ELSE 0 END) AS sum_incomes FROM transactions t", nativeQuery = true)
    Double sumOfIncomesPerYear(Long projectId, String year);

    @Query(value = "SELECT SUM( CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? AND t.amount<0) THEN t.amount ELSE 0 END) AS sum_incomes FROM transactions t", nativeQuery = true)
    Double sumOfExpensesPerYear(Long projectId, String year);

    @Query(value = "SELECT SUM(CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? AND MONTH(t.transaction_date)=?) THEN 1 ELSE 0 END) AS num_trans FROM transactions t", nativeQuery = true)
    Integer numberOfTransactionsPerMonth(Long projectId, String year, String month);

    @Query(value = "SELECT SUM(CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? AND MONTH(t.transaction_date)=? AND t.amount>0) THEN t.amount ELSE 0 END) AS sum_incomes FROM transactions t", nativeQuery = true)
    Double sumOfIncomesPerMonth(Long projectId, String year, String month);

    @Query(value = "SELECT SUM( CASE WHEN (t.project_id = ? AND YEAR(t.transaction_date)= ? AND MONTH(t.transaction_date)=? AND t.amount<0) THEN t.amount ELSE 0 END) AS sum_incomes FROM transactions t", nativeQuery = true)
    Double sumOfExpensesPerMonth(Long projectId, String year, String month);
}
