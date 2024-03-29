package app.login;

import app.Application;
import app.user.User;
import app.user.UserController;
import app.util.Path;
import app.util.ViewUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static app.util.RequestUtil.*;

public class LoginController
{
    public static Route serveLoginPage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        if (!UserController.authenticate(getQueryUsername(request), getQueryPassword(request)))
        {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }
        model.put("authenticationSucceeded", true);
        request.session().attribute("currentUser", getQueryUsername(request));
        response.redirect("/me");
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route serveCreatePage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.CREATE);
    };

    public static Route handleCreatePost = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        if (Application.getApplication().getUserDao().getUserByUsername(getQueryUsername(request)) != null)
        {
            model.put("creationFailed", true);
            return ViewUtil.render(request, model, Path.Template.CREATE);
        }
        model.put("creationSuccess", true);
        request.session().attribute("currentUser", getQueryUsername(request));

        User user = new User(getQueryUsername(request));

        String password = Long.toHexString(Double.doubleToLongBits(Math.random()));
//        String password = "password";
        String salt = BCrypt.gensalt();
        String hashPassword = BCrypt.hashpw(password, salt);
        user.getSalt().set(salt);
        user.getHashedPassword().set(hashPassword);
        user.getFACode().set(RandomStringUtils.randomAlphabetic(10));

        Application.getApplication().getUserDao().addUser(user);

        String to = getQueryUsername(request);
        String from = "noreply@sethy.xyz";
        String host = "localhost";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        String body = ("Thank You for using sAuth, Your login code for the account " + user.getUsername().get() + " is " + password + ". If You encounter any issues contact support@sethy.xyz");

        Session session = Session.getDefaultInstance(properties);


        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        message.setSubject("Your sAuth account password.");
        message.setText(body);

        Transport.send(message);


        Application.getApplication().getUserDao().saveToRedis(user);

        if (getQueryLoginRedirect(request) != null)
        {
            response.redirect(getQueryLoginRedirect(request));
        }
        return ViewUtil.render(request, model, Path.Template.CREATE);
    };

    public static Route handleLogoutPost = (Request request, Response response) ->
    {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    // The origin of the request (request.pathInfo()) is saved in the session so
    // the user can be redirected back after login
    @Deprecated
    public static void ensureUserIsLoggedIn(Request request, Response response)
    {
        if (request.session().attribute("currentUser") == null)
        {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    }
}
