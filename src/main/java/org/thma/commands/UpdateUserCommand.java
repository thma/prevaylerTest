package org.thma.commands;

import org.prevayler.Transaction;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

import java.util.Date;

/**
 * Created by thma on 26.05.2015.
 */
public class UpdateUserCommand implements Transaction<UserRepository> {

    private long userId;

    private String updateUserName;

    public UpdateUserCommand(long id, String name) {
        userId = id;
        updateUserName = name;
    }

    @Override
    public void executeOn(UserRepository prevalentSystem, Date executionTime) {
        User user = prevalentSystem.getUser(userId);
        user.setName(updateUserName);
    }
}
