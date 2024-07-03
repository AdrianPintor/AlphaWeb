package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Role;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.RoleRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public List<User> findAll() {
      return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}