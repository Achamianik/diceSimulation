package com.onwelo.dice;

import com.onwelo.dice.api.RollsDistributionStatistic;
import com.onwelo.dice.api.RollsStatistic;
import com.onwelo.dice.statistic.RollStatisticFacade;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/dice/statistic")
@Validated
public class DiceStatisticController {

    private final RollStatisticFacade rollStatisticFacade;

    public DiceStatisticController(RollStatisticFacade rollStatisticFacade) {
        this.rollStatisticFacade = rollStatisticFacade;
    }

    @GetMapping("/get")
    @ApiOperation(value = "Calculate roll statistic")
    public ResponseEntity<RollsStatistic> statistic() {
        RollsStatistic rollsStatistic = rollStatisticFacade.getStatistic();

        return ResponseEntity.ok(rollsStatistic);
    }

    @GetMapping("/rollDistribution/{diceNumber}/{diceSize}")
    @ApiOperation(value = "Calculate number distribution for dice group")
    public ResponseEntity<RollsDistributionStatistic> rollDistribution(@PathVariable @Min(1) int diceNumber,
                                                                       @PathVariable @Min(4) int diceSize) {
        RollsDistributionStatistic rollsStatistic = rollStatisticFacade.getRollDistribution(diceNumber, diceSize);
        return ResponseEntity.ok(rollsStatistic);
    }

}
