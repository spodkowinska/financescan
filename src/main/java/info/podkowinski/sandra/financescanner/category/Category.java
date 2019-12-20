package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    @Column(nullable = false)
    String keywords;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
