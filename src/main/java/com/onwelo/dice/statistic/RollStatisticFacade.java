package com.onwelo.dice.statistic;

import com.onwelo.dice.api.RollsDistributionStatistic;
import com.onwelo.dice.api.RollsStatistic;

public interface RollStatisticFacade {

    RollsStatistic getStatistic();

    RollsDistributionStatistic getRollDistribution(int diceNumber, int diceSize);
}
