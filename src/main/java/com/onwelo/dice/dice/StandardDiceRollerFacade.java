package com.onwelo.dice.dice;

import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.RolesSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class StandardDiceRollerFacade implements DiceRollerFacade {

    private final DiceRollerService diceRollerService;

    @Autowired
    public StandardDiceRollerFacade(DiceRollerService diceRollerService) {
        this.diceRollerService = diceRollerService;
    }

    @Override
    public DiceGroup createDiceGroup(int side, int diceNumber) {
        return new DiceGroup(side, diceNumber);
    }

    @Override
    public RolesSession roleDices(DiceGroup diceGroup, int times) {
        RolesSession rolesResults = new RolesSession(diceGroup);
        for (int i = 0; i < times; i++) {
            rolesResults.increment(diceGroup.groupRoll());
        }
        diceRollerService.saveSession(rolesResults);
        return rolesResults;
    }
}
