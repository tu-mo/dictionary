package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    /**
     * hiện giao diện đồ họa
     *
     * @param primaryStage dùng để cài đặt và hiển thị giao diện.
     * @throws Exception để đẩy ngoại lệ ra ngoài.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/graphicsDict.fxml"));
        primaryStage.setTitle("Dictionary");
        Image image = new Image("icon/logo.png");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * main.
     *
     * @param args nhận đối số dòng lệnh.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
