package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.User;
import employee.management.system.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervisor-panel/management")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add-user")
    public String showUserForm(Model model, @RequestParam(value = "errorMessage", required = false) String errorMessage){

        User user = new User();
        model.addAttribute("user", user);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/users/form";
    }


    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

        boolean userExists = userService.existsByUsername(user.getUsername());

        if (userExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Użytkownik o tej nazwie już istnieje.");
            return "redirect:/supervisor-panel/management/add-user";
        }

        boolean emailExists = userService.existsByEmail(user.getEmail());

        if (emailExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podany adres e-mail jest już używany.");
            return "redirect:/supervisor-panel/management/add-user";
        }

        userService.addUser(user);

        return "redirect:/supervisor-panel/management";
    }


}
