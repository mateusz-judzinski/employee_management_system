package employee.management.system.service;

import employee.management.system.entity.User;
import employee.management.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User with id: " + id + " not found");
        }
    }
}
