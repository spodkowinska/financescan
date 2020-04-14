package info.podkowinski.sandra.financescanner.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "user/user-login";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String register() {
        return "user/user-register";
    }

    @RequestMapping(value = {"/forgotpassword"}, method = RequestMethod.GET)
    public String forgotPassword() {
        return "user/user-forgot-password";
    }
}
