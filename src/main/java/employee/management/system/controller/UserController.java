package employee.management.system.controller;

import employee.management.system.entity.User;
import employee.management.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            return "redirect:/home-page";
        }
        return "login-page";
    }
}
