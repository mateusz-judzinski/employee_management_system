package employee.management.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Nazwa użytkownika nie może być pusta.")
    @Size(min = 3, max = 20, message = "Nazwa użytkownika musi mieć od 3 do 20 znaków.")
    @Column(name = "username")
    private String username;
    @NotBlank(message = "Hasło nie może być puste.")
    @Size(min = 8, max = 64, message = "Hasło musi mieć od 8 do 64 znaków.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,64}$",
            message = "Hasło musi zawierać od 8 do 64 znaków, w tym wielką i małą literę, cyfrę oraz znak specjalny."
    )
    @Column(name = "password")
    private String password;
    @Transient
    private String newPassword;
    @NotBlank(message = "Imię nie może być puste.")
    @Size(min = 2, max = 20, message = "Imię musi mieć od 2 do 20 znaków.")
    @Column(name = "first_name")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste.")
    @Size(min = 2, max = 35, message = "Nazwisko musi mieć od 2 do 35 znaków.")
    @Column(name = "last_name")
    private String lastName;
    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podaj poprawny adres email.")
    @Column(name = "email")
    private String email;
    @Pattern(regexp = "\\d{9}", message = "Numer telefonu musi zawierać dokładnie 9 cyfr.")
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "role")
    private String role;

    public User() {
    }
    public User(String username, String firstName, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        if(newPassword == null){
            return "";
        }
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
