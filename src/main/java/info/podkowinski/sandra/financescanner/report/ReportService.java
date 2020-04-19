package info.podkowinski.sandra.financescanner.report;

import info.podkowinski.sandra.financescanner.report.ReportRepository.CategoryStats;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    ReportService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }


    public Integer numberOfTransactions(String year, Long projectId){
        return reportRepository.numberOfTransactionsPerYear(projectId, year);
    }

    public Double sumOfExpenses(Long projectId, String year){
        return reportRepository.sumOfExpensesPerYear(projectId, year);
    }

    public Double sumOfIncomes(Long projectId, String year){
        return reportRepository.sumOfIncomesPerYear(projectId, year);
    }

    public Integer numberOfTransactionsPerMonth(String year, String month, Long projectId){
        return reportRepository.numberOfTransactionsPerMonth(projectId, year, month);
    }

    public Double sumOfExpensesPerMonth(Long projectId, String year, String month){
        return reportRepository.sumOfExpensesPerMonth(projectId, year, month);
    }

    public Double sumOfIncomesPerMonth(Long projectId, String year, String month) {
        return reportRepository.sumOfIncomesPerMonth(projectId, year, month);
    }

    public double balanceByMonth(Long projectId, String year, String month) {
        List<Transaction> transactionList = reportRepository.findByMonth(year, month, projectId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }

    public double balanceByYear(Long projectId, String year) {
        List<Transaction> transactionList = reportRepository.findByYear(year, projectId);
        double balance = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
        return balance;
    }
    public HashMap <Long, CategoryStats> categoriesWithStatisticsByYear(Long projectId, String year){
        List<ReportRepository.CategoryStats>categoryStats = reportRepository.categoriesWithIncomesExpensesBalanceYear(projectId, year);
        HashMap <Long, CategoryStats> categoriesWithStatistics =new HashMap<>();
        categoryStats.forEach(c->categoriesWithStatistics.put(c.categoryId, c));
        return categoriesWithStatistics;
    }

    public HashMap <Long, CategoryStats> categoriesWithStatisticsByMonth(Long projectId, String year, String month){
        List<ReportRepository.CategoryStats>categoryStats = reportRepository.categoriesWithIncomesExpensesBalanceYearMonth(projectId, year, month);
        HashMap <Long, CategoryStats> categoriesWithStatistics =new HashMap<>();
        categoryStats.forEach(c->categoriesWithStatistics.put(c.categoryId, c));
        return categoriesWithStatistics;
    }
}
