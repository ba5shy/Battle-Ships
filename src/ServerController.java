import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;

public class ServerController {

    public void switchToCreateServer() {
        try {

            ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/HostServer/HostServer.fxml"));
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (Main.getLocale().getLanguage().equals("ar")) {
                // if the language is arabic
                Label enter_username_label = (Label) root.lookup("#enter_username_label");
                if (enter_username_label != null)
                    enter_username_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                Label enter_port_number_label = (Label) root.lookup("#enter_port_number_label");
                if (enter_port_number_label != null)
                    enter_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if (ip_address_label != null) {
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }

            } else {
                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if (ip_address_label != null) {
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }
            }

           
            if(Main.getHostUsername() != null){ // used when the players clicks "back" on the waiting screen
                TextField enter_username_field = (TextField) root.lookup("#enter_username_field");
                if(enter_username_field != null)
                    enter_username_field.setText(Main.getHostUsername());
            }
            if(Main.getServerPortNumber() != null){
                TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                if(enter_port_number_field != null)
                    enter_port_number_field.setText(Main.getServerPortNumber());
            }

            // set event listener for "host server button"

            Button start_server_button = (Button) root.lookup("#start_server_button");
            if (start_server_button != null){
                start_server_button.setOnAction(e->{

                    TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                    TextField enter_username_field = (TextField) root.lookup("#enter_username_field");

                    if(validateServerInformation(enter_port_number_field.getText(), enter_username_field.getText())){
                        // valid information - save information to Main class
                        System.out.println("HERE");
                        Main.setServerPortNumber(enter_port_number_field.getText());
                        Main.setHostUsername(enter_username_field.getText());

                        // move to next scene
                        switchToServerWait();
                    } else{
                        switchToCreateServer(); // malha da3y??
                    }
                });
            }
                    

            Stage stage = Main.getStage();
            stage.setScene(scene);

            String title = bundle.getString("title");
            stage.setTitle(title);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToJoinFriend() {
        try {

            ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/JoinFriend/JoinFriend.fxml"));
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (Main.getLocale().getLanguage().equals("ar")) {
                // if the language is arabic
                Label enter_username_label = (Label) root.lookup("#enter_username_label");
                if (enter_username_label != null)
                    enter_username_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                Label enter_port_number_label = (Label) root.lookup("#enter_port_number_label");
                if (enter_port_number_label != null)
                    enter_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                Label enter_ip_address_label = (Label) root.lookup("#enter_ip_address_label");
                if (enter_ip_address_label != null)
                    enter_ip_address_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if (ip_address_label != null) {
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }

            } else {
                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if (ip_address_label != null) {
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }
            }

            Stage stage = Main.getStage();
            stage.setScene(scene);

            String title = bundle.getString("title");
            stage.setTitle(title);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToServerWait() {

        try {

            ResourceBundle bundle = ResourceBundle.getBundle("resources.text", Main.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/HostServer/WaitForPlayer.fxml"));
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            // adjust ip address label
            String localAddress = InetAddress.getLocalHost().getHostAddress(); 
            Label server_ip_address_label = (Label) root.lookup("#server_ip_address_label");
            if (server_ip_address_label != null) {
                StringBuilder text = new StringBuilder(server_ip_address_label.getText());
                text.append(" " + localAddress);
                server_ip_address_label.setText(text.toString());
            }
            Label server_port_number_label = (Label) root.lookup("#server_port_number_label");
            if (server_port_number_label != null){
                StringBuilder text = new StringBuilder(server_port_number_label.getText());
                text.append(" " + Main.getServerPortNumber());
                server_port_number_label.setText(text.toString());
            }
            Label server_username_label = (Label) root.lookup("#server_username_label");
            if(server_username_label != null){
                StringBuilder text = new StringBuilder(server_username_label.getText());
                text.append(" " + Main.getHostUsername());
                server_username_label.setText(text.toString());
            }

            if (Main.getLocale().getLanguage().equals("ar")) {
                // if the language is arabic
                Label server_information_header_label = (Label) root.lookup("#server_information_header_label");
                if (server_information_header_label != null)
                    server_information_header_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                server_ip_address_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                server_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            } 

            Stage stage = Main.getStage();
            stage.setScene(scene);

            String title = bundle.getString("title");
            stage.setTitle(title);
            
            // before stage is shown, create a server thread

            Thread server_thread = createServerThread(Integer.parseInt(Main.getServerPortNumber()), server_username_label.getText());
            
            Main.setServerThread(server_thread);

            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateServerInformation(String port_number, String host_username){
        // check if port number is correct
        try{
            int port_number_integer = Integer.parseInt(port_number);

            if(port_number_integer >= 1024 && port_number_integer <= 49151){
                // valid port number
                // check username - no whitespaces
                if(host_username.contains(" ")){
                    // error scene 
                    return false;
                } else 
                    return true;
            } else{
                // error scene
                return false;
            }

        } catch(NumberFormatException e){
            // not a proper port number
            e.printStackTrace();
            // error scene
            return false;
        }
    }

    public Thread createServerThread(int port_number, String host_username){
        Thread server_thread = new Thread(
            new Server(port_number, host_username)
        );
        server_thread.start();
        return server_thread;
    }

    public void switchToMainMenu() {
        MainController mc = new MainController();
        mc.switchToMainMenu();
    }

    

}
