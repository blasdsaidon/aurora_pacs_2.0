package com.aurora.aurora_pacs.web;

import com.aurora.aurora_pacs.model.Role;
import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.UserAccountRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserAccountRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<UserResponse> all() {
        return userRepo.findAll().stream().map(this::toResponse).toList();
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        UserAccount user = new UserAccount();
        user.setUsername(request.username().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setPatientId(blankToNull(request.patientId()));
        return toResponse(userRepo.save(user));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        UserAccount user = userRepo.findById(id).orElseThrow();
        if (request.username() != null && !request.username().isBlank()) {
            user.setUsername(request.username().trim());
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }
        user.setPatientId(blankToNull(request.patientId()));
        return toResponse(userRepo.save(user));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepo.deleteById(id);
    }

    private UserResponse toResponse(UserAccount user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getPatientId());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public record CreateUserRequest(
            @NotBlank String username,
            @NotBlank String password,
            @NotNull Role role,
            String patientId
    ) {}

    public record UpdateUserRequest(
            String username,
            String password,
            Role role,
            String patientId
    ) {}

    public record UserResponse(Long id, String username, Role role, String patientId) {}
}
