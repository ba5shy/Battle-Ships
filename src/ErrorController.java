import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {

    private static Stage error_stage = new Stage();

    public void errorScene(String error_message) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("scenes/Error/ErrorScene.fxml"));

            Scene scene = new Scene(root);

            Label error_message_label = (Label) root.lookup("#error_message_label");
            if (error_message_label != null) {
                error_message_label.setText(error_message);
            } else {
                error_message_label = new Label();
                error_message_label.setLayoutX(14.0);
                error_message_label.setLayoutY(97.0);
                error_message_label.setMnemonicParsing(false);
                error_message_label.setPrefWidth(500.0);
                error_message_label.setPrefHeight(27.0);
                error_message_label.setText(error_message);
            }

            error_stage.setScene(scene);
            error_stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dismissError() {
        error_stage.close();
    }

}