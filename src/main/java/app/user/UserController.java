package app.user;

import org.mindrot.jbcrypt.BCrypt;

import static app.Application.getApplication;

public class UserController
{

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty())
        {
            return false;
        }
        User user = getApplication().getUserDao().getUserByUsername(username);
        if (user == null)
        {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, user.getSalt().get());
        return hashedPassword.equals(user.getHashedPassword().get());
    }

    // This method doesn't do anything, it's just included as an example
    public static void setPassword(String username, String oldPassword, String newPassword)
    {
        if (authenticate(username, oldPassword))
        {
            String newSalt = BCrypt.gensalt();
            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
            // Update the user salt and password
        }
    }
}
