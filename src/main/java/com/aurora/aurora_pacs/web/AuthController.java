package com.aurora.aurora_pacs.web;

import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountRepository userRepo;

    public AuthController(UserAccountRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        UserAccount user = userRepo.findByUsername(authentication.getName()).orElseThrow();
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole().name(),
                "patientId", user.getPatientId() == null ? "" : user.getPatientId()
        );
    }
}
