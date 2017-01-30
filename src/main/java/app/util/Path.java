package app.util;

import lombok.Getter;

public class Path
{

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web
    {
        @Getter
        public static final String ME = "/me/";
        @Getter
        public static final String LOGIN = "/login/";
        @Getter
        public static final String LOGOUT = "/logout/";
        @Getter
        public static final String CREATE = "/create/";
        @Getter
        public static final String API = "/api/:name/:code/";
        @Getter
        public static final String LANDING = "/";
    }

    public static class Template
    {
        public final static String ME = "/velocity/user/me.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String CREATE = "/velocity/user/create.vm";
        public final static String API = "/velocity/api/api.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public static final String LANDING = "/velocity/index/index.vm";
    }
}
