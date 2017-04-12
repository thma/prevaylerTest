package org.thma.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thma on 20.05.2015.
 */
public class UserRepository implements Serializable {

    /**
     * java.io.Serializable with a non changing serialVersionUID
     * will automatically handle backwards compatibility
     * if you add new non transient fields the the class.
     */
    private static final long serialVersionUID = 1l;

    private Map<Long, User> namesMap = new HashMap<>();

    public void addUser(User user) {
        namesMap.put(user.getId(), user);
    }

    public User getUser(long id) {
        return namesMap.get(id);
    }

    public Collection<User> getAllUsers() {
        return namesMap.values();
    }

    public long size() {
        return namesMap.size() + 1L;
    }
}
