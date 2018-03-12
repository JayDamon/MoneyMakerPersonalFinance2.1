package com.moneymaker;

import com.moneymaker.main.MainWindowController;
import com.moneymaker.main.UsernameData;
import com.moneymaker.utilities.gui.LaunchStage;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
//        USERNAME_DATA.setCredentials("test", "test");
//        USERNAME_DATA.clearCredentials();
//        USERNAME_DATA.clearAutoLogin();
        if (UsernameData.getAutoLogin() == null || UsernameData.getUsername() == null || UsernameData.getPassword() == null) {
            openLoginWindow();
        } else {
            UsernameData.setSessionCredentials(UsernameData.getUsername(), UsernameData.getPassword());
            openMainWindow();
        }
    }

    @FXML
    private void openLoginWindow() {
        LaunchStage.launchStage(
                new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/loginWindow.fxml"))
        );
    }

    @FXML
    private void openMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/mainWindow.fxml"));
        Stage stage = LaunchStage.getStage(loader);
        MainWindowController mainWindow = loader.getController();
        mainWindow.setUserName(UsernameData.getUsername());
        if (stage != null) stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
