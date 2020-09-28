package com.onwelo.dice.api;

import com.onwelo.dice.domain.GroupSizeKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RollsStatistic {

    private final Map<GroupSizeKey, RollDiceStatistic> statistic;

}
