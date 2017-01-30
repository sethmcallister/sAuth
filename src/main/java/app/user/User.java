package app.user;

import java.util.concurrent.atomic.AtomicReference;

public class User
{
    private final AtomicReference<String> username;
    private final AtomicReference<String> salt;
    private final AtomicReference<String> hashedPassword;
    private final AtomicReference<String> FACode;

    public User(String username)
    {
        this.username = new AtomicReference<>(username);
        this.salt = new AtomicReference<>();
        this.hashedPassword = new AtomicReference<>();
        this.FACode = new AtomicReference<>();
    }

    public User()
    {
        this.username = new AtomicReference<>();
        this.salt = new AtomicReference<>();
        this.hashedPassword = new AtomicReference<>();
        this.FACode = new AtomicReference<>();
    }

    public AtomicReference<String> getUsername()
    {
        return username;
    }

    public AtomicReference<String> getSalt()
    {
        return salt;
    }

    public AtomicReference<String> getHashedPassword()
    {
        return hashedPassword;
    }

    public AtomicReference<String> getFACode()
    {
        return FACode;
    }
}
