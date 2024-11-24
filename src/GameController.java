import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {

    final int ship_length = 3;
    final int grid_size = 10;
    private HashSet<String> occupied_squares = new HashSet<>();

    public void switchToGameScene() {
        // make initial game state

        Platform.runLater(() -> {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/Game/GameScene.fxml"));
                loader.setResources(bundle);

                Parent root = loader.load();
                Scene scene = new Scene(root);

                placePlayerShips(root);

                Stage stage = Main.getStage();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateGameState() {
        // this method should take in the coordinates that the opponent
        // chose
    }

    public void placePlayerShips(Parent root) {
        // this method will randomly place 5 ships for players
        // each player will have their own ship-placement

        int ships_to_place = 5;
        Random random = new Random();

        while (ships_to_place > 0) {
            String orientation = (random.nextBoolean()) ? "horizontal" : "vertical"; // random orientation
            int start_row = random.nextInt(grid_size); // 10 not included
            int start_column = random.nextInt(grid_size);
            if (canPlaceShip(start_row, start_column, orientation)) {

                GridPane player_grid_pane = (GridPane) root.lookup("#player_grid_pane"); 

                placeShip(player_grid_pane, start_row, start_column, orientation);

                addOccupiedSpaces(start_row, start_column, orientation);
                ships_to_place -= 1;
            }

        }

    }

    private void placeShip(GridPane player_grid_pane, int start_row, int start_column, String orientation) {
        if (orientation.equals("vertical")) {
            placeShipImage(player_grid_pane, "images/ship_images/vertical/bottom.jpg", start_row, start_column,
                    orientation, "top", "center");
            placeShipImage(player_grid_pane, "images/ship_images/vertical/middle.jpg", start_row - 1, start_column,
                    orientation, "center", "center");
            placeShipImage(player_grid_pane, "images/ship_images/vertical/top.jpg", start_row - 2, start_column,
                    orientation, "bottom", "center");
        } else {
            // horizontal
            placeShipImage(player_grid_pane, "images/ship_images/vertical/bottom.jpg", start_row, start_column,
                    orientation, "center", "right");
            placeShipImage(player_grid_pane, "images/ship_images/vertical/middle.jpg", start_row, start_column + 1,
                    orientation, "center", "center");
            placeShipImage(player_grid_pane, "images/ship_images/vertical/top.jpg", start_row, start_column + 2,
                    orientation, "center", "left");
        }
    }

    private void addOccupiedSpaces(int start_row, int start_column, String orientation) {
        if (orientation.equals("vertical")) {
            for (int i = 0; i < ship_length; i++) {
                int row = start_row - i;
                String coordinate = row + ", " + start_column;
                occupied_squares.add(coordinate);
            }
        } else {
            for (int i = 0; i < ship_length; i++) {
                int column = start_column + i;
                String coordinate = start_row + ", " + column;
                occupied_squares.add(coordinate);
            }
        }
    }

    private boolean canPlaceShip(int start_row, int start_column, String orientation) {

        int row = start_row; // assuming more than 0
        int column = start_column; // assuming more than 0

        for (int i = 0; i < ship_length; i++) {
            // start from ship bottom

            if (column >= grid_size || row >= grid_size)
                return false;

            if (row < 0 || column < 0)
                return false;

            if (occupied_squares.contains(row + ", " + column))
                return false;

            if (orientation.equals("vertical"))
                row -= 1; // to go up
            else
                column += 1; // to go right

        }

        return true;
    }

    private void placeShipImage(GridPane player_grid_pane, String path_to_image, int row, int column,
            String orientation, String v_align, String h_align) {
        Image image = new Image(path_to_image);
        ImageView image_view = new ImageView(image);

        image_view.setFitHeight(48);
        image_view.setFitWidth(48);
        if (orientation.equals("horizontal")) {
            image_view.setRotate(90);
            GridPane.setValignment(image_view, VPos.CENTER);
            GridPane.setHalignment(image_view, HPos.CENTER);
        } else {

        }

        player_grid_pane.add(image_view, column, row); // add(image_view, COLUMN, ROW) so stupid
    }
}
