package com.example.soba2clean.service.authentication;

import com.example.soba2clean.enums.Role;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllAdmins() {
        return this.userRepository.findAllByRole(Role.ADMIN);
    }

    public void incrementLoginAttemptCounts(User user) {
        user.incrementLoginAttempts();
        this.userRepository.save(user);
    }

    public User verifyUser(User user) {
        user.markAsVerified();
        return userRepository.save(user);
    }

    public User resetLoginAttempts(User user) {
        user.setLoginAttempts(0);
        return userRepository.save(user);
    }

}
