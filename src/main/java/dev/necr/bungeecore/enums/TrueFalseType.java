package dev.necr.bungeecore.enums;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public enum TrueFalseType {

    ON_OFF("On", "Off"),
    DEFAULT("True", "False"),
    ENABLED("Enabled", "Disabled");

    @Getter
    final String ifTrue, ifFalse;

    TrueFalseType(String ifTrue, String ifFalse) {
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }
}
