package com.onwelo.dice;

import com.onwelo.dice.api.RollDiceResult;
import com.onwelo.dice.dice.DiceRollerFacade;
import com.onwelo.dice.domain.DiceGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "Dice roll controller")
public class DiceController {

    private final DiceRollerFacade diceRollerFacade;

    @Autowired
    public DiceController(DiceRollerFacade diceRollerFacade) {
        this.diceRollerFacade = diceRollerFacade;
    }

    @GetMapping("/roll/{diceSide}/{diceNumber}/{rolls}")
    @ApiOperation(value = "Roll dices")
    public ResponseEntity<RollDiceResult> rollDices(@PathVariable @Min(4) int diceSide,
                                                    @PathVariable @Min(1) int diceNumber,
                                                    @PathVariable @Min(1) int rolls) {

        DiceGroup diceGroup = diceRollerFacade.createDiceGroup(diceSide, diceNumber);
        return ResponseEntity.ok(new RollDiceResult(diceRollerFacade.roleDices(diceGroup, rolls)));
    }

}
