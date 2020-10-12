package com.example.dobble.release;

public class Cfg {
    public static final int REQUEST_TIMEOUT = 2;
    public static final String PREF_NAME_COOKIE = "cookie";

    public static final String PREF_USER = "PREF_USER";
    public static final String USER_ID_KEY = "USER_ID";

    public static final String COOKIE_SID = "sid";

    public static final int REGISTER_MIN_PASSWORD_LENGTH = 3;
    public static final int REGISTER_MIN_FIRST_NAME_LENGTH = 2;
    public static final int REGISTER_MIN_LAST_NAME_LENGTH = 2;
    public static final int REGISTER_MIN_CITY_LENGTH = 2;
    public static final int REGISTER_MIN_STATE_LENGTH = 2;


    public static final String NAV_BUNDLE_USER_ID_KEY = "NAV_BUNDLE_USER_ID_KEY";

    public static final String BASE_URL = "http://37.193.146.146:4000/";
    public static final String BASE_STATIC_URL = BASE_URL + "static/upload/";
    public static final String IMG_RESOLUTION_LOW = "200";
    public static final String IMG_RESOLUTION_MEDIUM = "800";
    public static final String IMG_RESOLUTION_HIGH = "1200";

    public static final int LOAD_IMG_REQUEST_CODE = 1;

    public static final int GET_RANDOM_USERS_LIMIT = 25;

    public static final String DATABASE_NAME = "database";

    public static final int REMOVE_FRIEND_VIEW_HOLDER_DELAY = 3;
    public static final int CANCEL_TYPE_ANIMATION_DELAY = 3;

    public static final String WEB_SOCKET_EVENT_MSG_ARRIVE = "message";
    public static final String WEB_SOCKET_EVENT_TYPE = "type";
    public static final String WEB_SOCKET_EVENT_MSG_JOIN = "message-join";
    public static final String WEB_SOCKET_EVENT_MSG_LEAVE = "message-leave";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String MALE_ID = "0";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";

    public static final String BODY_MISSING = "Body is missing";
    public static final String RESPONSE_FIELDS_MISSING = "Response fields are missing";
}
