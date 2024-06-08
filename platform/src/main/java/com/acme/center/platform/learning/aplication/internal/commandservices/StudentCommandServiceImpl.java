package com.acme.center.platform.learning.aplication.internal.commandservices;

import com.acme.center.platform.learning.domain.model.aggregates.Student;
import com.acme.center.platform.learning.domain.model.commands.CreateStudentCommand;
import com.acme.center.platform.learning.domain.model.commands.UpdateStudentMetricsOnTutorialCompletedCommand;
import com.acme.center.platform.learning.domain.model.valueobjects.AcmeStudentRecordId;
import com.acme.center.platform.learning.domain.services.StudentCommandService;
import com.acme.center.platform.learning.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.acme.center.platform.learning.outboundservices.acl.ExternalProfileService;
import org.springframework.stereotype.Service;
@Service

public class StudentCommandServiceImpl implements StudentCommandService {
    private final StudentRepository studentRepository;
    private final ExternalProfileService externalProfileService;
    public StudentCommandServiceImpl(StudentRepository studentRepository, ExternalProfileService externalProfileService) {
        this.studentRepository = studentRepository;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public AcmeStudentRecordId handle(CreateStudentCommand command){
        var profileId = externalProfileService.fetchProfileIdByEmail(command.email());
        // If profileId is empty, create profile
        if(profileId.isEmpty()) {
            profileId = externalProfileService.createProfile(command.firstName(), command.lastName(),
                    command.email(), command.street(), command.number(), command.city(),
                    command.postalCode(), command.country());
        } else {
            //if profileId is not empty, check if student exists
            studentRepository.findByProfileId(profileId.get()).ifPresent(student -> {
                throw new IllegalArgumentException("Student already exists");
            });
        }
        // If profileId is still empty, throw exception
        if(profileId.isEmpty()) throw new IllegalArgumentException("Unable to create profile");
        //Create student using fetched or created profileId
        var student = new Student(profileId.get()); // create student using fetched or created profileId
        studentRepository.save(student);
        return student.getAcmeStudentRecordId();
    }

    @Override
    public AcmeStudentRecordId handle(UpdateStudentMetricsOnTutorialCompletedCommand command){
        studentRepository.findByAcmeStudentRecordId(command.StudentRecordId()).map(student -> {
            //Update student metrics on tutorial completed
            student.updateMetricsOnTutorialCompleted();
            studentRepository.save(student);
            return student.getAcmeStudentRecordId();
        }).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return null;
    }
}
