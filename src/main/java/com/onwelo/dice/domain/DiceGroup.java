package com.onwelo.dice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@ToString
public class DiceGroup {

    private final List<Dice> dices;

    public DiceGroup(int side, int numberOfDice) {
        this.dices = new ArrayList<>();

        for (int i = 0; i < numberOfDice; i++) {
            this.dices.add(new Dice(side));
        }
    }

    public RollResult groupRoll() {
        return new RollResult(dices.stream()
                .map(Dice::roll)
                .reduce(0, Integer::sum));
    }

    public int getGroupMinimalValue() {
        return dices.size();
    }

    public int getGroupMaximalValue() {
        return dices.size() * getDiceSide();
    }

    public int getDiceSide() {
        return dices.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dice group not exist"))
                .getSide();
    }

}
