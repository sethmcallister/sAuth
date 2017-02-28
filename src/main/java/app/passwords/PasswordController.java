package app.passwords;

import app.Application;
import app.serialziers.PasswordSerializer;
import app.user.User;
import app.user.password.PasswordEntry;
import app.util.Path;
import app.util.ViewUtil;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.*;

/**
 * Created by Seth on 27/02/2017.
 */
public class PasswordController
{
    public static Route handlePasswordAdd = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        User user = Application.getApplication().getUserDao().getUserByUsername(request.session().attribute("currentUser"));

        System.out.println(user.getUsername().get());

        String account = getQueryAccountName(request);
        String password = getQueryAccountPassword(request);
        System.out.println(account);
        System.out.println(password);

        PasswordEntry passwordEntry = new PasswordEntry(user);
        passwordEntry.getAccount().set(account);
        passwordEntry.getPassword().set(password);
        passwordEntry.getSalt().set(BCrypt.gensalt());
        passwordEntry.getUserName().set(user.getUsername().get());

        System.out.println("password serialized -> " + new PasswordSerializer().serialize(passwordEntry));

        user.getPasswords().add(passwordEntry);

        Application.getApplication().getUserDao().saveToRedis(user);

        model.put("createdPass", true);
        model.put("passwords", user.getPasswords());
        Application.getApplication().getUserDao().savePasswordToRedis(passwordEntry);
        response.redirect(Path.Web.ME);
        return ViewUtil.render(request, model, Path.Template.ME);
    };
}
