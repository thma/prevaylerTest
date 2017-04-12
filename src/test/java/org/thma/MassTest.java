package org.thma;

import org.junit.Test;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Transaction;
import org.thma.commands.AddUserCommand;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

/**
 * Created by thma on 28.05.2015.
 */
public class MassTest {


  @Test
  public void shouldCreateManyUsersInShortTime() throws Exception {

    PrevaylerFactory pf = new PrevaylerFactory(new UserRepository());
    Prevayler<UserRepository> rootPrevayler = pf.create();
    rootPrevayler.takeSnapshot();

    long start = System.currentTimeMillis();
    for (int i=0; i<1000; i++) {
      final User user = new User(i, "user_" + i);
      Transaction<UserRepository> tx = new AddUserCommand(user);
      rootPrevayler.execute(tx);
    }
    rootPrevayler.takeSnapshot();

    long time = System.currentTimeMillis() - start;
    System.out.println("created in " + time + " msecs.");

  }
}
