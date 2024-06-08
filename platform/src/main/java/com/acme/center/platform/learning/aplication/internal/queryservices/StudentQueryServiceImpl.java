package com.acme.center.platform.learning.aplication.internal.queryservices;

import com.acme.center.platform.learning.domain.model.aggregates.Student;
import com.acme.center.platform.learning.domain.model.queries.GetStudentByAcmeStudentRecordIdQuery;
import com.acme.center.platform.learning.domain.model.queries.GetStudentByProfileIdQuery;
import com.acme.center.platform.learning.domain.services.StudentQueryService;

import java.util.Optional;

public class StudentQueryServiceImpl implements StudentQueryService {




    @Override
    public Optional<Student> handle(GetStudentByProfileIdQuery query) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> handle(GetStudentByAcmeStudentRecordIdQuery query) {
        return Optional.empty();
    }
}
