package com.aurora.aurora_pacs.web;

import com.aurora.aurora_pacs.model.Role;
import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.UserAccountRepository;
import com.aurora.aurora_pacs.service.StudyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/studies")
public class StudyController {

    private final UserAccountRepository userRepo;
    private final StudyService studyService;

    public StudyController(UserAccountRepository userRepo, StudyService studyService) {
        this.userRepo = userRepo;
        this.studyService = studyService;
    }

    @GetMapping
    public Object listStudies(Principal principal) {
        UserAccount user = currentUser(principal.getName());

        if (user.getRole() == Role.DOCTOR || user.getRole() == Role.ADMIN) {
            return studyService.listAll();
        }

        if (user.getRole() == Role.PATIENT) {
            return studyService.listForPatient(user);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported role");
    }

    @GetMapping("/{studyInstanceUid}")
    public Object getStudy(@PathVariable String studyInstanceUid, Principal principal) {
        UserAccount user = currentUser(principal.getName());
        assertStudyAllowed(user, studyInstanceUid);

        var dto = studyService.findByStudyInstanceUid(studyInstanceUid);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found");
        }
        return dto;
    }

   @GetMapping("/{studyInstanceUid}/viewer-url")
public Map<String, String> viewerUrl(@PathVariable String studyInstanceUid, Principal principal) {
    UserAccount user = currentUser(principal.getName());
    assertStudyAllowed(user, studyInstanceUid);

    var dto = studyService.findByStudyInstanceUid(studyInstanceUid);
    if (dto == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found");
    }

    if (!dto.isViewerReady()) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Study is not ready for viewer");
    }

    return Map.of("viewerUrl", studyService.buildViewerUrl(studyInstanceUid));
}

    private UserAccount currentUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private void assertStudyAllowed(UserAccount user, String studyInstanceUid) {
        if (user.getRole() == Role.DOCTOR || user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.PATIENT && studyService.userCanAccess(user, studyInstanceUid)) {
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Study not allowed for user");
    }
}