package dao;

import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import static utils.IgniteConfigHelper.USER_CACHE;

public class UserDao {
    private IgniteCache<Integer, User> userCache;

    public UserDao(Ignite ignite){
        this.userCache = ignite.cache(USER_CACHE);
    }

    public User getUserById(int userId) {
        return userCache.get(userId);
    }
}
