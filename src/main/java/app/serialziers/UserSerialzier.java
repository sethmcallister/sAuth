package app.serialziers;

import app.user.User;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Seth on 24/01/2017.
 */
public class UserSerialzier
{
    public User deserialize(String string)
    {
        if (string == null)
            return null;

        User user = new User();

        final String[] strings = string.split("\n");
        for (String line : strings)
        {
            final String identifier = line.substring(0, line.indexOf(58));
            final String[] lineParts = line.substring(line.indexOf(58)).split(",");
            if (identifier.equalsIgnoreCase("phonenumber"))
                user.getUsername().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("hashpassword"))
                user.getHashedPassword().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("salt"))
                user.getSalt().set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("2facode"))
                user.getFACode().set(lineParts[0].replace(":", ""));
        }

        return user;
    }

    public String serialize(User src)
    {
        StringBuilder object = new StringBuilder();
        object.append("phonenumber:").append(src.getUsername().get()).append("\n");
        object.append("hashpassword:").append(src.getHashedPassword().get()).append("\n");
        object.append("salt:").append(src.getSalt().get()).append("\n");
        object.append("2facode:").append(src.getFACode().get()).append("\n");
        return object.toString();
    }
}
