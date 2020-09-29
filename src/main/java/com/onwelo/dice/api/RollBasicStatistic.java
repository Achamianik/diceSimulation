package com.onwelo.dice.api;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@ApiModel(description ="Basic information about simulation and roll count")
public class RollBasicStatistic {

    private final int rollSimulation;
    private final int totalRolls;

    public RollBasicStatistic(int totalRolls) {
        this.rollSimulation = 1;
        this.totalRolls = totalRolls;
    }

    public RollBasicStatistic incrementBy(RollBasicStatistic anotherRollStatistic) {
        return new RollBasicStatistic(rollSimulation + 1, this.totalRolls + anotherRollStatistic.getTotalRolls() );
    }
}
