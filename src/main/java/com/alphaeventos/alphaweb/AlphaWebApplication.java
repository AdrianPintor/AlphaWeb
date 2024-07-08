package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.*;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.alphaeventos.alphaweb.services.UserService;
import com.alphaeventos.alphaweb.services.BillingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class AlphaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlphaWebApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, ArtistService artistService) {
        return args -> {
            Role role1 = userService.saveRole(new Role(null, "ROLE_CONTRIBUTOR"));
            Role role2 = userService.saveRole(new Role(null, "ROLE_ADMIN"));

            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            userService.save(new User(null, "Enterprise SL", "Ramoncin Perez", "ramoncin.perez", "futbol2000", "ramoncin.perez@example.com", 672456464, "Calle Goya, 5", new URL("http://RPerez.com"), null, null, null, roles1));

            Set<Role> roles2 = new HashSet<>();
            roles2.add(role2);
            userService.save(new User(null, "Doe Enterprises", "Jane Doe", "jane.doe", "password123", "jane.doe@example.com", 123456789, "Main Street 1", new URL("http://JaneDoe.com"), null, null, null, roles2));

            try {
                artistService.save(new Artist(null, "Aiko Tanaka", new URL("http://example.com/aiko"), "Some personal info", new URL("http://example.com/rrss"), "Technical requirements", null, null));
                artistService.save(new Artist(null, "Jonas Schmidt", new URL("http://example.com/jonas"), "Some personal info", new URL("http://example.com/rrss"), "Technical requirements", null, null));
                artistService.save(new Artist(null, "Cas Van Dijk", new URL("http://example.com/cas"), "Some personal info", new URL("http://example.com/rrss"), "Technical requirements", null, null));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        };
    }
}