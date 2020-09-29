package com.onwelo.dice.api;

import com.onwelo.dice.domain.RolesSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ApiModel(description ="Dice roll result for one session")
public class RollDiceResult {

    private final Map<Integer, Integer> result;

    public RollDiceResult(RolesSession rolesSession) {
        this.result = rolesSession.getNumberDistribution().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (val1, val2) -> {throw new IllegalArgumentException(String.format("Duplicate key %s", val1));},
                        LinkedHashMap::new
                        ));
    }
}
