package com.project.chefskiss.configurations;

import java.util.Calendar;

public class Config {
    /* Database Configuration */
    public static final String DATABASE_IMPL = "MySQLJDBCImpl";
    public static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE = Calendar.getInstance().getTimeZone().getID();
    public static final String DATABASE_URL = "jdbc:mysql://localhost/chefskiss?user=utente_chefskiss&password=utentePassword&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=" + SERVER_TIMEZONE;

    public static final String PLATE_DEFAULT_IMG = "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";

    /* Cookie Configuration */
    public static final String COOKIE_IMPL = "CookieImpl";

    public static final Integer COOKIE_EXPIRATION_TIME = 60*60;

    /* Server Location Configuration */

    public static final String SERVER_ADDRESS_PORT = "http://localhost:8080";

    public static final String IMG_PATH = "C:\\Users\\david\\Documents\\UNIFE\\Sistemi Web\\chefsKiss\\src\\main\\resources\\static\\img\\";

    public static final String ENC_SEPARATOR = "&";

    public static final Integer MAX_VALUTAZIONI = 9;

}
