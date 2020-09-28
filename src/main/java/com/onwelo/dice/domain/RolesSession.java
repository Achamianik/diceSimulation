package com.onwelo.dice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashMap;
import java.util.Map;

@ToString
@Getter
@EqualsAndHashCode
public class RolesSession {

    @Id
    private ObjectId id;

    private int diceSize;
    private int groupSize;
    private int rolls = 0;

    private Map<Integer, Integer> numberDistribution;

    public RolesSession() {
    }

    public RolesSession(DiceGroup diceGroup) {
        this.numberDistribution = new LinkedHashMap<>();
        this.groupSize = diceGroup.getGroupMinimalValue();
        this.diceSize = diceGroup.getDiceSide();
        int maxValue = diceGroup.getGroupMaximalValue();

        for (int i = this.groupSize; i <= maxValue; i++) {
            numberDistribution.put(i, 0);
        }
    }

    public void increment(RollResult rollResult) {
        this.numberDistribution.put(rollResult.getRoleNumber(),
                this.numberDistribution.get(rollResult.getRoleNumber()) + 1);
        rolls++;
    }
}

