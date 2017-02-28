package app.index;

import app.Application;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class IndexController
{
    public static Route serveMeIndex = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        if(request.session().attribute("currentUser") != null)
        {
            model.put("signedIn", true);
            User user = Application.getApplication().getUserDao().getUserByUsername(request.session().attribute("currentUser"));
            model.put("currentUserCode", user.getFACode().get());
            model.put("passwords", user.getPasswords());
        }
        else
            model.put("signedIn", false);
        return ViewUtil.render(request, model, Path.Template.ME);
    };

    public static Route serveLanding = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        model.put("users", Application.getApplication().getUserDao().getAllUserNames().size());
        return ViewUtil.render(request, model, Path.Template.LANDING);
    };
}
