package employee.management.system.controller.supervisor.management;

import employee.management.system.entity.User;
import employee.management.system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/supervisor-panel/management")
public class UserManagementController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserManagementController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
    public String addUser(@ModelAttribute @Valid User user,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/add-user";
        }

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

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id,
                             @RequestParam("password") String password,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        User currentUser = userService.findUserByUsername(username);

        if (currentUser == null || !passwordEncoder.matches(password, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podane hasło jest nieprawidłowe. Usunięcie nie zostało wykonane.");
            return "redirect:/supervisor-panel/management";
        }

        User user = userService.findUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono użytkownika.");
            return "redirect:/supervisor-panel/management";
        }

        userService.deleteUserById(id);
        return "redirect:/supervisor-panel/management";
    }

    @GetMapping("/edit-user/{id}")
    public String showEditUserForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes,
                                   @RequestParam(value = "errorMessage", required = false) String errorMessage) {

        User user = userService.findUserById(id);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono użytkownika o ID: " + id);
            return "redirect:/error-handler";
        }

        model.addAttribute("user", user);

        if(errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "supervisor/users/edit";
    }

    @PostMapping("/edit-user")
    public String updateUser(@ModelAttribute @Valid User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("id", user.getId());
            redirectAttributes.addFlashAttribute("errorMessage", firstErrorMessage);
            return "redirect:/supervisor-panel/management/edit-user/{id}";
        }

        User existingUser = userService.findUserById(user.getId());

        if (existingUser == null) {
            redirectAttributes.addAttribute("id", user.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Nie znaleziono użytkownika o ID: " + user.getId());
            return "redirect:/supervisor-panel/management/edit-user/{id}";
        }

        boolean isUsernameOccupied = userService.isUsernameOccupied(user);
        if (isUsernameOccupied) {
            redirectAttributes.addAttribute("id", user.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Podana nazwa użytkownika jest już zajęta.");
            return "redirect:/supervisor-panel/management/edit-user/{id}";
        }

        boolean isEmailOccupied = userService.isEmailOccupied(user);
        if (isEmailOccupied) {
            redirectAttributes.addAttribute("id", user.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Podany adres e-mail jest już zajęty.");
            return "redirect:/supervisor-panel/management/edit-user/{id}";
        }

        if (!user.getNewPassword().isBlank()) {
            if (!isValidPassword(user.getNewPassword())) {
                redirectAttributes.addAttribute("id", user.getId());
                redirectAttributes.addFlashAttribute("errorMessage", "Nowe hasło musi mieć od 8 do 64 znaków, w tym wielką i małą literę, cyfrę oraz znak specjalny.");
                return "redirect:/supervisor-panel/management/edit-user/{id}";
            }
            existingUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
        } else {
            existingUser.setPassword(user.getPassword());
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        userService.updateUser(existingUser);

        return "redirect:/supervisor-panel/management";
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,64}$";
        return password.matches(passwordRegex);
    }

}
