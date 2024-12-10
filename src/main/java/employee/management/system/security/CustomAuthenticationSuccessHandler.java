package employee.management.system.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";

        if(!authentication.getAuthorities().isEmpty()){
            GrantedAuthority grantedAuthority = authentication.getAuthorities().iterator().next();

            if(grantedAuthority.getAuthority().equals("ROLE_LEADER")){
                redirectUrl = "/leader-panel";
            } else if (grantedAuthority.getAuthority().equals("ROLE_SUPERVISOR")) {
                redirectUrl = "/supervisor-panel";
            }

            response.sendRedirect(redirectUrl);
        }
    }
}
