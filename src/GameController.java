import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    public Parent root;
    public GridPane grid_pane;
    public GridPane opponent_grid_pane;
    public Scene scene;

    public void switchToGameScene() {
        // make initial game state

        Platform.runLater(() -> {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/Game/GameScene.fxml"));
                loader.setResources(bundle);

                Parent root = loader.load();
                this.root = root;

                Scene scene = new Scene(root);
                this.scene = scene;

                GridPane opponent_grid_pane = (GridPane) root.lookup("#opponent_grid_pane");
                GridPane grid_pane = (GridPane) root.lookup("#grid_pane");

                this.grid_pane = grid_pane;
                this.opponent_grid_pane = opponent_grid_pane;

                opponent_grid_pane.setOnMouseClicked(event -> handleGridClick(event, opponent_grid_pane));

                adjustGameInformationLabel();

                // place player ships
                placePlayerShips();

                Stage stage = Main.getStage();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void endGame(boolean won){
        Platform.runLater(() -> {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/Game/GameEnd.fxml"));
                loader.setResources(bundle);

                Parent root = loader.load();
                this.root = root;

                Scene scene = new Scene(root);
                this.scene = scene;

                Stage stage = new Stage();

                Label label = (Label) root.lookup("#game_end_label");

                if(won){
                    String message = bundle.getString("you_won");
                    label.setText(message);
                } else{
                    String message = bundle.getString("you_lost");
                    label.setText(message);
                }

                Button close_button = (Button) root.lookup("#close_button");

                close_button.setOnAction(e -> {
                    stage.close();
                    MainController mc = new MainController();
                    mc.switchToMainMenu(null);
                });

                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void adjustGameInformationLabel() { // used when receiving coordinates
        Label game_information_label = (Label) this.root.lookup("#game_information_label");
        if (my_turn) {
            game_information_label
                    .setText(username + " " + score + " - " + opponent_score + " " + opponent_username + ", Your turn");
        } else {
            game_information_label.setText(
                    username + " " + score + " - " + opponent_score + " " + opponent_username + ", Opponent's Turn");
        }

    }

    public void adjustGameInformationLabelPlayerSide() { // used after sending coordinates
        Platform.runLater(() -> {
            adjustGameInformationLabel();
        });

    }

    public void handleGridClick(MouseEvent event, GridPane opponent_grid_pane) {

        if (!my_turn)
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

    public boolean processHitCoordinates(String coordinates_and_orientation) {
        // coordinates -> (x1,y1)(x2,y2)(x3,y3)&orientation
        String coordinates = coordinates_and_orientation.split("&")[0];
        String orientation = coordinates_and_orientation.split("&")[1];

        // coor = (x,y)
        String top_coordinate = coordinates.substring(0, 5);
        String middle_coordinate = coordinates.substring(5, 10);
        String bottom_coordinate = coordinates.substring(10, 15);

        boolean game_end;
        if(score == 4)
            game_end = true;
        else
            game_end = false;
        if (orientation.equals("vertical")) {
            String top_path = "images/ship_images/vertical/destroyed/top.jpg";
            String middle_path = "images/ship_images/vertical/destroyed/middle.jpg";
            String bottom_path = "images/ship_images/vertical/destroyed/bottom.jpg";

            Stage stage = Main.getStage();

            Platform.runLater(() -> {

                placeShipImage(opponent_grid_pane, top_path, Character.getNumericValue(top_coordinate.charAt(1)),
                        Character.getNumericValue(top_coordinate.charAt(3)), orientation);
                placeShipImage(opponent_grid_pane, middle_path, Character.getNumericValue(middle_coordinate.charAt(1)),
                        Character.getNumericValue(middle_coordinate.charAt(3)), orientation);
                placeShipImage(opponent_grid_pane, bottom_path, Character.getNumericValue(bottom_coordinate.charAt(1)),
                        Character.getNumericValue(bottom_coordinate.charAt(3)), orientation);

                score += 1;
                this.my_turn = false;
                adjustGameInformationLabel();

                stage.setScene(scene);
                stage.show();
            });

        } else {
            String top_path = "images/ship_images/horizontal/destroyed/top.jpg";
            String middle_path = "images/ship_images/horizontal/destroyed/middle.jpg";
            String bottom_path = "images/ship_images/horizontal/destroyed/bottom.jpg";

            Stage stage = Main.getStage();

            Platform.runLater(() -> {

                placeShipImage(opponent_grid_pane, top_path, Character.getNumericValue(top_coordinate.charAt(1)),
                        Character.getNumericValue(top_coordinate.charAt(3)), orientation);
                placeShipImage(opponent_grid_pane, middle_path, Character.getNumericValue(middle_coordinate.charAt(1)),
                        Character.getNumericValue(middle_coordinate.charAt(3)), orientation);
                placeShipImage(opponent_grid_pane, bottom_path, Character.getNumericValue(bottom_coordinate.charAt(1)),
                        Character.getNumericValue(bottom_coordinate.charAt(3)), orientation);

                this.my_turn = false;
                score += 1;
                adjustGameInformationLabel();

                stage.setScene(scene);
                stage.show();
            });

        }

        return game_end;

    }

    public void processMissCoordinates(String coordinates) {
        int row = Character.getNumericValue(coordinates.charAt(1));
        int column = Character.getNumericValue(coordinates.charAt(3));

        // change color of coordinate to red
        Rectangle node = new Rectangle();

        node.setFill(Color.LIGHTCORAL);
        node.setWidth(48);
        node.setHeight(48);

        Stage stage = Main.getStage();

        Platform.runLater(() -> {
            GridPane.setRowIndex(node, row);
            GridPane.setColumnIndex(node, column);
            opponent_grid_pane.getChildren().add(node);

            this.my_turn = false;
            adjustGameInformationLabel();

            stage.setScene(scene);
            stage.show();
        });
    }

    public String processDestroyCoordinatesFromOpponent(String coordinates) {
        // process coordinates received from opponent (destroy on player side not
        // opponent)
        int row = Character.getNumericValue(coordinates.charAt(1));
        int column = Character.getNumericValue(coordinates.charAt(3));

        if (shipHit(coordinates)) {
            // if opponent hits a part of the ship
            // remove entire ship (get ship coordinates)

            ImageView image_view = (ImageView) getNodeFromGridPane(this.grid_pane, row, column);

            String message = destroyShips(image_view, row, column);

            return message;
        } else {
            // change color of coordinate to red
            Rectangle node = new Rectangle();

            node.setFill(Color.LIGHTCORAL);
            node.setWidth(48);
            node.setHeight(48);

            Stage stage = Main.getStage();

            Platform.runLater(() -> {
                GridPane.setRowIndex(node, row);
                GridPane.setColumnIndex(node, column);
                grid_pane.getChildren().add(node);

                this.my_turn = true;
                adjustGameInformationLabel();

                stage.setScene(scene);
                stage.show();
            });
            return "miss:" + "(" + row + "," + column + ")";
        }
    }

    private String destroyShips(ImageView image_view, int row, int column) {
        // receive one image of ship and destroy remaining ships
        // basically replace images of ship with destroyed image ship

        String image_path = getRelativePath(image_view.getImage());
        String coordinates = "hit:";

        if (image_path.equals("bin/images/ship_images/vertical/top.jpg")) {
            // vertical top, destroy two below

            image_view.setImage(new Image("images/ship_images/vertical/destroyed/top.jpg")); // set vertical top

            // set vertical middle
            ImageView vertical_middle = (ImageView) getNodeFromGridPane(grid_pane, row + 1, column);
            vertical_middle.setImage(new Image("images/ship_images/vertical/destroyed/middle.jpg"));

            // set vertical bottom
            ImageView vertical_bottom = (ImageView) getNodeFromGridPane(grid_pane, row + 2, column);
            vertical_bottom.setImage(new Image("images/ship_images/vertical/destroyed/bottom.jpg"));

            coordinates += "(" + row + "," + column + ")" + "(" + (row + 1) + "," + column + ")" + "(" + (row + 2) + ","
                    + column + ")&vertical";

        } else if (image_path.equals("bin/images/ship_images/vertical/middle.jpg")) {
            // vertical middle, destroy above and below

            image_view.setImage(new Image("images/ship_images/vertical/destroyed/middle.jpg")); // set vertical middle

            // set vertical top
            ImageView vertical_top = (ImageView) getNodeFromGridPane(grid_pane, row - 1, column);
            vertical_top.setImage(new Image("images/ship_images/vertical/destroyed/top.jpg"));

            // set vertical bottom
            ImageView vertical_bottom = (ImageView) getNodeFromGridPane(grid_pane, row + 1, column);
            vertical_bottom.setImage(new Image("images/ship_images/vertical/destroyed/bottom.jpg"));

            coordinates += "(" + (row - 1) + "," + column + ")" + "(" + row + "," + column + ")" + "(" + (row + 1) + ","
                    + column + ")&vertical";

        } else if (image_path.equals("bin/images/ship_images/vertical/bottom.jpg")) {
            // vertical bottom, destroy two above

            image_view.setImage(new Image("images/ship_images/vertical/destroyed/bottom.jpg")); // set vertical bottom

            // set vertical top
            ImageView vertical_top = (ImageView) getNodeFromGridPane(grid_pane, row - 2, column);
            vertical_top.setImage(new Image("images/ship_images/vertical/destroyed/top.jpg"));

            // set vertical middle
            ImageView vertical_middle = (ImageView) getNodeFromGridPane(grid_pane, row - 1, column);
            vertical_middle.setImage(new Image("images/ship_images/vertical/destroyed/middle.jpg"));

            coordinates += "(" + (row - 2) + "," + column + ")" + "(" + (row - 1) + "," + column + ")" + "(" + row + ","
                    + column + ")&vertical";

        } else if (image_path.equals("bin/images/ship_images/horizontal/top.jpg")) {
            // horizontal top, destroy two to the left

            image_view.setImage(new Image("images/ship_images/horizontal/destroyed/top.jpg")); // set horizontal top

            // set horizontal middle
            ImageView horizontal_middle = (ImageView) getNodeFromGridPane(grid_pane, row, column - 1);
            horizontal_middle.setImage(new Image("images/ship_images/horizontal/destroyed/middle.jpg"));

            // set horizontal bottom
            ImageView horizontal_bottom = (ImageView) getNodeFromGridPane(grid_pane, row, column - 2);
            horizontal_bottom.setImage(new Image("images/ship_images/horizontal/destroyed/bottom.jpg"));

            coordinates += "(" + row + "," + column + ")" + "(" + row + "," + (column - 1) + ")" + "(" + row + ","
                    + (column - 2) + ")&horizontal";

        } else if (image_path.equals("bin/images/ship_images/horizontal/middle.jpg")) {
            // horizontal middle, destroy right (top) and left (bottom)

            image_view.setImage(new Image("images/ship_images/horizontal/destroyed/middle.jpg")); // set horizontal
                                                                                                  // middle

            // set horizontal top
            ImageView horizontal_top = (ImageView) getNodeFromGridPane(grid_pane, row, column + 1);
            horizontal_top.setImage(new Image("images/ship_images/horizontal/destroyed/top.jpg"));

            // set horizontal bottom
            ImageView horizontal_bottom = (ImageView) getNodeFromGridPane(grid_pane, row, column - 1);
            horizontal_bottom.setImage(new Image("images/ship_images/horizontal/destroyed/bottom.jpg"));

            coordinates += "(" + row + "," + (column + 1) + ")" + "(" + row + "," + column + ")" + "(" + row + ","
                    + (column - 1) + ")&horizontal";

        } else if (image_path.equals("bin/images/ship_images/horizontal/bottom.jpg")) {
            // horizontal bottom, destroy two to the right (middle and top)

            image_view.setImage(new Image("images/ship_images/horizontal/destroyed/bottom.jpg")); // set horizontal
                                                                                                  // bottom

            // set horizontal middle
            ImageView horizontal_middle = (ImageView) getNodeFromGridPane(grid_pane, row, column + 1);
            horizontal_middle.setImage(new Image("images/ship_images/horizontal/destroyed/middle.jpg"));

            // set horizontal top
            ImageView horizontal_top = (ImageView) getNodeFromGridPane(grid_pane, row, column + 2);
            horizontal_top.setImage(new Image("images/ship_images/horizontal/destroyed/top.jpg"));

            coordinates += "(" + row + "," + (column + 2) + ")" + "(" + row + "," + (column + 1) + ")" + "(" + row + ","
                    + column + ")&horizontal";

        }

        // return the coordinates for the opponent to reveal on their screen
        Stage stage = Main.getStage();
        this.my_turn = true;

       

        Platform.runLater(() -> {
            opponent_score += 1;
            adjustGameInformationLabel();
            stage.setScene(scene);
            stage.show();
        });
        return coordinates;
    }

    public String getRelativePath(Image image) {
        // Get the full path from the image URL
        String fullPath = image.getUrl(); // Get the full path including "file:" prefix

        // Remove the "file:" prefix if present
        String fullPathWithoutPrefix = fullPath.replace("file:", "");

        // Define the starting directory for the relative path
        String startDirectory = "bin";

        // Find the index of the specified start directory
        int index = fullPathWithoutPrefix.indexOf(startDirectory);

        // Extract the relative path starting from the desired directory
        if (index != -1) {
            return fullPathWithoutPrefix.substring(index);
        } else {
            // Return the full path if the specified directory is not found
            return fullPathWithoutPrefix;
        }
    }

    boolean shipHit(String coordinates) {
        // if coordinate hits a ship, return true
        // coordinates -> (x,y)

        if (occupied_squares.contains(coordinates))
            return true;
        else
            return false;
    }

    public void placePlayerShips() {
        // this method will randomly place 5 ships for players
        // each player will have their own ship-placement

        int ships_to_place = 5;
        Random random = new Random();

        while (ships_to_place > 0) {
            String orientation = (random.nextBoolean()) ? "horizontal" : "vertical"; // random orientation
            int start_row = random.nextInt(grid_size); // 10 not included
            int start_column = random.nextInt(grid_size);
            if (canPlaceShip(start_row, start_column, orientation)) {

                GridPane player_grid_pane = this.grid_pane;

                placeShip(player_grid_pane, start_row, start_column, orientation);

                addOccupiedSpaces(start_row, start_column, orientation);
                ships_to_place -= 1;
            }

        }

    }

    private void placeShip(GridPane player_grid_pane, int start_row, int start_column, String orientation) {
        if (orientation.equals("vertical")) {
            placeShipImage(player_grid_pane, "images/ship_images/vertical/bottom.jpg", start_row, start_column,
                    orientation);
            placeShipImage(player_grid_pane, "images/ship_images/vertical/middle.jpg", start_row - 1, start_column,
                    orientation);
            placeShipImage(player_grid_pane, "images/ship_images/vertical/top.jpg", start_row - 2, start_column,
                    orientation);
        } else {
            // horizontal
            placeShipImage(player_grid_pane, "images/ship_images/horizontal/bottom.jpg", start_row, start_column,
                    orientation);
            placeShipImage(player_grid_pane, "images/ship_images/horizontal/middle.jpg", start_row, start_column + 1,
                    orientation);
            placeShipImage(player_grid_pane, "images/ship_images/horizontal/top.jpg", start_row, start_column + 2,
                    orientation);
        }
    }

    private void addOccupiedSpaces(int start_row, int start_column, String orientation) {
        if (orientation.equals("vertical")) {
            for (int i = 0; i < ship_length; i++) {
                int row = start_row - i;
                String coordinate = "(" + row + "," + start_column + ")";
                occupied_squares.add(coordinate);
            }
        } else {
            for (int i = 0; i < ship_length; i++) {
                int column = start_column + i;
                String coordinate = "(" + start_row + "," + column + ")";
                occupied_squares.add(coordinate);
            }
        }
    }

    private boolean canPlaceShip(int start_row, int start_column, String orientation) {

        int row = start_row; // assuming more than 0
        int column = start_column; // assuming more than 0

        if(row == 0 && column == 0) // (0,0) is always an instance of the Group class which cannot be casted into an imageview
            return false;

        for (int i = 0; i < ship_length; i++) {
            // start from ship bottom

            if (column >= grid_size || row >= grid_size)
                return false;

            if (row < 0 || column < 0)
                return false;

            if (occupied_squares.contains("(" + row + "," + column + ")"))
                return false;

            if (orientation.equals("vertical"))
                row -= 1; // to go up
            else
                column += 1; // to go right

        }

        return true;
    }

    private void placeShipImage(GridPane player_grid_pane, String path_to_image, int row, int column,
            String orientation) {
        Image image = new Image(path_to_image);
        ImageView image_view = new ImageView(image);

        image_view.setFitHeight(48);
        image_view.setFitWidth(48);
        if (orientation.equals("horizontal")) {
            // image_view.setRotate(90);
            GridPane.setValignment(image_view, VPos.CENTER);
            GridPane.setHalignment(image_view, HPos.CENTER);
        }

        GridPane.setRowIndex(image_view, row);
        GridPane.setColumnIndex(image_view, column);
        player_grid_pane.getChildren().add(image_view);
    }
}
