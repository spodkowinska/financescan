package info.podkowinski.sandra.financescanner.transaction;


import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.category.Category;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date transactionDate;

    String description;

    String party;

    float amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    Bank bank;

    @JoinColumn(name = "categories_id")
    @ManyToMany
    List<Category> categories;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;

    String importName;


}
