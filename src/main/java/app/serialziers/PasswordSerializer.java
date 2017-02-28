package app.serialziers;

import app.user.password.PasswordEntry;

/**
 * Created by Seth on 27/02/2017.
 */
public class PasswordSerializer
{
    public PasswordEntry deserialize(String string)
    {
        if (string == null)
            return null;

        PasswordEntry passwordEntry = new PasswordEntry();

        final String[] strings = string.split("\n");
        for (String line : strings)
        {
            final String identifier = line.substring(0, line.indexOf(58));
            final String[] lineParts = line.substring(line.indexOf(58)).split(",");
            if (identifier.equalsIgnoreCase("salt"))
                passwordEntry.getSalt().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("account"))
                passwordEntry.getAccount().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("password"))
                passwordEntry.getPassword().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("user"))
            {
                System.out.println(lineParts[0].replace(":", ""));
                System.out.println(passwordEntry.getUserName());
                passwordEntry.getUserName().set(lineParts[0].replace(":", ""));
            }
        }
        return passwordEntry;
    }

    public String serialize(PasswordEntry src)
    {
        StringBuilder object = new StringBuilder();
        object.append("user:").append(src.getUserName().get()).append("\n");
        object.append("salt:").append(src.getSalt().get()).append("\n");
        object.append("account:").append(src.getAccount().get()).append("\n");
        object.append("password:").append(src.getPassword().get()).append("\n");
        return object.toString();
    }
}
