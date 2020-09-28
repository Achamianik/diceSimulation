package com.onwelo.dice;

import com.onwelo.dice.api.RollDiceResult;
import com.onwelo.dice.dice.DiceRollerFacade;
import com.onwelo.dice.domain.DiceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/dice")
public class DiceController {

    private final DiceRollerFacade diceRollerFacade;

    @Autowired
    public DiceController(DiceRollerFacade diceRollerFacade) {
        this.diceRollerFacade = diceRollerFacade;
    }

    @GetMapping("/roll/{side}/{diceNumber}/{rolls}")
    public ResponseEntity<RollDiceResult> rollDices(@PathVariable @Min(4) int side,
                                                    @PathVariable @Min(1) int diceNumber,
                                                    @PathVariable @Min(1) int rolls) {

        DiceGroup diceGroup = diceRollerFacade.createDiceGroup(side, diceNumber);

        return ResponseEntity.ok(new RollDiceResult(diceRollerFacade.roleDices(diceGroup, rolls)));
    }

}
