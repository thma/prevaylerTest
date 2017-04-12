package org.thma.commands;

import org.prevayler.Transaction;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

import java.util.Date;

/**
 * Created by thma on 21.05.2015.
 */
public class AddUserCommand implements Transaction<UserRepository> {

    private User addedUser;

    public AddUserCommand(User userToAdd) {
        addedUser = userToAdd;
    }

    public void executeOn(UserRepository userRepository, Date date) {
        if (addedUser.getId() == null) {
            addedUser.setId(userRepository.size());
        }
        userRepository.addUser(addedUser);
    }
}
