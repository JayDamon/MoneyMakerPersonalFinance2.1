package com.moneymaker;

import com.moneymaker.main.MainWindowController;
import com.moneymaker.main.UsernameData;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/loginWindow.fxml"));
        try {
            AnchorPane primaryStage = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            Scene scene = new Scene(primaryStage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/mainWindow.fxml"));
        try {
            AnchorPane primaryStage = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Budget");
            Scene scene = new Scene(primaryStage);
            stage.setScene(scene);
            MainWindowController mainWindow = loader.getController();
            mainWindow.setUserName(UsernameData.getUsername());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
