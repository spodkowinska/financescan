package info.podkowinski.sandra.financescanner.home;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("code", code != null ? code.toString() : "");

        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        model.addAttribute("exception", exception != null ? exception.toString() : "");

        Object exceptionType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        model.addAttribute("exceptionType", exceptionType != null ? exceptionType.toString() : "");

        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        model.addAttribute("message", message != null ? message.toString() : "");

        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        model.addAttribute("requestUri", requestUri != null ? requestUri.toString() : "");

        Object servletName = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
        model.addAttribute("servletName", servletName != null ? servletName.toString() : servletName);

        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
