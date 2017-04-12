package org.thma.commands;

import org.prevayler.Transaction;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

/**
 * Created by thma on 14.06.2016.
 */
public class UserCommands {

    public static Transaction<UserRepository> getAddUserCommand(User user ) {
        return new AddUserCommand(user);
    }
}
