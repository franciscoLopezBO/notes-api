package com.notes_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notes_api.model.Role;
import com.notes_api.model.UserAccount;
import com.notes_api.repository.RoleRepository;
import com.notes_api.repository.UserAccountRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserAccountRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserAccountRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount registerUser(String email, String rawPassword, Long roleId) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role role = roleRepository.getById(roleId);

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    public Optional<UserAccount> authenticateUser(String email, String rawPassword) {
        Optional<UserAccount> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPasswordHash()) && user.getActive()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<UserAccount> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
