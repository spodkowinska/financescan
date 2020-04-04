package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.user.User;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Category {

    public Category(String name, String description, List<String> keywords, String color, String fontColor, User user) {
        this.name = name;
        this.description = description;
        this.keywords = keywords;
        this.color = color;
        this.fontColor = fontColor;
        this.user = user;
    }

    public Category(String name, String description, String color, String fontColor, User user) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.fontColor = fontColor;
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

    @ElementCollection
    List<String> keywords;

    @ElementCollection
    List<String> safeKeywords;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(length = 7)
    String color;
    
    @Column(length = 7)
    String fontColor;
}
