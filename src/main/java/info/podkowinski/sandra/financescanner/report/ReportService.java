package info.podkowinski.sandra.financescanner.report;

import info.podkowinski.sandra.financescanner.report.ReportRepository.CategoryStats;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final TransactionRepository transactionRepository;

    ReportService(ReportRepository reportRepository, TransactionRepository transactionRepository){
        this.reportRepository = reportRepository;
        this.transactionRepository = transactionRepository;
    }

    public Integer numberOfTransactions(String year, Long projectId){
        return transactionRepository.numberOfTransactionsPerYear(projectId, year);
    }

    public Double sumOfExpenses(Long projectId, String year){
        return transactionRepository.sumOfExpensesPerYear(projectId, year);
    }

    public Double sumOfIncomes(Long projectId, String year){
        return transactionRepository.sumOfIncomesPerYear(projectId, year);
    }

    public Integer numberOfTransactionsPerMonth(String year, String month, Long projectId){
        return transactionRepository.numberOfTransactionsPerMonth(projectId, year, month);
    }

    public Double sumOfExpensesPerMonth(Long projectId, String year, String month){
        return transactionRepository.sumOfExpensesPerMonth(projectId, year, month);
    }

    public Double sumOfIncomesPerMonth(Long projectId, String year, String month) {
        return transactionRepository.sumOfIncomesPerMonth(projectId, year, month);
    }

    public double balanceByMonth(Long projectId, String year, String month) {
        List<Transaction> transactionList = transactionRepository.findByMonth(year, month, projectId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByYear(Long projectId, String year) {
        List<Transaction> transactionList = transactionRepository.findByYear(year, projectId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }
    public HashMap <Long, CategoryStats> categoriesWithStatisticsByYear(Long projectId, String year){
        List<Object[]>categoryStats = reportRepository.categoriesWithIncomesExpensesBalanceYear(projectId, year);
        HashMap<Long, CategoryStats> categoriesWithStatistics = new HashMap<>();
        categoryStats.forEach(c->categoriesWithStatistics.put(c[0] == null ? null : ((BigInteger)c[0]).longValue(), new CategoryStats(c)));
        return categoriesWithStatistics;
    }

    public HashMap <Long, CategoryStats> categoriesWithStatisticsByMonth(Long projectId, String year, String month){
        List<Object[]>categoryStats = reportRepository.categoriesWithIncomesExpensesBalanceYearMonth(projectId, year, month);
        HashMap<Long, CategoryStats> categoriesWithStatistics =new HashMap<>();
        categoryStats.forEach(c->categoriesWithStatistics.put(c[0] == null ? null : ((BigInteger)c[0]).longValue(), new CategoryStats(c)));
        return categoriesWithStatistics;
    }
}
