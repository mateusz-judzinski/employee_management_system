package employee.management.system.service;

import employee.management.system.entity.User;
import employee.management.system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User with id: " + id + "not found"));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void addUser(User user) {
        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        user.setRole("ROLE_LEADER");
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if(!user.getNewPassword().isBlank() || !user.getNewPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            user.setNewPassword("");
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User with id: " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean processLogin(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if(user != null){
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Override
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }
}
