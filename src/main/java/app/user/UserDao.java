package app.user;

import app.serialziers.UserSerialzier;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class UserDao
{
    private final List<User> users;
    private final Jedis jedis;
    private final String jedisKey = "sauth.users.";

    public UserDao()
    {
        this.users = new LinkedList<>();
        this.jedis = new Jedis();

        User adminUser = new User("admin");
        adminUser.getSalt().set("$2a$10$h.dl5J86rGH7I8bD9bZeZe");
        adminUser.getHashedPassword().set(BCrypt.hashpw("password", "$2a$10$h.dl5J86rGH7I8bD9bZeZe"));
        adminUser.getFACode().set(RandomStringUtils.randomAlphabetic(30));
        users.add(adminUser);
    }

    public User loadFromRedis(String key)
    {
        String userString = this.jedis.get(jedisKey + key);
        User user = new UserSerialzier().deserialize(userString);
        this.users.add(user);
        return user;
    }

    public boolean unloadToRedis(User user)
    {
        boolean result = false;
        try
        {
            String userString = new UserSerialzier().serialize(user);
            this.users.remove(user);
            this.jedis.set(jedisKey + user.getUsername().get(), userString);
        }
        finally
        {
            result = true;
        }
        return result;
    }

    public User getUserByUsername(String username)
    {
        for(User user : users)
        {
            if(user.getUsername() != null)
            {
                if(user.getUsername().get().equals(username))
                {
                    return user;
                }
            }
        }
        return null;
    }

    public List<AtomicReference<String>> getAllUserNames()
    {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public boolean addUser(User user)
    {
        return this.users.add(user);
    }
}
