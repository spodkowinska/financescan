package info.podkowinski.sandra.financescanner.transaction;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    ArrayList<Transaction> findAllByUserId(Long userId);

    @Query(value = "SELECT t.id FROM transactions t LEFT OUTER JOIN transactions_categories tc ON t.id = tc.transaction_id WHERE tc.categories_id IS NULL AND t.user_id=?", nativeQuery = true)
    ArrayList<Long> findAllNoncategorizedByUserId(Long userId);

    @Query(value = "SELECT * FROM transactions t WHERE t.amount<0", nativeQuery = true)
    ArrayList<Transaction>findSpendings(Long userId);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO transactions_categories (transaction_id, categories_id) VALUES (?1, ?2)", nativeQuery = true)
//    void setCategory (Long transactionId, Long categoryId);

  //  List<Transaction> findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(Date after, Date before, User user);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=? and t.categories like '%?%'", nativeQuery = true)
    List<Transaction> findByDateAndUserAndCategory
            (Date after, Date before, User user, Category category);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=?", nativeQuery = true)
    List<Transaction> findByDates(String after, String before, Long userId);

    @Query(value = "SELECT * FROM transactions_categories tc WHERE tc.categories_id =?", nativeQuery = true)
    public Transaction findByCategoryId(Long categoryId);

    @Query(value = "SELECT * FROM transactions t ORDER BY transaction_date ASC LIMIT 1", nativeQuery = true)
    Transaction findLastTransaction(Long userId);
}
