package info.podkowinski.sandra.financescanner.csvScanner;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "csv_settings")
public class CsvSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    int datePosition;

    int descriptionPosition;

    int partyPosition;

    int amountPosition;

    char csvSeparator;

    int skipLines;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;

    @JoinColumn(name = "account_id")
    @ManyToOne
    Account account;


}
