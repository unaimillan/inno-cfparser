package io.github.unaimillan.innocfparser.types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.unaimillan.innocfparser.types.helpers.StudentToCSVConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.unaimillan.Main.sourceFilesIndex;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Data
@AllArgsConstructor
@JsonSerialize(converter = StudentToCSVConverter.class)
public class StudentSolution {

    protected StudentSubmission submission;
    protected int submissionCount;

    public void moveSourceCode(Path from, Path to) {
        try {
            if (!Files.exists(to)) {
                Files.createDirectories(to);
            }
            Path filename = Path.of(sourceFilesIndex.get(submission.submissionId));
            Files.copy(from.resolve(filename),
                    to.resolve(filename.toString().replace(String.valueOf(submission.submissionId), submission.handle))
                    , REPLACE_EXISTING);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Source file with given submission ID is missing. Most probably VIRTUAL participants weren't downloaded from Codeforces (or MANAGER)", e);
        }
    }
}
