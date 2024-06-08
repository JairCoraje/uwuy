package com.acme.center.platform.learning.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.UUID;
@Embeddable

public record AcmeStudentRecordId(String studentRecorId) {
    public AcmeStudentRecordId(){
        this(UUID.randomUUID().toString()); //It is used to generate a random UUID
    }

    public AcmeStudentRecordId{
        if (studentRecorId == null || studentRecorId.isBlank()){throw new IllegalArgumentException("Acme student record profileId cannot be null or blank");

        }
    }
}