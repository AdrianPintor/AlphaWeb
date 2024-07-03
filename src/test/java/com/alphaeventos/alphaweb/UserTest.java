package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.Role;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.RoleRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.UserService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndFindUser() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));

        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals("Enterprise SL", users.get(0).getEnterpriseName());
    }
}

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindAll() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        userRepository.save(user);

        List<User> users = userService.findAll();
        assertFalse(users.isEmpty());
        assertEquals("Enterprise SL", users.get(0).getEnterpriseName());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user = userRepository.save(user);

        Optional<User> foundUser = userService.findById(user.getId());
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

    @Test
    public void testDeleteById() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user = userRepository.save(user);

        userService.deleteById(user.getId());
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private User testUser;

    @BeforeEach
    void setup() throws MalformedURLException {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEnterpriseName("Enterprise SL");
        testUser.setPersonalName("Ramoncin Perez");
        testUser.setEmail("ramoncin.perez@example.com");
        testUser.setUsername("ramoncin.perez@example.com");
        testUser.setPassword("futbol2000");
        testUser.setTelephoneContact(672456464);
        testUser.setAddress("Calle Goya, 5");
        testUser.setRrss(new URL("http://RPerez.com"));

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);

        testUser.setRoles(Set.of(userRole));
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<User[]> response = restTemplate.withBasicAuth(testUser.getUsername(), "futbol2000")
                .getForEntity(createURLWithPort("/users"), User[].class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User[] users = response.getBody();
        assertNotNull(users);
        assertEquals(1, users.length);
        assertEquals("Enterprise SL", users[0].getEnterpriseName());
        assertEquals("http://RPerez.com", users[0].getRrss().toString());
    }

    @Test
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.withBasicAuth(testUser.getUsername(), testUser.getPassword())
                .getForEntity(createURLWithPort("/users/" + testUser.getId()), User.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://RPerez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testCreateUser() throws MalformedURLException {
        User newUser = new User();
        newUser.setEnterpriseName("New Enterprise SL");
        newUser.setPersonalName("Luis Lopez");
        newUser.setEmail("luis.lopez@example.com");
        newUser.setUsername("luis.lopez@example.com");
        newUser.setPassword("password123");
        newUser.setTelephoneContact(671234567);
        newUser.setAddress("Calle Serrano, 10");
        newUser.setRrss(new URL("http://LuisLopez.com"));

        ResponseEntity<User> response = restTemplate.withBasicAuth(testUser.getUsername(), testUser.getPassword())
                .postForEntity(createURLWithPort("/users"), newUser, User.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("New Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://LuisLopez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testUpdateUser() throws MalformedURLException {
        testUser.setEnterpriseName("Updated Enterprise SL");
        testUser.setRrss(new URL("http://updatedRPerez.com"));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(testUser, headers);

        ResponseEntity<User> response = restTemplate.withBasicAuth(testUser.getUsername(), testUser.getPassword())
                .exchange(createURLWithPort("/users/" + testUser.getId()), HttpMethod.PUT, entity, User.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("Updated Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://updatedRPerez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testDeleteUser() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.withBasicAuth(testUser.getUsername(), testUser.getPassword())
                .exchange(createURLWithPort("/users/" + testUser.getId()), HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
        assertFalse(userRepository.findById(testUser.getId()).isPresent());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
