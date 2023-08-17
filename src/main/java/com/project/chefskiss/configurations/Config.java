package com.project.chefskiss.configurations;

import java.util.Calendar;

public class Config {
    /* Database Configruation */
    public static final String DATABASE_IMPL="MySQLJDBCImpl";
    public static final String DATABASE_DRIVER="com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE= Calendar.getInstance().getTimeZone().getID();
    public static final String DATABASE_URL="jdbc:mysql://localhost/NOME_DB?user=USERNAME&password=PASSWORD&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;

    /* Cookie Configuration */
    public static final String COOKIE_IMPL= "CookieImpl";

    /* Root Login Data (for debug testing) */
    public static final String root_username = "root_Davide";
    public static final String root_password = "root";
}
