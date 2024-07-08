package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Role;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAll() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals("Enterprise SL", users.get(0).getEnterpriseName());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("Enterprise SL", foundUser.get().getEnterpriseName());
    }

    @Test
    public void testSave() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setUsername("ramoncin.perez");
        user.setPassword("futbol2000");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));

        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals("Enterprise SL", savedUser.getEnterpriseName());
        assertEquals("Ramoncin Perez", savedUser.getPersonalName());
        assertEquals("ramoncin.perez", savedUser.getUsername());
        assertEquals("ramoncin.perez@example.com", savedUser.getEmail());
        assertEquals(672456464, savedUser.getTelephoneContact());
        assertEquals("Calle Goya, 5", savedUser.getAddress());
        assertEquals(new URL("http://RPerez.com"), savedUser.getRrss());
    }
}