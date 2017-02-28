package app.api;

import app.Application;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Seth on 24/01/2017.
 */
public class APIController
{
    public static Route serveAPI = (Request request, Response response) ->
    {
        Map<String, Object> model = new ConcurrentHashMap<>();
        User user = Application.getApplication().getUserDao().getUserByUsername(request.params("name"));
        model.put("username", user.getUsername().get());
        model.put("code", user.getFACode().get());
        String inputCode = request.params("code");
        if(inputCode.equals(user.getFACode().get()))
            model.put("canAuthenticate", true);
        else
            model.put("canAuthenticate", false);
        return ViewUtil.render(request, model, Path.Template.API);
    };
}
