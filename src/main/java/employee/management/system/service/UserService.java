package employee.management.system.service;

import employee.management.system.entity.User;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);
    User findUserById(int id);
    List<User> findAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);
    boolean processLogin(String username, String password);
    List<User> findByRole(String role);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
