package io.github.unaimillan.innocfparser.types.helpers;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.github.unaimillan.innocfparser.types.StudentSolution;

import java.util.Objects;

public class StudentToCSVConverter extends StdConverter<StudentSolution, CSVStudent> {

    @Override
    public CSVStudent convert(StudentSolution v) {
        return new CSVStudent(v.getSubmission().getHandle(), v.getSubmissionCount(), v.getSubmission().getSubmissionId(), v.getSubmission().getCreationTime(), Objects.requireNonNullElse(v.getSubmission().getPoints(), 0.0), v.getSubmission().getPassedTestCount());
    }
}
