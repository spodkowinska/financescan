package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    String logoImage = DEFAULT_ACCOUNT_IMAGE;
    String logoFilter;

    LocalDateTime created = LocalDateTime.now();

    @JoinColumn(name = "project_id")
    @ManyToOne
    Project project;

    // This is the default image used by all new accounts
    private static final String DEFAULT_ACCOUNT_IMAGE = "zzz_custom_1.png";
}
