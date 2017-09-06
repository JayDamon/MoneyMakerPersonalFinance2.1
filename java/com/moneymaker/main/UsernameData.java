package com.moneymaker.main;

import com.moneymaker.utilities.SQLAdmin;

import java.util.prefs.Preferences;

/**
 * Created for MoneyMaker by Jay Damon on 6/25/2016.
 */
public class UsernameData {

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(UsernameData.class);

    static void setUsername(String username) {
        PREFERENCES.put("db_username", username);
    }

    static void setPassword(String password) {
        PREFERENCES.put("db_password", password);
    }

    public static void setSessionCredentials(String sessionUserName, String sessionPassword) {
        PREFERENCES.put("db_sessionUsername", sessionUserName);
        PREFERENCES.put("db_sessionPassword", sessionPassword);
    }

    static void clearUsername() {
        PREFERENCES.remove("db_username");
    }

    static void clearPassword() {
        PREFERENCES.remove("db_password");
    }

//    public void clearSessionCredentials() {
//        PREFERENCES.remove("db_sessionUsername");
//        PREFERENCES.remove("db_sessionPassword");
//    }
//
//    public void clearAutoLogin() {
//        PREFERENCES.remove("db_autoLogin");
//    }
//
//    public void clearSaveCredentials() {
//        PREFERENCES.remove("db_saveCredentials");
//    }

    static void setAutoLogin(boolean autoLogin) {
        PREFERENCES.put("db_autoLogin", String.valueOf(autoLogin));
    }

    static void setSaveCredentials() { PREFERENCES.put("db_saveCredentials",
            String.valueOf(true)); }

    public static String getUsername() {
        return PREFERENCES.get("db_username", null);
    }

    public static String getPassword() {
        return PREFERENCES.get("db_password", null);
    }

    public static String getSessionUsername() {
        return PREFERENCES.get("db_sessionUsername", null);
    }

    public static String getSessionPassword() {
        return PREFERENCES.get("db_sessionPassword", null);
    }

    public static String getAutoLogin() {
        return PREFERENCES.get("db_autoLogin", null);
    }

    static String getSaveCredentials() {
        return PREFERENCES.get("db_saveCredentials", null);
    }

    public static String getUserSchema() {
        String userName = PREFERENCES.get("db_sessionUsername", null);
        String password = PREFERENCES.get("db_sessionPassword", null);
        return SQLAdmin.getUserSchema(userName, password);
    }

}
