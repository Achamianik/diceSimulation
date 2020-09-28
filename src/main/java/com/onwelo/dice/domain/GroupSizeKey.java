package com.onwelo.dice.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class GroupSizeKey {

    private final int groupSize;
    private final int diceSize;

    @Override
    public String toString() {
        return "GroupSizeKey{" +
                "groupSize=" + groupSize +
                ", diceSize=" + diceSize +
                '}';
    }

    @JsonValue
    public String toJsoKey() {
        return groupSize + "_" + diceSize;
    }
}
