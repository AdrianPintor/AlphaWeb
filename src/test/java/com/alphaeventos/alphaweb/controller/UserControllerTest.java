package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setEnterpriseName("Enterprise SL");
        user.setPersonalName("Ramoncin Perez");
        user.setEmail("ramoncin.perez@example.com");
        user.setUsername("ramoncin.perez@example.com");
        user.setPassword("futbol2000");
        user.setTelephoneContact(672456464);
        user.setAddress("Calle Goya, 5");
        user.setRrss(new URL("http://RPerez.com"));
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Enterprise SL"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Enterprise SL"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setEnterpriseName("New Enterprise SL");
        newUser.setPersonalName("Juan Perez");
        newUser.setEmail("juan.perez@example.com");
        newUser.setUsername("juan.perez@example.com");
        newUser.setPassword("password123");
        newUser.setTelephoneContact(123456789);
        newUser.setAddress("Calle Luna, 10");
        newUser.setRrss(new URL("http://JPerez.com"));
        String body = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Enterprise SL"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser() throws Exception {
        user.setEnterpriseName("Updated Enterprise SL");
        user.setRrss(new URL("http://updatedRPerez.com"));
        String body = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User updated = userRepository.findById(user.getId()).get();
        assertEquals("Updated Enterprise SL", updated.getEnterpriseName());
        assertEquals("http://updatedRPerez.com", updated.getRrss().toString());
    }

    // no funciona porque no tienes delete
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}
