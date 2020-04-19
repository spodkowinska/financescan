package info.podkowinski.sandra.financescanner.report;

import info.podkowinski.sandra.financescanner.transaction.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReportRepository {


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

    @Query(value = "SELECT * FROM transactions t WHERE YEAR(t.transaction_date)=? and MONTH(t.transaction_date)=? and project_id=? ORDER BY transaction_date ASC", nativeQuery = true)
    List<Transaction> findByMonth(String year, String month, Long projectId);

    @Query(value = "SELECT * FROM transactions t WHERE YEAR(t.transaction_date)=? and project_id=? ORDER BY transaction_date ASC", nativeQuery = true)
    List<Transaction> findByYear(String year, Long projectId);

    public class CategoryStats {
        CategoryStats(Long categoryId, Double income, Double outcome, Double balance) {
            this.categoryId = categoryId;
            this.income = income;
            this.outcome = outcome;
            this.balance = balance;
        }
        public Long categoryId;
        public Double income, outcome, balance;
    }

    @Query(value = "select new info.podkowinski.sandra.financescanner.transaction.TransactionRepository.CategoryStats(\n" +
            "    categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2))\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    public List<CategoryStats> categoriesWithIncomesExpensesBalance(Long projectId);

    @Query(value = "select new info.podkowinski.sandra.financescanner.transaction.TransactionRepository.CategoryStats(\n" +
            "    categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2))\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? and YEAR(t.transaction_date)= ? group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    List<CategoryStats> categoriesWithIncomesExpensesBalanceYear(Long projectId, String year);

    @Query(value = "select new info.podkowinski.sandra.financescanner.transaction.TransactionRepository.CategoryStats(\n" +
            "    categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2))\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? and YEAR(t.transaction_date)= ? and MONTH (t.transaction_date)= ?  group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    List<CategoryStats> categoriesWithIncomesExpensesBalanceYearMonth(Long projectId, String year, String month);
}
