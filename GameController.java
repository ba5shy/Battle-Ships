import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameController {

    final int ship_length = 3;
    final int grid_size = 10;
    private HashSet<String> occupied_squares = new HashSet<>();

    public String user_chosen_cell = null;
    public String username = null;
    public String opponent_username = null;
    public boolean is_host;
    public int score = 0;
    public int opponent_score = 0;
    public boolean my_turn = false;


    public void switchToGameScene() {
        // make initial game state

        Platform.runLater(() -> {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/Game/GameScene.fxml"));
                loader.setResources(bundle);

                Parent root = loader.load();
                Scene scene = new Scene(root);

                // adjust game information label
                adjustGameInformationLabel(root);

                // place player ships
                placePlayerShips(root);

                GridPane opponent_grid_pane = (GridPane) root.lookup("#opponent_grid_pane");
                opponent_grid_pane.setOnMouseClicked(event -> handleGridClick(event, opponent_grid_pane));

                Stage stage = Main.getStage();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void adjustGameInformationLabel(Parent root){
        Label game_information_label = (Label) root.lookup("#game_information_label");
        if(is_host)
            game_information_label.setText(username + " " + score + " - " + opponent_score + " " + opponent_username + ", Opponent's turn");
        else
            game_information_label.setText(username + " " + score + " - " + opponent_score + " " + opponent_username + ", Your turn");
        

    }

    public void handleGridClick(MouseEvent event, GridPane opponent_grid_pane) {

        if(!my_turn)
            return;

        int col = getColumnIndex(event, opponent_grid_pane);
        int row = getRowIndex(event, opponent_grid_pane);

        if (isValidCell(row, col)) {
            this.user_chosen_cell = "(" + row + "," + col + ")";
        }

    }

    private int getColumnIndex(MouseEvent event, GridPane opponent_grid_pane) {
        return (int) (event.getX()
                / (opponent_grid_pane.getWidth() / opponent_grid_pane.getColumnConstraints().size()));
    }

    private int getRowIndex(MouseEvent event, GridPane opponent_grid_pane) {
        return (int) (event.getY() / (opponent_grid_pane.getHeight() / opponent_grid_pane.getRowConstraints().size()));
    }

    private boolean isValidCell(int row, int col) {
        // Add additional logic if needed
        return row >= 0 && col >= 0;
    }

    private void colorCell(GridPane gridPane, int row, int col, String color, boolean destroyed) {
        // Get the node in the cell, or create a new one
        Node cell = getNodeFromGridPane(gridPane, row, col);
        if(!destroyed){
            if (cell == null) {
                Pane newCell = new Pane();
                newCell.setStyle("-fx-background-color: " + color + ";");
                gridPane.add(newCell, col, row);
            } else {
                cell.setStyle("-fx-background-color: " + color + ";");
            }
        } else{
            // if destroyed cell
        }

    }

    private Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            if (nodeRow == null)
                nodeRow = 0;
            if (nodeCol == null)
                nodeCol = 0;

            if (nodeRow == row && nodeCol == col) {
                return node;
            }
        }
        return null;
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
