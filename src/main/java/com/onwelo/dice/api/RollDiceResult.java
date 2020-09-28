package com.onwelo.dice.api;

import com.onwelo.dice.domain.RolesSession;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
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
