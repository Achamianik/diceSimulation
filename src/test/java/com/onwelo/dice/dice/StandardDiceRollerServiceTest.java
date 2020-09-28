package com.onwelo.dice.dice;


import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.RolesSession;
import com.onwelo.dice.domain.RollResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(StandardDiceRollerService.class)
class StandardDiceRollerServiceTest {

    @Autowired
    private DiceRollerService standardDiceRollerService;

    @Autowired
    private RolesSessionRepository rolesSessionRepository;

    @Test
    public void saveRollSession_save_read() {
        RolesSession session = new RolesSession(new DiceGroup(6, 1));
        session.increment(new RollResult(1));
        session.increment(new RollResult(3));

        standardDiceRollerService.saveSession(session);

        assertThat(rolesSessionRepository.findAll())
                .hasSize(1)
                .contains(session);

    }

    @Test
    public void saveRollSession_save_findBy_diceNumber_diceSize() {
        DiceGroup diceGroup = new DiceGroup(6, 1);

        RolesSession session = new RolesSession(diceGroup);
        RolesSession nextSession = new RolesSession(diceGroup);

        session.increment(new RollResult(1));
        session.increment(new RollResult(3));

        nextSession.increment(new RollResult(6));
        nextSession.increment(new RollResult(2));

        standardDiceRollerService.saveSession(session);
        standardDiceRollerService.saveSession(nextSession);


        assertThat(rolesSessionRepository.findByDiceSizeAndAndGroupSize(6,1))
                .hasSize(2)
                .contains(session,nextSession);

    }
}