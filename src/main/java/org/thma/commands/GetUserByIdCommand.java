package org.thma.commands;

import org.prevayler.Query;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

import java.util.Date;

/**
 * Created by thma on 26.05.2015.
 */
public class GetUserByIdCommand implements Query<UserRepository, User> {

    private final long queryUserId;

    public GetUserByIdCommand(long someId) {
        queryUserId = someId;
    }

    @Override
    public User query(UserRepository prevalentSystem, Date executionTime) throws Exception {
        return prevalentSystem.getUser(queryUserId);
    }
}
