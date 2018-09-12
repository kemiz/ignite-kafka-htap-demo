package dao;

import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import static utils.IgniteConfigHelper.USER_CACHE;

public class UserDao {
    private IgniteCache<String, User> userCache;

    public UserDao(Ignite ignite){
        this.userCache = ignite.cache(USER_CACHE);
    }

    public User getUserById(String userId) {
        return userCache.get(userId);
    }

    public int getUserCount() {
        return userCache.size();
    }

    public void addUser(User user) {
        userCache.put(user.getId(), user);
    }
}
