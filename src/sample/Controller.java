package sample;


import dictionary.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Dictionary;

public class Controller {
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    public void eventSearch(ActionEvent actionEvent) throws IOException {
        String text = textField.getText();
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
        String str = dictionaryManagement.dictionarySearch(textField.getText());
        textArea.setText(str);
    }
}
