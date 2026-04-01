package com.aurora.aurora_pacs;

import com.aurora.aurora_pacs.model.Role;
import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuroraPacsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuroraPacsApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdmin(UserAccountRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                UserAccount admin = new UserAccount();
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                repo.save(admin);
                System.out.println("Seeded admin user: admin / admin123");
            }
        };
    }
}
