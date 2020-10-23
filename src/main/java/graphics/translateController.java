package graphics;

import dictionary.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class translateController {

    @FXML
    public TextArea textTarget;
    @FXML
    public TextArea textExplain;

    /**
     * dịch từ anh sanh việt.
     *
     * @param actionEvent ...
     */
    public void enToVi(ActionEvent actionEvent) {
        String result = "";
        try {
            result
                    = DictionaryManagement.translate("en", "vi", textTarget.getText().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textExplain.setText(result);
    }

    /**
     * dịch từ việt sang anh.
     *
     * @param actionEvent ,,,
     */
    public void viToEn(ActionEvent actionEvent) {
        String result = "";
        try {
            result
                    = DictionaryManagement.translate("vi", "en", textTarget.getText().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textExplain.setText(result);
    }


    /**
     * đọc chuỗi nhập vào.
     *
     * @param actionEvent ...
     */
    public void speechTargetEvent(ActionEvent actionEvent) {
        DictionaryManagement.dictionarySpeak(textTarget.getText().trim());
    }

    /**
     * đọc chuỗi in ra.
     *
     * @param actionEvent ...
     */
    public void speechExplainEvent(ActionEvent actionEvent) {
        DictionaryManagement.dictionarySpeak(textExplain.getText().trim());
    }
}
