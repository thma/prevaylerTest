package org.thma.commands;

import org.prevayler.Query;
import org.thma.domain.UserRepository;

import java.util.Date;

/**
 * Created by thma on 26.05.2015.
 */
public class GetNextUserPkCommand implements Query<UserRepository, Long> {


    public GetNextUserPkCommand() {
    }

    @Override
    public Long query(UserRepository prevalentSystem, Date executionTime) throws Exception {
        return prevalentSystem.size();
    }
}
