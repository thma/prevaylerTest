package org.thma;

import org.junit.Test;
import static org.junit.Assert.*;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.thma.commands.GetNextUserPkCommand;
import org.thma.commands.GetUserByIdCommand;
import org.thma.commands.UpdateUserCommand;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

import static org.thma.commands.UserCommands.*;

/**
 * Created by thma on 28.05.2015.
 */
public class SimpleTest {


  @Test
  public void createAndReadObjects() throws Exception {

      Prevayler<UserRepository> repositoryPrevayler;
      repositoryPrevayler = PrevaylerFactory.createPrevayler(new UserRepository());

      long primaryKey = repositoryPrevayler.execute(new GetNextUserPkCommand());
      final User user = new User(primaryKey, "new User");
      repositoryPrevayler.execute(getAddUserCommand(user));

      GetUserByIdCommand userByIdCommand = new GetUserByIdCommand(primaryKey);

      User user1 = repositoryPrevayler.execute(userByIdCommand);
      assertEquals(user.getName(), user1.getName());

      UpdateUserCommand updateUserCommand = new UpdateUserCommand(primaryKey, "Hubert_" + user1.getId());
      repositoryPrevayler.execute(updateUserCommand);

      user1 = repositoryPrevayler.execute(userByIdCommand);
      assertEquals("Hubert_" + user1.getId(), user1.getName());
    }

}
