package com.aurora.aurora_pacs.web;

import com.aurora.aurora_pacs.model.StudyAccess;
import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.StudyAccessRepository;
import com.aurora.aurora_pacs.repo.UserAccountRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/study-access")
public class StudyAccessAdminController {

    private final StudyAccessRepository accessRepo;
    private final UserAccountRepository userRepo;

    public StudyAccessAdminController(StudyAccessRepository accessRepo, UserAccountRepository userRepo) {
        this.accessRepo = accessRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/user/{userId}")
    public List<StudyAccessResponse> listByUser(@PathVariable Long userId) {
        return accessRepo.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @PostMapping
    public StudyAccessResponse grant(@RequestBody GrantStudyAccessRequest request) {
        UserAccount user = userRepo.findById(request.userId()).orElseThrow();
        StudyAccess access = new StudyAccess();
        access.setUser(user);
        access.setStudyInstanceUid(request.studyInstanceUid().trim());
        return toResponse(accessRepo.save(access));
    }

    @DeleteMapping("/{id}")
    public void revoke(@PathVariable Long id) {
        accessRepo.deleteById(id);
    }

    private StudyAccessResponse toResponse(StudyAccess access) {
        return new StudyAccessResponse(
                access.getId(),
                access.getUser().getId(),
                access.getStudyInstanceUid(),
                access.getGrantedAt()
        );
    }

    public record GrantStudyAccessRequest(@NotNull Long userId, @NotBlank String studyInstanceUid) {}

    public record StudyAccessResponse(Long id, Long userId, String studyInstanceUid, java.time.OffsetDateTime grantedAt) {}
}
