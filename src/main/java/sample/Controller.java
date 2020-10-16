package sample;


import dictionary.DictionaryManagement;
import dictionary.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    /**
     * Biến kiểu DictionaryManagement để trỏ đến đối tượng chứa dữ liệu.
     */
    DictionaryManagement dictionaryManagement;

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> listView;

    /**
     * constructor khởi tạo ra đối tượng DictionaryManagement và đọc file txt chứa dữ liệu từ điển.
     *
     * @throws IOException để đẩy ngoại lệ ra ngoài.
     */
    public Controller() throws IOException {
        dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
    }

    /**
     * bắt sự kiện khi bấm "Search".
     *
     * @param actionEvent ...
     * @throws IOException để đẩy ngoại lệ ra ngoài.
     */
    public void searchEvent(ActionEvent actionEvent) throws IOException {
        String text = textField.getText().trim();
        String str = dictionaryManagement.dictionarySearch(text);
        if (str != null) {
            textArea.setText(text + " " + str);
        } else {
            String result = dictionaryManagement.dictionarySearchNearly(text);
            textArea.setText(result);
        }
    }

    /**
     * bắt sự kiện khi người dùng bấm "Search" sẽ trả về nghĩa hoặc từ gần đúng.
     *
     * @param mouseEvent ...
     * @throws IOException để đẩy ngoại lệ ra ngoài.
     */
    public void clickEvent(MouseEvent mouseEvent) throws IOException {
        String act = listView.getSelectionModel().getSelectedItem();
        if (act != null) {
            textField.setText(act);
            suggestEvent();
            textArea.setText(act + " " + dictionaryManagement.dictionarySearch(act));
        }
    }

    /**
     * bắt sự kiện khi người dùng nhập vào sẽ hiện gợi ý và có thể bấm vào gợi ý để xem nghĩa.
     *
     * @throws IOException
     */
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

    /**
     * bắt sự kiện khi bấm "Delete" sẽ xóa từ nhập vào.
     *
     * @param actionEvent ...
     */
    public void deleteEvent(ActionEvent actionEvent) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete");
        dialog.setHeaderText("Nhập từ bạn muốn xóa !");

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField word = new TextField();
        word.setPromptText("Nhập từ muốn xóa ...");
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
                    alert.setHeaderText("Bạn có chắc chắn muốn xóa '" + word.getText().trim() + "' ?");
                    ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType buttonCancle = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonYes, buttonCancle);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonYes) {
                        return new String(word.getText().trim());
                    }
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setHeaderText("Từ này không có !! \uD83D\uDE42");
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

    /**
     * bắt sự kiện khi người dùng bấm "Add" sẽ hiện bảng để nhập từ và nghĩa.
     *
     * @param actionEvent ...
     */
    public void addEvent(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add");
        dialog.setHeaderText("Nhập từ và nghĩa của từ bạn muốn thêm !");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField target = new TextField();
        target.setPromptText("Nhập từ muốn thêm ...");
        grid.add(target, 0, 0);
        TextArea explain = new TextArea();
        explain.setPromptText("Nhập ngĩa ...");
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
                    alert.setHeaderText("Từ nãy đã có !!\nKhông thể thêm mới \uD83D\uDE42 \uD83D\uDE42");
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

    /**
     * bắt sự kiện khi người dùng bấm "Change" sẽ hiện phiên âm và nghĩa hiện tại và cho phép thay đổi
     *
     * @param actionEvent ...
     * @throws IOException để đẩy ngoại lệ ra ngoài.
     */
    public void changeEvent(ActionEvent actionEvent) throws IOException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change");
        dialog.setHeaderText("Mời thay đổi phiên âm và nghĩa của '" + textField.getText().trim() + "' !");

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

    /**
     * bắt sự kiện khi người dùng bấm "Speak" thì sẽ phát âm từ tiếng anh nhập vào.
     *
     * @param actionEvent ...
     */
    public void speakEvent(ActionEvent actionEvent) {
        dictionaryManagement.dictionarySpeak(textField.getText().trim());
    }

    /**
     * bắt sự kiên khi người dùng bấm "About" thì hiện thông tin tác giả.
     *
     * @param actionEvent ...
     */
    public void aboutEvent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Project: Dictionary\n" +
                "Author: Phạm Ngọc Tú - 18021353\n" +
                "        Lê Chí Thọ - 18021236");
        alert.getButtonTypes().addAll(ButtonType.OK);
        alert.show();
    }

    /**
     * bắt sự kiện khi bấm "Save" thì dưu liệu mới vào file txt.
     *
     * @param actionEvent ...
     * @throws IOException để đẩy ngoại lệ ra ngoài.
     */
    public void saveEvent(ActionEvent actionEvent) throws IOException {
        dictionaryManagement.dictionarySave();
    }
}
