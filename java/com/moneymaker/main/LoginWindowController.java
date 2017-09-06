package com.moneymaker.main;

import com.moneymaker.utilities.SQLAdmin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created for MoneyMaker by Jay Damon on 3/30/2016.
 */
public class LoginWindowController implements Initializable {

    @FXML
    private AnchorPane primaryPane;

    @FXML
    private CheckBox checkBoxSaveCredentials, checkBoxAutoLogin;

    @FXML
    private Button buttonLogin;

    @FXML
    private PasswordField passwordFieldLogin;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private Label invalidField;

    private final UsernameData USERNAME_DATA = new UsernameData();

    @FXML
    private Button testUser;

    public void initialize(URL url, ResourceBundle rs) {
        testUser.setOnAction(event -> testUserLogin());
        passwordFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                passwordFieldLogin.setStyle("-fx-control-inner-background: white");
            }
        });

        textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textFieldUsername.setStyle("-fx-control-inner-background: white");
            }
        });

        if(UsernameData.getSaveCredentials() != null || UsernameData.getUsername() != null) {
            checkBoxSaveCredentials.setSelected(true);
            textFieldUsername.setText(UsernameData.getUsername());
        }
        primaryPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buttonLogin.requestFocus();
                loginAction();
            }
        });

    }

    private void testUserLogin() {
        boolean userExists = SQLAdmin.userExists("testuser", "testuser");
        if (userExists) {
            UsernameData.clearUsername();
            UsernameData.clearPassword();
            UsernameData.setAutoLogin(false);
            UsernameData.setSessionCredentials("testuser","testuser");
            exitWindow();
            openMainWindow("testuser");
        }
    }

    @FXML
    private void loginAction() {
        if (passwordFieldLogin.getText().isEmpty() || textFieldUsername.getText().isEmpty()) {
            if (passwordFieldLogin.getText() == null || passwordFieldLogin.getText().isEmpty()) {
                passwordFieldLogin.setStyle("-fx-control-inner-background: red");
            }
            if (textFieldUsername.getText() == null || textFieldUsername.getText().isEmpty()) {
                textFieldUsername.setStyle("-fx-control-inner-background: red");
            }
        } else {
            String userName = textFieldUsername.getText();
            String password = passwordFieldLogin.getText();

            boolean userExists = SQLAdmin.userExists(userName, password);

            if (userExists) {
                if (checkBoxSaveCredentials.isSelected()) {
                    UsernameData.setUsername(userName);
                    UsernameData.setSaveCredentials();
                    UsernameData.clearPassword();
                } else {
                    UsernameData.clearUsername();
                    UsernameData.clearPassword();
                }

                if (checkBoxAutoLogin.isSelected()) {
                    UsernameData.setAutoLogin(true);
                    UsernameData.setUsername(userName);
                    UsernameData.setPassword(password);
                }

                UsernameData.setSessionCredentials(userName, password);

                exitWindow();

                openMainWindow(userName);
            } else {
                invalidField.setText("Username or Password is invalid");
            }
        }
    }

    private void openMainWindow(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/mainWindow.fxml"));
            AnchorPane primaryStage = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Budget");
            Scene scene = new Scene(primaryStage);
            stage.setScene(scene);
            MainWindowController mainWindow = loader.getController();
            mainWindow.setUserName(userName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void newUser(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/newUser.fxml"));
        AnchorPane primaryStage = loader.load();
        Stage stage = new Stage();
        stage.setTitle("New User Registration");
        Scene scene = new Scene(primaryStage);
        stage.setScene(scene);
        stage.show();

    }

    private void exitWindow() {
        Stage stage = (Stage)primaryPane.getScene().getWindow();
        stage.close();
    }

}
