package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.User;
import employee.management.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supervisor-panel/users")
public class UserManagementController {

    private final UserService userService;

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.findByRole("ROLE_LEADER");
        model.addAttribute("users", users);
        return "supervisor/user/list";
    }

    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "supervisor/user/form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/supervisor-panel/users";
    }

    @GetMapping("/edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        return "supervisor/user/edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/supervisor-panel/users";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUserById(userId);
        return "redirect:/supervisor-panel/users";
    }
}
