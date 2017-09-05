package com.moneymaker.utilities;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by Jay Damon on 8/2/2016.
 */
public class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {

    private ComboBox comboBox;
    private StringBuilder sb;
    private int lastLength;

    public AutoCompleteComboBoxListener(ComboBox comboBox) {
        this.comboBox = comboBox;
        sb = new StringBuilder();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);

        // add a focus listener such that if not in focus, reset the filtered typed keys
        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                lastLength = 0;
                sb.delete(0, sb.length());
                selectClosestResultBasedOnTextFieldValue(false);
            }
        });

        this.comboBox.setOnMouseClicked(event -> selectClosestResultBasedOnTextFieldValue(true));
    }

    @Override
    public void handle(KeyEvent event) {
        // this variable is used to bypass the auto complete process if the length is the same.
        // this occurs if user types fast, the length of textfield will record after the user
        // has typed after a certain delay.
        if (lastLength != (comboBox.getEditor().getLength() - comboBox.getEditor().getSelectedText().length()))
            lastLength = comboBox.getEditor().getLength() - comboBox.getEditor().getSelectedText().length();

        if (event.isControlDown() || event.getCode() == KeyCode.BACK_SPACE ||
                event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT ||
                event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.HOME ||
                event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB
                )
            return;

        IndexRange ir = comboBox.getEditor().getSelection();
        sb.delete(0, sb.length());
        sb.append(comboBox.getEditor().getText());
        // remove selected string index until end so only unselected text will be recorded
        try {
            sb.delete(ir.getStart(), sb.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList items = comboBox.getItems();
        for (Object item : items) {
            if (item.toString().toLowerCase().startsWith(comboBox.getEditor().getText().toLowerCase())
                    ) {
                try {
                    comboBox.getEditor().setText(sb.toString() + item.toString().substring(sb.toString().length()));
                } catch (Exception e) {
                    comboBox.getEditor().setText(sb.toString());
                }
                comboBox.getEditor().positionCaret(sb.toString().length());
                comboBox.getEditor().selectEnd();
                break;
            }
        }
    }

    /*
     * selectClosestResultBasedOnTextFieldValue() - selects the item and scrolls to it when
     * the updateitem is shown.
     *
     * parameters:
     *  affect - true if combobox is clicked to show updateitem so text and caret position will be readjusted.
     *  inFocus - true if combobox has focus. If not, programmatically press enter key to add new entry to list.
     *
     */
    private void selectClosestResultBasedOnTextFieldValue(boolean affect) {
        ObservableList items = AutoCompleteComboBoxListener.this.comboBox.getItems();
        boolean found = false;
        for (int i=0; i<items.size(); i++) {
            if (AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase().equals(items.get(i).toString().toLowerCase())) {
                try {
                    ListView lv = ((ComboBoxListViewSkin) AutoCompleteComboBoxListener.this.comboBox.getSkin()).getListView();
                    lv.getSelectionModel().clearAndSelect(i);
                    lv.scrollTo(lv.getSelectionModel().getSelectedIndex());
                    found = true;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String s = comboBox.getEditor().getText();
        if (!found && affect) {
            comboBox.getSelectionModel().clearSelection();
            comboBox.getEditor().setText(s);
            comboBox.getEditor().end();
        }

//        if (!inFocus && comboBox.getEditor().getText() != null && comboBox.getEditor().getText().trim().length() > 0) {
//            // press enter key programmatically to have this entry added
//            KeyEvent ke = KeyEvent.impl_keyEvent(comboBox, KeyCode.ENTER.toString(), KeyCode.ENTER.getName(), KeyCode.ENTER.impl_getCode(), false, false, false, false, KeyEvent.KEY_RELEASED);
//            comboBox.fireEvent(ke);
//        }
    }
}
