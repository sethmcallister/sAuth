package app.user;

import app.user.password.PasswordEntry;
import lombok.Getter;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class User
{
    @Getter
    private final AtomicReference<String> username;
    @Getter
    private final AtomicReference<String> salt;
    @Getter
    private final AtomicReference<String> hashedPassword;
    @Getter
    private final AtomicReference<String> FACode;
    @Getter
    private final LinkedList<PasswordEntry> passwords;

    public User(String username)
    {
        this.username = new AtomicReference<>(username);
        this.salt = new AtomicReference<>();
        this.hashedPassword = new AtomicReference<>();
        this.FACode = new AtomicReference<>();
        this.passwords = new LinkedList<>();

//        PasswordEntry password = new PasswordEntry(this);
//        password.getAccount().set("Facebook");
//        password.getPassword().set("password");
//        password.getSalt().set(BCrypt.gensalt());
//        this.passwords.add(password);
//
//        PasswordEntry password1 = new PasswordEntry(this);
//        password1.getAccount().set("Twitter");
//        password1.getPassword().set("password");
//        password1.getSalt().set(BCrypt.gensalt());
//        this.passwords.add(password1);

    }

    public User()
    {
        this.username = new AtomicReference<>();
        this.salt = new AtomicReference<>();
        this.hashedPassword = new AtomicReference<>();
        this.FACode = new AtomicReference<>();
        this.passwords = new LinkedList<>();
    }
}
