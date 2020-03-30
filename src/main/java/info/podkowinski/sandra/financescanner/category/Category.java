package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.keyword.Keyword;
import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Category {

    public Category(String name, String description, Keyword keyword, String color, User user) {
        this.name = name;
        this.description = description;
        this.keywords.add(keyword);
        this.color = color;
        this.user = user;

    }
    public Category(String name, String description, String color, User user) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.user = user;

    }

    public Category() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public
    String name;

    String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy ="category")
    List<Keyword> keywords;

    void addKeyword(Keyword keyword){
        keywords.add(keyword);
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String color;


}
