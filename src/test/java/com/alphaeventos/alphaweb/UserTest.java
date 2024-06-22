package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
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
    public void testSave() {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");

        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals("Enterprise SL", savedUser.getEnterpriseName());
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
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));
        userRepository.save(user);

        ResponseEntity<User[]> response = restTemplate.getForEntity(createURLWithPort("/users"), User[].class);

        assertEquals(200, response.getStatusCodeValue());
        User[] users = response.getBody();
        assertNotNull(users);
        assertEquals(1, users.length);
        assertEquals("Enterprise SL", users[0].getEnterpriseName());
        assertEquals("http://RPerez.com", users[0].getRrss().toString());
    }

    @Test
    public void testGetUserById() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));
        user = userRepository.save(user);

        ResponseEntity<User> response = restTemplate.getForEntity(createURLWithPort
                ("/users/" + user.getId()), User.class);

        assertEquals(200, response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://RPerez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testCreateUser() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));

        ResponseEntity<User> response = restTemplate.postForEntity(createURLWithPort
                ("/users"), user, User.class);

        assertEquals(201, response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://RPerez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testUpdateUser() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));
        user = userRepository.save(user);

        user.setEnterpriseName("Updated Enterprise SL");
        user.setRrss(new URL("http://updatedRPerez.com"));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<User> response = restTemplate.exchange(createURLWithPort
                ("/users/" + user.getId()), HttpMethod.PUT, entity, User.class);

        assertEquals(200, response.getStatusCodeValue());
        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals("Updated Enterprise SL", returnedUser.getEnterpriseName());
        assertEquals("http://updatedRPerez.com", returnedUser.getRrss().toString());
    }

    @Test
    public void testDeleteUser() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));
        user = userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort
                ("/users/" + user.getId()), HttpMethod.DELETE, entity, Void.class);

        assertEquals(204, response.getStatusCodeValue());
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}