package info.podkowinski.sandra.financescanner.user;

import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String mail;

    // TODO password must have its setter which makes it always encoded
    private String password;

    private int enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private Set<Role> roles;

    public void addRole(Role role){
        this.roles.add(role);
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    List <Project> projects;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "current_project")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Project currentProject;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "friends_id")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    List<User> friends;

    LocalDateTime createdDate = LocalDateTime.now();

}
