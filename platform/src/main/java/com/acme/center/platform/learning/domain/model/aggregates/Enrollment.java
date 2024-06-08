package com.acme.center.platform.learning.domain.model.aggregates;

import com.acme.center.platform.learning.domain.model.events.TutorialCompletedEvent;
import com.acme.center.platform.learning.domain.model.valueobjects.AcmeStudentRecordId;
import com.acme.center.platform.learning.domain.model.valueobjects.EnrollmentStatus;
import com.acme.center.platform.learning.domain.model.valueobjects.ProgressRecord;
import com.acme.center.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class Enrollment extends AuditableAbstractAggregateRoot<Enrollment> {
    @Getter
    @Embedded
    private AcmeStudentRecordId acmeStudentRecordId;
    @Getter
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @Embedded
    private ProgressRecord progressRecord;
    private EnrollmentStatus status;
    public Enrollment(){
    }
    public Enrollment(AcmeStudentRecordId studentRecordId, Course course){
        this.acmeStudentRecordId = studentRecordId;
        this.course = course;
        this.status = EnrollmentStatus.REQUESTED;
        this.progressRecord = new ProgressRecord();
    }
    public void confirm(){
        this.status = EnrollmentStatus.CONFIRMED;
        this.progressRecord.initializeProgressRecord(this, course.getLearningPath());
    }
    public void reject(){
        this.status = EnrollmentStatus.REJECTED;
    }
    public void cancel(){
        this.status = EnrollmentStatus.CANCELLED;
    }
    public String getStatus(){ return this.status.name().toLowerCase(); }
    public long calculateDaysElapsed(){
        return progressRecord.calculateDaysElapsedForEnrollment(this);
    }
    public boolean isConfirmed(){
        return  this.status == EnrollmentStatus.CONFIRMED;
    }
    public boolean isRejected(){
        return this.status == EnrollmentStatus.REJECTED;
    }
    public void completeTutorial(Long tutorialId){
        progressRecord.completeTutorial(tutorialId, course.getLearningPath());
        //Publish a Tutorial Completed Event
        this.registerEvent(new TutorialCompletedEvent(this, this.getId(), tutorialId));
    }
}