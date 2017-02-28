package app;

import app.api.APIController;
import app.index.IndexController;
import app.login.LoginController;
import app.passwords.PasswordController;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

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

        // Configure Spark
        port(800);
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
        post(Path.Web.ME, PasswordController.handlePasswordAdd);

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
