package app;

import app.api.APIController;
import app.index.IndexController;
import app.login.LoginController;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;
import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Application
{
    private static Application application;
    private final UserDao userDao;

    public Application()
    {
        // Instantiate your dependencies
        application = this;
        userDao = new UserDao();

        Twilio.init("AC70c348e5ab5311610bb3e8733130299b", "4d32809df55ebee6b7e79dac708111b6");

        // Configure Spark
        port(80);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.handleLocaleChange);

        // Set up routes
        get("/", IndexController.serveLanding);
        get(Path.Web.ME, IndexController.serveMeIndex);
        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        post(Path.Web.CREATE, LoginController.handleCreatePost);
        get(Path.Web.CREATE, LoginController.serveCreatePage);
        get(Path.Web.API, APIController.serveAPI);
        get("*", ViewUtil.notFound);
        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);
    }

    public static Application getApplication()
    {
        return application;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }
}
