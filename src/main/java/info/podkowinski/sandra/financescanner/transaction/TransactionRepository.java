package info.podkowinski.sandra.financescanner.transaction;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.sql.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM transactions t WHERE t.category_id is null and t.user_id =?", nativeQuery = true)
    List<Transaction> findAllNoncategorizedByUserId(Long userId);

    List<Transaction> findAllByTransactionDateAfterAndTransactionDateBeforeAndUser(Date after, Date before, User user);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=? and t.categories like '%?%'", nativeQuery = true)
    List<Transaction> findByDateAndUserAndCategory
            (Date after, Date before, User user, Category category);

    @Query(value = "SELECT * FROM transactions t WHERE t.transaction_date>=? and t.transaction_date<=? and user_id=?", nativeQuery = true)
    List<Transaction> findByDates(Date after, Date before, User user);
}
