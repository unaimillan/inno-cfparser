package io.github.unaimillan.innocfparser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ParticipantType {
    CONTESTANT("CONTESTANT"),
    PRACTICE("PRACTICE"),
    VIRTUAL("VIRTUAL"),
    MANAGER("MANAGER"),
    OUT_OF_COMPETITION("OUT_OF_COMPETITION");

    @Getter
    private final String value;
}
