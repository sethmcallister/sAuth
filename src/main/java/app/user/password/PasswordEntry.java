package app.user.password;

import app.user.User;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Seth on 30/01/2017.
 */
public class PasswordEntry
{
    private User user;
    private AtomicReference<String> userName;
    private AtomicReference<String> account;
    private AtomicReference<String> password;
    private AtomicReference<String> salt;

    public PasswordEntry(User name)
    {
        this.user = name;
        this.account = new AtomicReference<>();
        this.password = new AtomicReference<>();
        this.salt = new AtomicReference<>();
        this.userName = new AtomicReference<>();
    }

    public PasswordEntry()
    {
        this.user = null;
        this.account = new AtomicReference<>();
        this.password = new AtomicReference<>();
        this.salt = new AtomicReference<>();
        this.userName = new AtomicReference<>();
    }

    public AtomicReference<String> getAccount()
    {
        return account;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public AtomicReference<String> getPassword()
    {
        return password;
    }

    public AtomicReference<String> getSalt()
    {
        return salt;
    }

    public AtomicReference<String> getUserName()
    {
        return userName;
    }
}
