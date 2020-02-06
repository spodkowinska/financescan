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

    @Query(value = "SELECT * FROM transactions t, transactions_categories tc WHERE t.id not in (tc.transaction_id) and t.user_id =?", nativeQuery = true)
    ArrayList<Transaction> findAllNoncategorizedByUserId(Long userId);

  //  List<Transaction> findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(Date after, Date before, User user);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=? and t.categories like '%?%'", nativeQuery = true)
    List<Transaction> findByDateAndUserAndCategory
            (Date after, Date before, User user, Category category);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=?", nativeQuery = true)
    List<Transaction> findByDates(Date after, Date before, Long userId);

    @Query(value = "SELECT * FROM transactions_categories tc WHERE tc.categories_id =?", nativeQuery = true)
    public Transaction findByCategoryId(Long categoryId);
}
