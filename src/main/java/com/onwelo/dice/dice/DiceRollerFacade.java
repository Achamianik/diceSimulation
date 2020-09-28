package com.onwelo.dice.dice;

import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.RolesSession;

public interface DiceRollerFacade {

    DiceGroup createDiceGroup(int side, int diceNumber);

    RolesSession roleDices(DiceGroup diceGroup, int times);
}
