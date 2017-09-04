package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.main.MainWindowController;
import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Budget;
import com.moneymaker.modules.financialtype.list.BudgetList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by jaynd on 6/26/2016.
 */
public class BudgetWindowController implements Initializable {

    @FXML private AnchorPane primaryPane;

    @FXML private Button btnExitWindow, btnAdd, btnRemove;

    @FXML private TextField txtCustomBudgetCategory;

    @FXML private ListView<String> listViewBudget, listViewInactiveBudget;

    public boolean newUser = false;

    private BudgetList budgetList = BudgetList.getInstance().activateList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBudgets();
        addListViewListener();
        addButtonListener();
    }

    @FXML
    private void showBudgets() {

        listViewBudget.getItems().clear();
        listViewInactiveBudget.getItems().clear();
        for (Budget b : budgetList.getAllBudgets()) {
            if (b.inUse()) {
                listViewBudget.getItems().add(b.getName());
            } else {
                listViewInactiveBudget.getItems().add(b.getName());
            }
        }
    }

    private void addListViewListener() {
        setDragDetectedBehavior(listViewInactiveBudget);
        setDragDetectedBehavior(listViewBudget);

        setDragOverBehavior(listViewInactiveBudget);
        setDragOverBehavior(listViewBudget);

        setDragDroppedBehavior(listViewInactiveBudget);
        setDragDroppedBehavior(listViewBudget);

        listViewBudget.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewInactiveBudget.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private void setDragDetectedBehavior(ListView<String> list) {
        list.setOnDragDetected(event -> {
            Dragboard db = list.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(list.getSelectionModel().getSelectedItem());
            db.setContent(content);
            event.consume();
        });
    }

    private void setDragOverBehavior(ListView<String> list) {
        list.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    if(event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                        list.requestFocus();
                    }
                    event.consume();
                });
    }

    private void setDragDroppedBehavior(ListView<String> list) {
        list.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    Dragboard dragboard = event.getDragboard();
                    if(event.getTransferMode() == TransferMode.COPY && dragboard.hasString() &&
                            event.getGestureSource() != list) {
                        if (list.equals(listViewBudget)) {
                            for (String s : listViewInactiveBudget.getSelectionModel().getSelectedItems()) {
                                activateBudgets(listViewInactiveBudget);
//                                activateInDB(s);
                            }
                        } else if (list.equals(listViewInactiveBudget)) {
                            for (String s : listViewBudget.getSelectionModel().getSelectedItems()) {
                                deactivateBudgets(listViewBudget);
                            }
//                            deactivateInDB(dragboard.getString());
                        }
                        event.setDropCompleted(true);
                    }
                    event.consume();
                }
        );
    }

    private void deactivateBudgets(ListView<String> list) {
        ObservableList<String> budgets = list.getSelectionModel().getSelectedItems();
        for (String s : budgets) {
            deactivateInDB(s);
        }
        listViewInactiveBudget.getItems().addAll(budgets);
        list.getItems().removeAll(budgets);
    }

    private void activateBudgets(ListView<String> list) {
        ObservableList<String> budgets = list.getSelectionModel().getSelectedItems();
        for (String s : budgets) {
            activateInDB(s);
        }
        listViewBudget.getItems().addAll(budgets);
        list.getItems().removeAll(budgets);
    }

    private void addButtonListener() {
        btnAdd.setOnAction(event -> {
            String item = listViewInactiveBudget.getSelectionModel().getSelectedItem();
            activateInDB(item);
            moveFromInactiveToActive(item);
        });

        btnRemove.setOnAction(event -> {
            String item = listViewBudget.getSelectionModel().getSelectedItem();
            deactivateInDB(item);
            moveFromActiveToInactive(item);
        });
    }

    private void deactivateInDB(String name) {
        for (Budget b : this.budgetList.getAllBudgets()) {
            if (b.getName().equals(name)) {
                if (!b.isGeneric()) {
                    this.budgetList.getAllBudgets().remove(b);
                    this.budgetList.getList().remove(b);
                }
                b.getBehavior().delete();
            }
        }

    }

    private void activateInDB(String name) {
        for (Budget b : this.budgetList.getAllBudgets()) {
            if (b.getName().equals(name)) {
                b.getBehavior().addToDB();
                budgetList.getList().add(b);
            }
        }
    }



    private void moveFromActiveToInactive(String name) {
        listViewBudget.getItems().remove(name);
        listViewInactiveBudget.getItems().add(name);
    }

    private void moveFromInactiveToActive(String name) {
        listViewInactiveBudget.getItems().remove(name);
        listViewBudget.getItems().add(name);
    }

    public void setExitTextForNewUser() {
        btnExitWindow.setText("Next");
    }

    @FXML
    protected void addCustomBudget() throws SQLException {
        String newCat = txtCustomBudgetCategory.getText();
        Budget b = new Budget(newCat);
        b.getBehavior().addToDB();
        moveFromInactiveToActive(b.getName());
        budgetList.getList().add(b);
        budgetList.getAllBudgets().add(b);
        txtCustomBudgetCategory.setText("");
    }

    @FXML
    protected void exitWindow() {
        if (newUser) openMaineWindow();
        closeStage();
    }

    private void openMaineWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().
                getResource("fxml/main/mainWindow.fxml"));
        try {
            AnchorPane primaryStage = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Budget");
            Scene scene = new Scene(primaryStage);
            stage.setScene(scene);
            MainWindowController mainWindow = loader.getController();
            mainWindow.setUserName(new UsernameData().getSessionUsername());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        Stage stage = (Stage)primaryPane.getScene().getWindow();
        stage.close();
    }

}
