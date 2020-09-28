package com.onwelo.dice.domain;

import java.util.concurrent.ThreadLocalRandom;

public interface Roll {

    int getSide();

    default int roll() {
        return ThreadLocalRandom.current().nextInt(1, getSide() + 1);
    }
}
