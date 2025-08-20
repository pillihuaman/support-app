package pillihuaman.com.pe.support.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardingController {

    @RequestMapping(value = { "/{path:[^\\.]*}", "/{path:^(?!api$).*}/**" })
    public String forward(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api") || uri.startsWith("/v1") ||
                uri.startsWith("/support") || uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs")) {
            return null; // deja que lo maneje Spring
        }
        return "forward:/index.html";
    }

    @RequestMapping("/")
    public String forwardRoot() {
        return "forward:/index.html";
    }
}
