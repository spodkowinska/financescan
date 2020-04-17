package info.podkowinski.sandra.financescanner.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {
    @ModelAttribute("user")
    public User user(@AuthenticationPrincipal CurrentUser currentUser) {
        return currentUser == null ? null : currentUser.getUser();
    }
}