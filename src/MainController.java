import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

public class MainController {
    

    public void switchToMainMenu(String error_message_key) {
        try {

            ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/MainMenu/MainMenu.fxml"));
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = Main.getStage();
            stage.setScene(scene);

            // set error message if given 
            if(error_message_key != null){
                Label error_message_label = (Label) root.lookup("#error_message_label");
                String error_message = bundle.getString(error_message_key);
                                error_message_label.setText(error_message);
            }

            String title = bundle.getString("title");
            stage.setTitle(title);

            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeLanguage(){
        // changing language will only be available on the main menu

        if(Main.getLocale().getLanguage().equals("en")){
            // switch to arabic
            Main.setLocale(new Locale("ar"));
        } else{
            Main.setLocale(new Locale("en"));
        }
        switchToMainMenu(null);

    }

    public void switchToCreateServer(){
        ServerController sc = new ServerController();
        sc.switchToCreateServer();
    }

    public void switchToJoinFriend(){
        ServerController sc = new ServerController();
        sc.switchToJoinFriend();
    }

    

}
