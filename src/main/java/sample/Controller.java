package sample;


import dictionary.DictionaryManagement;
import dictionary.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller {
    DictionaryManagement dictionaryManagement;

    ObservableList list = FXCollections.observableArrayList();
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> listView;

    public Controller() throws IOException {
        dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
    }

    public void searchEvent(ActionEvent actionEvent) throws IOException {
        String text = textField.getText().trim();
        String str = dictionaryManagement.dictionarySearch(text);
        String result = "";
        if (str != null) {
            textArea.setText(text + " " + str);
        } else {
            for (int i = 0; i < dictionaryManagement.getDictionary().getLists().length; i++) {
                for (int j = 0 ; j < dictionaryManagement.getDictionary().getLists()[i].size(); j++) {
                    if (nearEqual(((Word)dictionaryManagement.getDictionary().getLists()[i].get(j)).getTarget(), text)) {
                        result += ((Word)dictionaryManagement.getDictionary().getLists()[i].get(j)).getTarget();
                        result += "\n";
                    }
                }
            }
            textArea.setText(result);
        }

    }

    public static boolean nearEqual(String s1, String s2) {
        int errorAllow = (int) Math.round(s2.length() * 0.3);
        if (s1.length() < (s2.length() - errorAllow) || s1.length() > (s2.length() + errorAllow)) {
            return false;
        }
        int indexS2 = 0;
        int indexS1 = 0;
        int error = 0;
        while (indexS2 < s2.length() && indexS1 < s1.length()) {
            if (s2.charAt(indexS2) != s1.charAt(indexS1)) {
                error++;
                for (int k = 1; k <= errorAllow; k++) {
                    if ((indexS2 + k < s2.length()) && s2.charAt(indexS2 + k) == s1.charAt(indexS1)) {
                        indexS2 += k;
                        error += k - 1;
                        break;
                    } else if ((indexS1 + k < s1.length()) && s2.charAt(indexS2) == s1.charAt(indexS1 + k)) {
                        indexS1 += k;
                        error += k - 1;
                        break;
                    }
                }
            }
            indexS1++;
            indexS2++;
        }
        error += s2.length() - indexS2 + s1.length() - indexS1;
        if (error <= errorAllow) {
            return true;
        } else {
            return false;
        }
    }

    public void clickEvent(MouseEvent mouseEvent) throws IOException {
        String act = listView.getSelectionModel().getSelectedItem();
        if (act.isEmpty() || act == null) {
            System.out.println("a");
        } else {
            textField.setText(act);
            suggestEvent();
            textArea.setText(act + " " + dictionaryManagement.dictionarySearch(act));
        }
    }

    public void suggestEvent() throws IOException {
        listView.getItems().clear();
        list.removeAll(list);
        if (textField.getText().trim().isEmpty()) {

        } else {
            ArrayList arrayList = dictionaryManagement.dictionarySuggest(textField.getText().trim());
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
                        return new String(word.getText().trim());
                    }
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setHeaderText("Does not exist !! \uD83D\uDE42");
                    alert2.showAndWait();
                }
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(userPass -> {
            dictionaryManagement.dictionaryDelete(result.get().trim());
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
                String str = dictionaryManagement.dictionarySearch(target.getText().trim());
                if (str != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("This word exists !!\nCannot add new \uD83D\uDE42 \uD83D\uDE42");
                    alert.show();
                } else {
                    return new Pair<>(target.getText().trim(), explain.getText());
                }
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(userPass -> {
            dictionaryManagement.dictionaryAdd(new Word(userPass.getKey().trim(), userPass.getValue()));
        });
    }

    public void changeEvent(ActionEvent actionEvent) throws IOException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change");

        ButtonType changeButton = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea word = new TextArea();
        word.setText(dictionaryManagement.dictionarySearch(textField.getText().trim()));
        grid.add(word, 0, 0);


        Node loginButton = dialog.getDialogPane().lookupButton(changeButton);
        loginButton.setDisable(true);

        word.textProperty().addListener((observableValue, s, t1) -> {
            loginButton.setDisable(t1.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButton) {
                return new String(word.getText());
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(userPass -> {
            dictionaryManagement.dictionaryChange(textField.getText().trim(), result.get());
        });
    }

    public void speakEvent(ActionEvent actionEvent) {
        dictionaryManagement.dictionarySpeak(textField.getText().trim());
    }

    public void aboutEvent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Project: Dictionary\n" +
                             "Author: Phạm Ngọc Tú - 18021353\n" +
                             "        Lê Chí Thọ");
        alert.getButtonTypes().addAll(ButtonType.OK);
        alert.show();
    }

    public void saveEvent(ActionEvent actionEvent) throws IOException {
        dictionaryManagement.dictionarySave();
    }
}
