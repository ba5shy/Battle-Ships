import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameController {

    public static Node[][] fleet = new Node[10][10];

    public void switchToGameScene(){
        // make initial game state

        placePlayerShips();

        try{
            ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/Game/GameScene.fxml"));
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            placePlayerShips();

            Stage stage = Main.getStage();
            stage.setScene(scene);  
            stage.show();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void updateGameState(){
        // this method should take in the coordinates that the opponent
        // chose
    }

    public void placePlayerShips(){
        // this method will randomly place 5 ships for players
        // each player will have their own ship-placement
    }
}
