package sample;


import dictionary.DictionaryManagement;
import dictionary.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    DictionaryManagement dictionaryManagement;

    ObservableList list = FXCollections.observableArrayList();
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Controller() throws IOException {
        dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
    }

    public void searchEvent(ActionEvent actionEvent) throws IOException {
        String text = textField.getText();
        String str = dictionaryManagement.dictionarySearch(textField.getText());
        textArea.setText(str);
    }

    public void clickEvent(MouseEvent mouseEvent) throws IOException {
        String act = listView.getSelectionModel().getSelectedItem();
        if (act.isEmpty() || act == null) {
            textField.setText("Nothing");
        } else {
            textField.setText(act);
            suggestEvent();
        }
    }


    public void suggestEvent() throws IOException {
        listView.getItems().clear();
        list.removeAll(list);
        if (textField.getText().trim().isEmpty()) {

        } else {
            ArrayList arrayList = dictionaryManagement.suggest(textField.getText().trim());
            list.addAll(arrayList);
            listView.getItems().addAll(list);
        }

    }

    public void deleteEvent(ActionEvent actionEvent) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete");
        dialog.setHeaderText("Enter the word you want to delete");

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField word = new TextField();

        grid.add(word, 0, 0);


        Node loginButton = dialog.getDialogPane().lookupButton(deleteButton);
        loginButton.setDisable(true);

        word.textProperty().addListener((observableValue, s, t1) -> {
            loginButton.setDisable(t1.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButton) {
                String str = word.getText().trim();
                String re = dictionaryManagement.dictionarySearch(str);
                if (re != null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Confirm");
                    alert.setHeaderText("Are you sure want to delete ?");
                    ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType buttonCancle = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonYes, buttonCancle);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonYes) {
                        return new String(word.getText());
                    }
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setHeaderText("Does not exist !!");
                    alert2.showAndWait();
                }
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(userPass -> {
            dictionaryManagement.dictionaryDelete(result.get());
        });
    }

    public void addEvent(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add");
        dialog.setHeaderText("Enter the word you want to add");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField target = new TextField();
        grid.add(target, 0, 0);
        TextArea explain = new TextArea();
        grid.add(explain, 0, 1);


        Node loginButton = dialog.getDialogPane().lookupButton(addButton);
        loginButton.setDisable(true);

        target.textProperty().addListener((observableValue, s, t1) -> {
            loginButton.setDisable(t1.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(target.getText(), explain.getText());
            }

            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(userPass -> {
            dictionaryManagement.dictionaryAdd(new Word(userPass.getKey(), userPass.getValue()));
        });
    }
}
