package io.github.unaimillan.innocfparser.types.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@JsonPropertyOrder({"Handle", "Submission Count", "Submission ID", "Submission Time", "Points", "Tests"})
public class CSVStudent {
    @JsonProperty("Handle")
    String handle;
    @JsonProperty("Submission Count")
    int submissionCount;
    @JsonProperty("Submission ID")
    int submissionId;
    @JsonProperty("Submission Time")
    ZonedDateTime submissionTime;
    @JsonProperty("Points")
    double points;
    @JsonProperty("Tests")
    int tests;
}
