package com.onwelo.dice.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
public class RollDiceStatistic {

    private final int rollSimulation;
    private final int totalRolls;

    public RollDiceStatistic(int totalRolls) {
        this.rollSimulation = 1;
        this.totalRolls = totalRolls;
    }

    public RollDiceStatistic incrementBy(RollDiceStatistic anotherRollStatistic) {
        return new RollDiceStatistic(rollSimulation + 1, this.totalRolls + anotherRollStatistic.getTotalRolls() );
    }
}
