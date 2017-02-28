package app.user;

import app.serialziers.PasswordSerializer;
import app.serialziers.UserSerialzier;
import app.user.password.PasswordEntry;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class UserDao
{
    private final List<User> users;
    private final Jedis jedis;
    private final String jedisKey = "sauth.users.";
    private final String passKey = "sauth.pass.";
    private final List<PasswordEntry> passwords;

    public UserDao()
    {
        this.jedis = new Jedis();
        this.users = loadAllFromRedis();
        this.passwords = loadAllPasswordsFromRedis();

        for(PasswordEntry passwordEntry : passwords)
        {
            if(passwordEntry.getUserName().get() != null)
            {
                getUserByUsername(passwordEntry.getUserName().get()).getPasswords().add(passwordEntry);
            }
        }

        System.out.println("everything loaded");
    }

    private List<PasswordEntry> loadAllPasswordsFromRedis()
    {
        System.out.println("loading passwords");
        List<PasswordEntry> passwords = new ArrayList<>();
        for(String key : jedis.keys(passKey + "*"))
        {
            String passStrig = this.jedis.get(key);
            PasswordEntry passwordEntry = new PasswordSerializer().deserialize(passStrig);
            passwords.add(passwordEntry);
        }
        System.out.println("loaded all passwrods");
        return passwords;
    }

    public void savePasswordToRedis(PasswordEntry passwordEntry)
    {
        jedis.set(passKey + UUID.randomUUID().toString(), new PasswordSerializer().serialize(passwordEntry));
    }

    private List<User> loadAllFromRedis()
    {
        System.out.println("loading all users");
        List<User> users = new ArrayList<>();
        for(String key : jedis.keys(jedisKey + "*"))
        {
            String userString = this.jedis.get(key);
            User user = new UserSerialzier().deserialize(userString);
            users.add(user);
        }
        System.out.println("loaded all users");
        return users;
    }

    public User loadFromRedis(String key)
    {
        String userString = this.jedis.get(jedisKey + key);
        User user = new UserSerialzier().deserialize(userString);
        this.users.add(user);
        return user;
    }

    public void saveToRedis(User user)
    {
        String userString = new UserSerialzier().serialize(user);
        this.jedis.set(jedisKey + user.getUsername().get(), userString);
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
