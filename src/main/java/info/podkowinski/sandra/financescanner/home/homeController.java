package info.podkowinski.sandra.financescanner.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class homeController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }


}
