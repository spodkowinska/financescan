package info.podkowinski.sandra.financescanner.transaction;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.processor.PreAssignmentProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.csvScanner.FormatOfNumbers;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    Bank bank;

    @ManyToOne
    Category category;


}
