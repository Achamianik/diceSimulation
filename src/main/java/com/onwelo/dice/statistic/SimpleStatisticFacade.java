package com.onwelo.dice.statistic;

import com.onwelo.dice.api.RollDiceStatistic;
import com.onwelo.dice.api.RollsDistributionStatistic;
import com.onwelo.dice.api.RollsStatistic;
import com.onwelo.dice.dice.RolesSessionRepository;
import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.GroupSizeKey;
import com.onwelo.dice.domain.RolesSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SimpleStatisticFacade implements RollStatisticFacade {


    private final RolesSessionRepository rolesSessionRepository;

    @Autowired
    public SimpleStatisticFacade(RolesSessionRepository rolesSessionRepository) {
        this.rolesSessionRepository = rolesSessionRepository;
    }

    @Override
    public RollsStatistic getStatistic() {
        List<RolesSession> allRoll = rolesSessionRepository.findAll();
        Map<GroupSizeKey, RollDiceStatistic> rollStatistic = allRoll.stream()
                .collect(Collectors.toMap(
                        rolesSession -> new GroupSizeKey(rolesSession.getGroupSize(), rolesSession.getDiceSize()),
                        rolesSession -> new RollDiceStatistic(rolesSession.getRolls()),
                        RollDiceStatistic::incrementBy
                ));
        return new RollsStatistic(rollStatistic);
    }

    @Override
    public RollsDistributionStatistic getRollDistribution(int diceNumber, int diceSide) {
        List<RolesSession> rolls = rolesSessionRepository.findByDiceSizeAndAndGroupSize(diceSide, diceNumber);
        int numberOfRolls = rolls.stream()
                .mapToInt(RolesSession::getRolls)
                .sum();

        if (numberOfRolls == 0) {
            RolesSession rolesResults = new RolesSession(new DiceGroup(diceSide, diceNumber));
            return new RollsDistributionStatistic(rolesResults);
        }

        Map<Integer, BigDecimal> rollsDistribution = margeRollDistribution(rolls).stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        calculateDistribution(numberOfRolls),
                        (v1, v2) -> {
                            throw new IllegalArgumentException(String.format("Duplicated key %s", v1));
                        },
                        LinkedHashMap::new
                ));

        return new RollsDistributionStatistic(rollsDistribution);
    }

    private Set<Map.Entry<Integer, Integer>> margeRollDistribution(List<RolesSession> rolls) {
        return rolls.stream()
                .flatMap(rolesSession -> rolesSession.getNumberDistribution().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum,
                        LinkedHashMap::new
                )).entrySet();
    }

    private Function<Map.Entry<Integer, Integer>, BigDecimal> calculateDistribution(int numberOfRolls) {
        return integerIntegerEntry -> {
            double value = ((double) integerIntegerEntry.getValue() / numberOfRolls) * 100;
            return new BigDecimal(value).setScale(2, RoundingMode.DOWN);
        };
    }
}
