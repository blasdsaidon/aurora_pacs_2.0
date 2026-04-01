package com.aurora.aurora_pacs.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "studies", uniqueConstraints = {
        @UniqueConstraint(name = "uk_studies_study_instance_uid", columnNames = "study_instance_uid")
})
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_instance_uid", nullable = false, unique = true, length = 255)
    private String studyInstanceUid;

    @Column(name = "patient_id", length = 128)
    private String patientId;

    @Column(name = "patient_name", length = 255)
    private String patientName;

    @Column(name = "study_date", length = 32)
    private String studyDate;

    @Column(name = "modality", length = 64)
    private String modality;

    @Column(name = "study_description", length = 255)
    private String studyDescription;

    @Column(name = "clinic_code", length = 128)
    private String clinicCode;

    @Column(name = "source_type", length = 64)
    private String sourceType;

    @Column(name = "storage_type", length = 64)
    private String storageType;

    @Column(name = "storage_key", length = 512)
    private String storageKey;

    @Column(name = "viewer_ready", nullable = false)
    private boolean viewerReady = false;

    @Column(name = "sync_status", length = 64)
    private String syncStatus;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public boolean isViewerReady() {
        return viewerReady;
    }

    public void setViewerReady(boolean viewerReady) {
        this.viewerReady = viewerReady;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
