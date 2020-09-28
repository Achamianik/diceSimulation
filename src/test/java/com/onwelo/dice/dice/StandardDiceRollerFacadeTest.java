package com.onwelo.dice.dice;


import com.onwelo.dice.domain.Dice;
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
    public void createDice_ok(int side, int diceNumber) {
        DiceGroup diceGroup = standardDiceRollerFacade.createDiceGroup(side, diceNumber);

        assertThat(diceGroup).isNotNull();
        assertThat(diceGroup.getDices())
                .hasSize(diceNumber)
                .extracting(Dice::getSide)
                .contains(side);
    }

    @Test
    public void roleDice_ok() {
        DiceGroup diceGroup = new DiceGroup(6, 1);
        RolesSession rolesSession = standardDiceRollerFacade.roleDices(diceGroup, 1);

        assertThat(rolesSession).isNotNull();
        assertThat(rolesSession.getNumberDistribution())
                .hasSize(6);

    }
}