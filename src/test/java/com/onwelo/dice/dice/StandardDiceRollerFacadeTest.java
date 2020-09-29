package com.onwelo.dice.dice;


import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.RolesSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StandardDiceRollerFacadeTest {

    @InjectMocks
    private StandardDiceRollerFacade standardDiceRollerFacade;

    @Mock
    private DiceRollerService diceRollerService;

    @ParameterizedTest
    @CsvSource({
            "6, 1",
            "4, 5",
    })
    public void createDice_ok(int diceSide, int diceNumber) {
        DiceGroup diceGroup = standardDiceRollerFacade.createDiceGroup(diceSide, diceNumber);

        assertThat(diceGroup).isNotNull();
        assertThat(diceGroup.getDices())
                .hasSize(diceNumber);
        assertThat(diceGroup.getDiceSide())
                .isEqualTo(diceSide);
    }

    @Test
    public void roleDice_ok() {
        DiceGroup diceGroup = new DiceGroup(6, 1);
        RolesSession rolesSession = standardDiceRollerFacade.roleDices(diceGroup, 1);
        verify(diceRollerService, times(1)).saveSession(eq(rolesSession));

        assertThat(rolesSession).isNotNull();
        assertThat(rolesSession.getNumberDistribution())
                .hasSize(6);

    }
}