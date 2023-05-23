package io.github.unaimillan.innocfparser.types;

import io.github.unaimillan.innocfparser.enums.ParticipantType;
import io.github.unaimillan.innocfparser.enums.Verdict;
import lombok.Data;
import ru.covariance.codeforcesapi.entities.Submission;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
public class StudentSubmission {

    protected int submissionId;

    protected String handle;

    protected ZonedDateTime creationTime;

    protected ParticipantType participationType;

    protected Verdict verdict;

    protected int passedTestCount;

    protected Double points;

    public StudentSubmission(Submission submission) {
        this.submissionId = submission.getId();
        this.handle = submission.getAuthor().getMembers().get(0).getHandle();
        this.creationTime = ZonedDateTime.ofLocal(
                LocalDateTime.ofEpochSecond(
                        submission.getCreationTimeSeconds(), 0, ZoneOffset.of("+3")),
                ZoneId.of("Europe/Moscow"),
                ZoneOffset.UTC);
        this.participationType = ParticipantType.valueOf(submission.getAuthor().getParticipantType());
        this.verdict = Verdict.valueOf(submission.getVerdict());
        this.passedTestCount = submission.getPassedTestCount();
        this.points = submission.getPoints();
    }

    public static StudentSubmission of(Submission submission) {
        return new StudentSubmission(submission);
    }
}
