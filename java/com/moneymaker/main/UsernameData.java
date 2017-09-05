package com.moneymaker.main;

import com.moneymaker.utilities.SQLAdmin;

import java.util.prefs.Preferences;

/**
 * Created for MoneyMaker by Jay Damon on 6/25/2016.
 */
public class UsernameData {

    private static Preferences preferences = Preferences.userNodeForPackage(UsernameData.class);

    void setUsername(String username) {
        preferences.put("db_username", username);
    }

    void setPassword(String password) {
        preferences.put("db_password", password);
    }

    public void setSessionCredentials(String sessionUserName, String sessionPassword) {
        preferences.put("db_sessionUsername", sessionUserName);
        preferences.put("db_sessionPassword", sessionPassword);
    }

    void clearUsername() {
        preferences.remove("db_username");
    }

    void clearPassword() {
        preferences.remove("db_password");
    }

//    public void clearSessionCredentials() {
//        preferences.remove("db_sessionUsername");
//        preferences.remove("db_sessionPassword");
//    }
//
//    public void clearAutoLogin() {
//        preferences.remove("db_autoLogin");
//    }
//
//    public void clearSaveCredentials() {
//        preferences.remove("db_saveCredentials");
//    }

    void setAutoLogin(boolean autoLogin) {
        preferences.put("db_autoLogin", String.valueOf(autoLogin));
    }

    void setSaveCredentials() { preferences.put("db_saveCredentials",
            String.valueOf(true)); }

    public String getUsername() {
        return preferences.get("db_username", null);
    }

    public String getPassword() {
        return preferences.get("db_password", null);
    }

    public String getSessionUsername() {
        return preferences.get("db_sessionUsername", null);
    }

    public String getSessionPassword() {
        return preferences.get("db_sessionPassword", null);
    }

    public String getAutoLogin() {
        return preferences.get("db_autoLogin", null);
    }

    String getSaveCredentials() {
        return preferences.get("db_saveCredentials", null);
    }

    public static String getUserSchema() {
        String userName = preferences.get("db_sessionUsername", null);
        String password = preferences.get("db_sessionPassword", null);
        return SQLAdmin.getUserSchema(userName, password);
    }

}
