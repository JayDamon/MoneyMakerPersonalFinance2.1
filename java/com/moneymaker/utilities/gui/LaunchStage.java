package com.moneymaker.utilities.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jay Damon on 7/22/2017.
 */
public class LaunchStage {

    public void launchPopup(String fxmlPath, String title, Pane initOwner) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        try {
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle((title));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(initOwner.getScene().getWindow());
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPopupStage(String fxmlPath, String title, Pane initOwner, PopupController cont) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        try {
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle((title));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(initOwner.getScene().getWindow());
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
