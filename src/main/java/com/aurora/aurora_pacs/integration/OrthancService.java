package com.aurora.aurora_pacs.integration;

import com.aurora.aurora_pacs.dto.StudySummaryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrthancService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${orthanc.base-url}")
    private String orthancBaseUrl;

    @Value("${orthanc.username}")
    private String orthancUsername;

    @Value("${orthanc.password}")
    private String orthancPassword;
    
    @Value("${viewer.stone-base-url}")
    private String stoneBaseUrl;
    

    public List<Map<String, Object>> getStudiesRaw() {
        String url = orthancBaseUrl + "/dicom-web/studies";
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    public List<StudySummaryDto> getStudySummaries() {
        List<Map<String, Object>> rawStudies = getStudiesRaw();
        List<StudySummaryDto> result = new ArrayList<>();

        for (Map<String, Object> study : rawStudies) {
            String studyInstanceUid = extractTagValue(study, "0020000D");
            String patientName = extractPersonName(study, "00100010");
            String patientId = extractTagValue(study, "00100020");
            String studyDate = extractTagValue(study, "00080020");
            String modality = extractTagValue(study, "00080061");
            String description = extractTagValue(study, "00081030");

            result.add(new StudySummaryDto(
                    studyInstanceUid,
                    patientName,
                    patientId,
                    studyDate,
                    modality,
                    description,
                    true
            ));
        }

        return result;
    }

    public StudySummaryDto findStudySummaryByUid(String studyInstanceUid) {
        return getStudySummaries().stream()
                .filter(s -> studyInstanceUid.equals(s.getStudyInstanceUid()))
                .findFirst()
                .orElse(null);
    }

    public String buildStoneViewerUrl(String studyInstanceUid) {
    return stoneBaseUrl + "?study=" + studyInstanceUid;
    }      

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(orthancUsername, orthancPassword);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    @SuppressWarnings("unchecked")
    private String extractTagValue(Map<String, Object> study, String tag) {
        Object tagObj = study.get(tag);
        if (!(tagObj instanceof Map<?, ?> tagMap)) {
            return "";
        }

        Object valueObj = tagMap.get("Value");
        if (!(valueObj instanceof List<?> values) || values.isEmpty()) {
            return "";
        }

        Object first = values.get(0);
        return first != null ? String.valueOf(first) : "";
    }

    @SuppressWarnings("unchecked")
    private String extractPersonName(Map<String, Object> study, String tag) {
        Object tagObj = study.get(tag);
        if (!(tagObj instanceof Map<?, ?> tagMap)) {
            return "";
        }

        Object valueObj = tagMap.get("Value");
        if (!(valueObj instanceof List<?> values) || values.isEmpty()) {
            return "";
        }

        Object first = values.get(0);
        if (first instanceof Map<?, ?> pnMap) {
            Object alpha = pnMap.get("Alphabetic");
            return alpha != null ? String.valueOf(alpha) : "";
        }

        return first != null ? String.valueOf(first) : "";
    }
}