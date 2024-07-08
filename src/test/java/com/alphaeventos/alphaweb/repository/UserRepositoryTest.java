package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
