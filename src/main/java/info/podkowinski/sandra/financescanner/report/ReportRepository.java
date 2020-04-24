package info.podkowinski.sandra.financescanner.report;

import info.podkowinski.sandra.financescanner.transaction.Transaction;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

    @Repository
    public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    @Getter
    public class CategoryStats {
        CategoryStats(Long categoryId, Double income, Double outcome, Double balance) {
            this.categoryId = categoryId;
            this.income = income;
            this.outcome = outcome;
            this.balance = balance;
        }

        CategoryStats(Object[] objects) {
            this.categoryId = objects[0] == null ? null : ((BigInteger)objects[0]).longValue();
            this.income = (Double)objects[1];
            this.outcome = (Double)objects[2];
            this.balance = (Double)objects[3];
        }

        public Long categoryId;
        public Double income, outcome, balance;
    }

    @Query(value = "select categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2)\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    public List<Object[]> categoriesWithIncomesExpensesBalance(Long projectId);

    @Query(value = "select categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2)\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? and YEAR(t.transaction_date)= ? group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    List<Object[]> categoriesWithIncomesExpensesBalanceYear(Long projectId, String year);

    @Query(value = "select categories_id,\n" +
            "    round(sum(if (divided_amount > 0, divided_amount, 0)), 2),\n" +
            "    round(sum(if (divided_amount < 0, divided_amount, 0)), 2),\n" +
            "    round(sum(divided_amount), 2)\n" +
            "from transactions_categories c\n" +
            "         right join (\n" +
            "    select t.id as trans_id, coalesce(t.amount / count(c.categories_id), t.amount) as divided_amount\n" +
            "    from transactions t left join transactions_categories c on t.id = c.transaction_id\n" +
            "    where t.project_id = ? and YEAR(t.transaction_date)= ? and MONTH (t.transaction_date)= ?  group by t.id order by t.id\n" +
            ") t\n" +
            "on c.transaction_id = t.trans_id\n" +
            "group by c.categories_id\n" +
            "order by categories_id asc;\n", nativeQuery = true)
    List<Object[]> categoriesWithIncomesExpensesBalanceYearMonth(Long projectId, String year, String month);
}
