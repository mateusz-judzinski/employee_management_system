package employee.management.system.controller;

import employee.management.system.entity.User;
import employee.management.system.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String getHomePage(){
        return "user/home";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("user", new User());
        return "user/login";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }

}
