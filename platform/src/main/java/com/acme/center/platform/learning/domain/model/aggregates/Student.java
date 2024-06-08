package com.acme.center.platform.learning.domain.model.aggregates;

import com.acme.center.platform.learning.domain.model.valueobjects.AcmeStudentRecordId;
import com.acme.center.platform.learning.domain.model.valueobjects.ProfileId;
import com.acme.center.platform.learning.domain.model.valueobjects.StudentPerformanceMetricSet;
import com.acme.center.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
@Entity
public class Student extends AuditableAbstractAggregateRoot<Student> {
    @Getter
    @Embedded
    @Column(name = "acme_student_id")
    private final AcmeStudentRecordId acmeStudentRecordId;
    @Embedded
    private ProfileId profileId;
    @Embedded
    private StudentPerformanceMetricSet performanceMetricSet;
    public Student(){
        this.acmeStudentRecordId = new AcmeStudentRecordId();
        this.performanceMetricSet = new StudentPerformanceMetricSet();
    }
    public Student(Long profileId){
        this();
        this.profileId = new ProfileId(profileId);
    }

    public Student(ProfileId profileId){
        this();
        this.profileId = profileId;
    }

    /**
     * Updates the student metrics when a course is completed.
     */
    public void updateMetricsOnCourseCompleted(){
        this.performanceMetricSet = this.performanceMetricSet.incrementTotalCompletedCourses();
    }

    /**
     * Updates the student metrics when a tutorial is completed.
     */
    public void updateMetricsOnTutorialCompleted(){
        this.performanceMetricSet = this.performanceMetricSet.incrementTotalTutorials();
    }

    public String getStudentRecordId(){
        return  this.acmeStudentRecordId.studentRecorId();
    }

    public Long getProfileId(){
        return this.profileId.profileId();
    }
}