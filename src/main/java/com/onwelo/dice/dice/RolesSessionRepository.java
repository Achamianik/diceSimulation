package com.onwelo.dice.dice;

import com.onwelo.dice.domain.RolesSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesSessionRepository extends MongoRepository<RolesSession, String> {

    List<RolesSession> findByDiceSizeAndAndGroupSize(int diceSize, int groupSize);
}
