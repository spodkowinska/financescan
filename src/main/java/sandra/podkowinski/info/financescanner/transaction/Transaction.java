package sandra.podkowinski.info.financescanner.transaction;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import sandra.podkowinski.info.financescanner.bank.Bank;
import sandra.podkowinski.info.financescanner.category.Category;
import sandra.podkowinski.info.financescanner.csvScanner.FormatOfNumbers;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

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


    @CsvCustomBindByName(column = "#Kwota", converter = FormatOfNumbers.class)
    float amount;

    @CsvBindByName(column = "#Saldo po operacji")
    float saldo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    Bank bank;

    @ManyToOne
    Category category;


}
