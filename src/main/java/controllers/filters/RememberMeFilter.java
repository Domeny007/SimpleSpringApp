package controllers.filters;

import controllers.SecurityController;
import database.entity.User;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@WebFilter(urlPatterns = {"/","/login"})
public class RememberMeFilter implements Filter {
    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        config.getServletContext().log("Initializing RememberMeFilter");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Principal principal = req.getUserPrincipal();

        if (principal != null && principal instanceof RememberMeAuthenticationToken) {
            resp.sendRedirect(req.getContextPath() + "/403");
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
        config.getServletContext().log("Destroying RememberMeFilter");
    }
}