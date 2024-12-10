package employee.management.system.controller;

import employee.management.system.entity.User;
import employee.management.system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(){
        return "home-page";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        model.addAttribute("user", new User());
        return "login-page";
    }

    @PostMapping("/process-login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password){

        if(userService.processLogin(username, password)){
            return "redirect:/leader-panel";
        }
        return "login-page";
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }

    @GetMapping("/leader-panel")
    public String showLeaderPanelPage(){
        return "leader-panel-page";
    }

    @GetMapping("/supervisor-panel")
    public String showSupervisorPanelPage(){
        return "supervisor-panel-page";
    }

}
