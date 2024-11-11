import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
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
                if(enter_username_label != null)
                    enter_username_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                
                Label enter_port_number_label = (Label) root.lookup("#enter_port_number_label");
                if(enter_port_number_label != null)
                    enter_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if(ip_address_label != null){
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }

            } else{
                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if(ip_address_label != null){
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
                if(enter_username_label != null)
                    enter_username_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                
                Label enter_port_number_label = (Label) root.lookup("#enter_port_number_label");
                if(enter_port_number_label != null)
                    enter_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                
                Label enter_ip_address_label = (Label) root.lookup("#enter_port_number_label");
                if(enter_ip_address_label != null)
                    enter_port_number_label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if(ip_address_label != null){
                    StringBuilder text = new StringBuilder(ip_address_label.getText());
                    text.append(" " + localAddress);
                    ip_address_label.setText(text.toString());
                }

            } else{
                String localAddress = InetAddress.getLocalHost().getHostAddress();
                Label ip_address_label = (Label) root.lookup("#ip_address_label");
                if(ip_address_label != null){
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

    public void switchToMainMenu() {
        MainController mc = new MainController();
        mc.switchToMainMenu();
    }

}
