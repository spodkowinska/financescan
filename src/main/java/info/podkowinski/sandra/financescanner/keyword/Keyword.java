package info.podkowinski.sandra.financescanner.keyword;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "keywords",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Keyword {

    Keyword (String name, Category category, User user) {
        this.name = name;
        this.category = category;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public
    String name;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
