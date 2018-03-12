package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.list.TransferList;
import com.moneymaker.modules.financialtype.popup.newpopup.NewTransferController;
import com.moneymaker.modules.financialtype.popup.newpopup.NewTransferEmptyTransactionController;
import com.moneymaker.utilities.gui.PopupController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created for MoneyMaker by Jay Damon on 10/20/2016.
 */
public class TransferController extends FinancialTypeController<Transfer> {

    public TransferController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(TransferList.getInstance().activateList());
    }

    public void initialize(URL url, ResourceBundle rs) {
        super.initialize(url, rs);
        setTableSelectMultiple(primaryTable);
        setDeleteKeyAction(primaryTable);
    }

    private void setTableSelectMultiple(TableView<Transfer> table) {
        table.setOnMouseClicked(event -> {
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            event.consume();
        });
    }

    private void setDeleteKeyAction(TableView<Transfer> table) {
        table.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DELETE) {
                delete();
            }
        });
    }

    @Override
    protected void createNew() {

        Stage stage = new Stage();
        stage.setTitle("New Transfer");
        GridPane gridPane = new GridPane();
        Label label = new Label();
        label.setText("Have the transactions for this transfer already been imported?");
        label.setPrefWidth(350);
        Button buttonYes = new Button();
        buttonYes.setText("Yes");
        buttonYes.setAlignment(Pos.BASELINE_CENTER);
        Button buttonNo = new Button();
        buttonNo.setText("No");
        buttonNo.setAlignment(Pos.BASELINE_CENTER);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(buttonYes, buttonNo);
        hBox.setAlignment(Pos.BASELINE_RIGHT);

        gridPane.setVgap(10);
        gridPane.setHgap(20);
        gridPane.add(label, 1, 1);
        gridPane.add(hBox, 1, 2);

        buttonYes.setOnAction(event -> {
            event.consume();
            Stage getStage = (Stage)buttonYes.getScene().getWindow();
            getStage.close();
            launchPopupWindow(new NewTransferController(), this.updateFXMLPath);
        });

        buttonNo.setOnAction(event -> {
            event.consume();
            Stage getStage = (Stage)buttonNo.getScene().getWindow();
            getStage.close();
            launchPopupWindow(new NewTransferEmptyTransactionController(), this.newFXMLPath);
        });

        stage.setScene(new Scene(gridPane, 390,80));
        stage.show();
    }

    private void launchPopupWindow(PopupController c, String path) {
        if (path != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            try {
                loader.setController(c);
                AnchorPane newWindow = loader.load();
                Stage stage = this.getStage(newWindow, "New Item");

                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu contextMenu = getContextMenu();
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(contextMenu);
        contextMenu.show(pane, x, y);
    }

}
