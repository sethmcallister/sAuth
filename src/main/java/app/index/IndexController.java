package app.index;

import app.Application;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import org.apache.commons.lang3.RandomStringUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

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
