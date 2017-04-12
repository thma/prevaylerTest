package org.thma;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thma.commands.AddUserCommand;
import org.thma.commands.GetUserByIdCommand;
import org.thma.commands.UpdateUserCommand;
import org.thma.domain.User;
import org.thma.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/users", produces = "application/json")
public class AppController {

    Prevayler<UserRepository> repositoryPrevayler;

    public AppController() throws Exception {
        repositoryPrevayler = PrevaylerFactory.createPrevayler(new UserRepository());
        repositoryPrevayler.takeSnapshot();
    }


    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> allUsers() throws Exception {
        List<User> result = new ArrayList<>(repositoryPrevayler.prevalentSystem().getAllUsers());
        return result;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUser(@PathVariable("id") long id) throws Exception {
        User result = repositoryPrevayler.prevalentSystem().getUser(id);
        repositoryPrevayler.takeSnapshot();
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
    public
    @ResponseBody
    User postUser(@RequestBody String userName) throws Exception {
        long id = repositoryPrevayler.prevalentSystem().size();
        final User user = new User(id, userName);
        Transaction<UserRepository> tx = new AddUserCommand(user);
        repositoryPrevayler.execute(tx);
        return user;
    }


    @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
    public
    @ResponseBody
    User putUser(@RequestBody final User user) throws Exception {
        User userToUpdate = repositoryPrevayler.execute(new GetUserByIdCommand(user.getId()));
        repositoryPrevayler.execute(new UpdateUserCommand(user.getId(), user.getName()));
        userToUpdate.setName(user.getName());
        return userToUpdate;
    }


}
