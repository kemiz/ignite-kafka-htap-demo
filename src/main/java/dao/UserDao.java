package dao;

import data.model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;


public class UserDao {
    private final String USER_CACHE = "UserCache";
    private IgniteCache<Integer, User> userCache;

    public UserDao(Ignite ignite){
        this.userCache = ignite.cache(USER_CACHE);
    }

    public User getUserById(int userId) {
        return userCache.get(userId);
    }
}
