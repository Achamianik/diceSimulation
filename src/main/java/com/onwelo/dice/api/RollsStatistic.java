package com.onwelo.dice.api;

import com.onwelo.dice.domain.GroupSizeKey;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@ApiModel(description ="Container for all RollBasicStatistic")
public class RollsStatistic {

    private final Map<GroupSizeKey, RollBasicStatistic> statistic;

}
