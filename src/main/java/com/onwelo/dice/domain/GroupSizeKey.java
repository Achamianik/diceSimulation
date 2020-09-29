package com.onwelo.dice.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class GroupSizeKey {

    private final int diceNumber;
    private final int diceSize;

    @JsonValue
    public String toJsoKey() {
        return diceNumber + "_" + diceSize;
    }
}
