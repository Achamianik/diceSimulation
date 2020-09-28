package com.onwelo.dice.dice;

import com.onwelo.dice.domain.RolesSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class StandardDiceRollerService implements DiceRollerService {

    private final RolesSessionRepository rolesSessionRepository;

    @Autowired
    public StandardDiceRollerService(RolesSessionRepository rolesSessionRepository) {
        this.rolesSessionRepository = rolesSessionRepository;
    }

    @Override
    public void saveSession(RolesSession rolesSession) {
        rolesSessionRepository.save(rolesSession);
    }
}
