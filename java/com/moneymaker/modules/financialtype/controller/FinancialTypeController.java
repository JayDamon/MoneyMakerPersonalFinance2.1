package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Bean;
import com.moneymaker.modules.financialtype.list.FinancialTypeList;
import com.moneymaker.modules.financialtype.popup.NewPopupController;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Jay Damon on 7/20/2017.
 */
public abstract class FinancialTypeController<T extends Bean> implements Initializable {

    final String newFXMLPath;
    final String updateFXMLPath;

    @FXML
    Pane primaryPane;

    @FXML
    TableView<T> primaryTable;

    @FXML
    public Button buttonNew;
    public Button buttonDelete;
    public Button buttonUpdate;

    private FinancialTypeList<T> itemList;

    FinancialTypeList<T> getItemList() {
        return itemList;
    }

    void setItemList(FinancialTypeList<T> itemList) {
        this.itemList = itemList;
    }

    FinancialTypeController(String newFXMLPath, String updateFXMLPath) {
        this.newFXMLPath = newFXMLPath;
        this.updateFXMLPath = updateFXMLPath;
    }

    public void initialize(URL url, ResourceBundle rs) {
        buttonNew.setOnAction(e -> createNew());
        buttonDelete.setOnAction(e -> delete());
        buttonUpdate.setOnAction(e -> update());
        primaryTable.setOnMouseClicked(e -> {
            primaryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            if (e.getButton() == MouseButton.SECONDARY) {
                launchContextMenu(e.getScreenX(), e.getScreenY());
            }
        });
        primaryTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) delete();
        });

        addItemsToTable();
    }

    private void addItemsToTable() {
        ObservableList<T> list = this.getItemList().getList();
        list.addListener((ListChangeListener<T>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    primaryTable.getItems().addAll(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    for (T t : c.getRemoved()) {
                        primaryTable.getItems().remove(t);
                    }
                }
            }
        });
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list);
    }

    void createNew() {
        if (newFXMLPath != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(this.newFXMLPath));
            try {
                AnchorPane newWindow = loader.load();
                Stage stage = getStage(newWindow, "New Item");
                NewPopupController<FinancialTypeList<T>> cont = loader.getController();
                cont.setListToAddTo(this.itemList);

                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        ObservableList<T> financialType = primaryTable.getSelectionModel().getSelectedItems();
        if(financialType.size() == 1) {
            for (T t : financialType) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(this.updateFXMLPath));
                try {
                    AnchorPane newWindow = loader.load();
                    Stage stage = getStage(newWindow, "Update Item");
                    UpdatePopupController<T> controller = loader.getController();
                    controller.setItemToUpdate(t);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (financialType.size() == 0 ) {
            launchAlertWindow("No Items Selected", "You must select at least one item");
        } else {
            launchAlertWindow("Too Many Items Selected", "You can only update one item at a time.");
        }
    }

    private void launchAlertWindow(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(body);
        alert.showAndWait();
    }

    Stage getStage(AnchorPane newWindow, String stageTitle) {
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryPane.getScene().getWindow());
        Scene scene = new Scene(newWindow);
        stage.setScene(scene);
        return stage;
    }

    void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Selected Item(s)");
        alert.setContentText("Are you sure you want to delete the selected item(s)? " +
                "Item(s) will be permanently deleted.");

        Optional<ButtonType> result = alert.showAndWait();
        ObservableList<T> financialTypes = primaryTable.getSelectionModel().getSelectedItems();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (T f : financialTypes) {
                if (f != null) {
                    f.getBehavior().delete();
                }
            }
            this.itemList.getList().removeAll(financialTypes);
        }
    }

    ContextMenu getContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem createNew = new MenuItem("New");
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");

        createNew.setOnAction(event -> {
            createNew();
            event.consume();
        });
        update.setOnAction(event -> {
            update();
            event.consume();
        });

        delete.setOnAction(event -> {
            delete();
            event.consume();
        });

        contextMenu.getItems().addAll(createNew, update, delete);

        return contextMenu;
    }

    protected abstract void launchContextMenu(double x, double y);

}
