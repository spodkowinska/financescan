package info.podkowinski.sandra.financescanner.csvScanner;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.processor.PreAssignmentProcessor;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.bank.BankService;
import info.podkowinski.sandra.financescanner.csvScanner.FormatOfNumbers;
import info.podkowinski.sandra.financescanner.transaction.Transaction;

import java.sql.Date;

public class TransactionMBank extends Transaction {

    private BankService bankService;

    public TransactionMBank(BankService bankService) {
        this.bankService = bankService;
    }

    @CsvBindByName(column = "#Data operacji")
    @CsvDate("yyyy-MM-dd")
    Date transactionDate;

    @CsvBindByName(column = "#Data ksiegowania")
    @CsvDate("yyyy-MM-dd")
    Date financeDate;

    @CsvBindByName(column = "#Opis operacji")
    String description;

    @CsvBindByName(column = "#Tytul")
    String title;

    @CsvBindByName(column = "#Nadawca/Odbiorca")
    String party;

    @CsvBindByName(column = "#Numer konta")
    String accountNumber;

    @PreAssignmentProcessor(processor = FormatOfNumbers.class, paramString = "-")
    @CsvBindByName(column = "#Kwota")
    float amount;

    @PreAssignmentProcessor(processor = FormatOfNumbers.class, paramString = "-")
    @CsvBindByName(column = "#Saldo po operacji")
    float balance;

    Bank bank = bankService.getBankById(1l);
}
