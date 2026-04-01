package com.aurora.aurora_pacs.dto;

public class StudySummaryDto {

    private String studyInstanceUid;
    private String patientName;
    private String patientId;
    private String studyDate;
    private String modality;
    private String studyDescription;
    private boolean viewerReady;

    public StudySummaryDto() {
    }

    public StudySummaryDto(String studyInstanceUid,
                           String patientName,
                           String patientId,
                           String studyDate,
                           String modality,
                           String studyDescription,
                           boolean viewerReady) {
        this.studyInstanceUid = studyInstanceUid;
        this.patientName = patientName;
        this.patientId = patientId;
        this.studyDate = studyDate;
        this.modality = modality;
        this.studyDescription = studyDescription;
        this.viewerReady = viewerReady;
    }

    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public boolean isViewerReady() {
        return viewerReady;
    }

    public void setViewerReady(boolean viewerReady) {
        this.viewerReady = viewerReady;
    }
}