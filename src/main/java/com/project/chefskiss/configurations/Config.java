package com.project.chefskiss.configurations;

import java.util.Calendar;

public class Config {
    /* Database Configuration */
    public static final String DATABASE_IMPL = "MySQLJDBCImpl";
    public static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE = Calendar.getInstance().getTimeZone().getID();
    public static final String DATABASE_URL = "jdbc:mysql://localhost/chefskiss?user=chefskiss_user&password=password&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=" + SERVER_TIMEZONE;

    /* Cookie Configuration */
    public static final String COOKIE_IMPL = "CookieImpl";

    public static final Integer COOKIE_EXPIRATION_TIME = 60*60;

    /* Server Location Configuration */

    public static final String PROFILE_IMG_PATH = "C:\\Users\\david\\Documents\\UNIFE\\Sistemi Web\\chefsKiss\\src\\main\\resources\\static\\profileImg.jpg";

    public static final String ENC_SEPARATOR = "&";

}
