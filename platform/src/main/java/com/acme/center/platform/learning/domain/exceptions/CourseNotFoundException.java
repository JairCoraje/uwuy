package com.acme.center.platform.learning.domain.exceptions;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(Long along) {
        super("Course with id " + along + " not found");
    }
}