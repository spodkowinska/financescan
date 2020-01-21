package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String number;
    String institutionName;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;

}