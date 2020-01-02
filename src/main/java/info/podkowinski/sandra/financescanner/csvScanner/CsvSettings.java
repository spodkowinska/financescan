package info.podkowinski.sandra.financescanner.csvScanner;

import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CsvSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    int datePosition;

    int descriptionPosition;

    int partyPosition;

    int amountPosition;

    char separator;

    int skipLines;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;
}
