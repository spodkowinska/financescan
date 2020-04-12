package info.podkowinski.sandra.financescanner.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

public class CurrentUser extends User {

    private final info.podkowinski.sandra.financescanner.user.User user;
    public CurrentUser(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       info.podkowinski.sandra.financescanner.user.User user) {
        super(username, password, authorities);
        this.user = user;
    }
    public info.podkowinski.sandra.financescanner.user.User getUser() {return user;}
}