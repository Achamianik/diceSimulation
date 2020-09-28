package com.onwelo.dice.statistic;

import com.onwelo.dice.api.RollDiceStatistic;
import com.onwelo.dice.api.RollsDistributionStatistic;
import com.onwelo.dice.api.RollsStatistic;
import com.onwelo.dice.dice.RolesSessionRepository;
import com.onwelo.dice.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SimpleStatisticFacadeTest {

    @InjectMocks
    private SimpleStatisticFacade simpleStatisticFacade;

    @Mock
    private RolesSessionRepository rolesSessionRepository;

    @Test
    public void getStatistic_manySimulations() {
        given(rolesSessionRepository.findAll())
                .willReturn(createRolesSessionStatisticList());

        RollsStatistic statistic = simpleStatisticFacade.getStatistic();

        then(rolesSessionRepository)
                .should(times(1))
                .findAll();

        assertThat(statistic).isNotNull();
        assertThat(statistic.getStatistic()).
                containsOnly(
                        entry(new GroupSizeKey(3, 6), new RollDiceStatistic(2, 5)),
                        entry(new GroupSizeKey(1, 4), new RollDiceStatistic(1, 1)),
                        entry(new GroupSizeKey(2, 5), new RollDiceStatistic(1, 4)),
                        entry(new GroupSizeKey(1, 6), new RollDiceStatistic(1, 1))
                );
    }

    @Test
    public void getStatistic_emptyDB() {
        given(rolesSessionRepository.findAll())
                .willReturn(Collections.emptyList());

        RollsStatistic statistic = simpleStatisticFacade.getStatistic();

        then(rolesSessionRepository)
                .should(times(1))
                .findAll();

        assertThat(statistic).isNotNull();
        assertThat(statistic.getStatistic()).isEmpty();
    }

    @Test
    public void RollDistribution_emptyDB() {
        given(rolesSessionRepository.findByDiceSizeAndAndGroupSize(eq(6), eq(3)))
                .willReturn(Collections.emptyList());

        RollsDistributionStatistic statistic = simpleStatisticFacade.getRollDistribution(3, 6);

        assertThat(statistic).isNotNull();
        assertThat(statistic)
                .isEqualTo(new RollsDistributionStatistic(new RolesSession(new DiceGroup(6, 3))));
    }

    @Test
    public void RollDistribution_noEmptyDB() {
        given(rolesSessionRepository.findByDiceSizeAndAndGroupSize(eq(4), eq(1)))
                .willReturn(createRolesSessionDistribution());

        RollsDistributionStatistic statistic = simpleStatisticFacade.getRollDistribution(1, 4);

        assertThat(statistic).isNotNull();
        assertThat(statistic.getStatistic())
                .containsExactly(
                        entry(1, new BigDecimal("50.00")),
                        entry(2, new BigDecimal("16.66")),
                        entry(3, new BigDecimal("25.00")),
                        entry(4, new BigDecimal("8.33"))
                );
    }

    private List<RolesSession> createRolesSessionDistribution() {
        RolesSession first = new RolesSession(new DiceGroup(4, 1));
        RolesSession second = new RolesSession(new DiceGroup(4, 1));
        RolesSession third = new RolesSession(new DiceGroup(4, 1));

        first.increment(new RollResult(3));
        first.increment(new RollResult(1));
        first.increment(new RollResult(3));

        second.increment(new RollResult(2));
        second.increment(new RollResult(1));
        second.increment(new RollResult(2));
        second.increment(new RollResult(3));

        third.increment(new RollResult(4));
        third.increment(new RollResult(1));
        third.increment(new RollResult(1));
        third.increment(new RollResult(1));
        third.increment(new RollResult(1));

        return Arrays.asList(first, second, third);
    }

    private List<RolesSession> createRolesSessionStatisticList() {
        RolesSession first = new RolesSession(new DiceGroup(6, 3));
        RolesSession second = new RolesSession(new DiceGroup(6, 3));
        RolesSession third = new RolesSession(new DiceGroup(6, 1));
        RolesSession forth = new RolesSession(new DiceGroup(4, 1));
        RolesSession fifth = new RolesSession(new DiceGroup(5, 2));

        first.increment(new RollResult(3));
        first.increment(new RollResult(16));
        first.increment(new RollResult(8));
        second.increment(new RollResult(5));
        second.increment(new RollResult(6));

        third.increment(new RollResult(2));

        forth.increment(new RollResult(1));

        fifth.increment(new RollResult(10));
        fifth.increment(new RollResult(10));
        fifth.increment(new RollResult(10));
        fifth.increment(new RollResult(10));

        return Arrays.asList(first, second, third, forth, fifth);
    }
}