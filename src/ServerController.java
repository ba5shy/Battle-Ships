import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;

public class ServerController {

    public void switchToCreateServer() {

        if (Main.getServer() != null) {
            try {
                Main.getServer().stopServer();
                Main.getServerThread().interrupt();
            } catch (IOException e) {
                System.out.println("Server socket closed");
            } // terminate previously running server
        }

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

            if (Main.getHostUsername() != null) { // used when the players clicks "back" on the waiting screen
                TextField enter_username_field = (TextField) root.lookup("#enter_username_field");
                if (enter_username_field != null)
                    enter_username_field.setText(Main.getHostUsername());
            }
            if (Main.getServerPortNumber() != null) {
                TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                if (enter_port_number_field != null)
                    enter_port_number_field.setText(Main.getServerPortNumber());
            }

            // set event listener for "host server button"

            Button start_server_button = (Button) root.lookup("#start_server_button");
            if (start_server_button != null) {
                start_server_button.setOnAction(e -> {

                    TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                    TextField enter_username_field = (TextField) root.lookup("#enter_username_field");

                    Main.setHostUsername(enter_username_field.getText());
                    Main.setServerPortNumber(enter_port_number_field.getText());

                    Label error_message_label = (Label) root.lookup("#error_message_label");

                    try {
                        int port_number_integer = Integer.parseInt(enter_port_number_field.getText());

                        if (port_number_integer >= 1024 && port_number_integer <= 49151) {
                            // valid port number
                            // check username - no whitespaces
                            if (enter_username_field.getText().contains(" ")) {

                                String error_message = bundle.getString("username_white_space_error");
                                error_message_label.setText(error_message);

                            } else {
                                // move to next scene
                                try {
                                    ServerSocket server_socket = new ServerSocket(Integer.parseInt(Main.getServerPortNumber()));
                                    Server server = new Server(server_socket, enter_username_field.getText());
                    
                                    Thread server_thread = new Thread(server);
                                    Main.setServerThread(server_thread);
                                    server_thread.start();
                                    switchToServerWait();
                                    
                                } catch (BindException error) {
                                    String error_message = bundle.getString("bind_exception");
                                    error_message_label.setText(error_message);
                                } catch(IOException error){
                                    
                                }
                                
                            }

                        } else {

                            String error_message = bundle.getString("port_number_out_of_bounds_error");
                            error_message_label.setText(error_message);

                        }

                    } catch (NumberFormatException error) {
                        // not a proper port number
                        System.err.println("Invalid port number");
                        String error_message = bundle.getString("invalid_port_number_error");
                        error_message_label.setText(error_message);
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

            }

            if (Main.getClientUsername() != null) { // used when the players clicks "back" on the waiting screen
                TextField enter_username_field = (TextField) root.lookup("#enter_username_field");
                if (enter_username_field != null)
                    enter_username_field.setText(Main.getClientUsername());
            }
            if (Main.getServerPortNumber() != null) {
                TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                if (enter_port_number_field != null)
                    enter_port_number_field.setText(Main.getServerPortNumber());
            }

            Button join_friend_button = (Button) root.lookup("#join_friend_button");
            join_friend_button.setOnAction(e -> {
                // validate information
                TextField enter_ip_address_label = (TextField) root.lookup("#enter_ip_address_field");
                TextField enter_port_number_field = (TextField) root.lookup("#enter_port_number_field");
                TextField enter_username_field = (TextField) root.lookup("#enter_username_field");

                Main.setClientUsername(enter_username_field.getText());
                Main.setServerPortNumber(enter_port_number_field.getText());

                try {

                    int port_number_integer = Integer.parseInt(enter_port_number_field.getText());

                    if (!validateIpAddress(enter_ip_address_label.getText())) {

                        // if ip address not valid
                        Label error_message_label = (Label) root.lookup("#error_message_label");
                        String error_message = bundle.getString("invalid_ip_address_error");
                        error_message_label.setText(error_message);

                    } else if (port_number_integer <= 1024 || port_number_integer >= 49151) {
                        // if port number not valid
                        Label error_message_label = (Label) root.lookup("#error_message_label");
                        String error_message = bundle.getString("port_number_out_of_bounds_error");
                        error_message_label.setText(error_message);
                    } else if (enter_username_field.getText().contains(" ")) {
                        Label error_message_label = (Label) root.lookup("#error_message_label");
                        String error_message = bundle.getString("username_white_space_error");
                        error_message_label.setText(error_message);
                    } else {
                        // continue with connecting to server
                        // attempt to connect. If server not available show error message
                        try {
                            // connect to host from server controller because if an error 
                            // occurs, use the error label

                            // set timeout to 3 seconds because its too long by default
                            Socket client_socket = new Socket();
                            client_socket.connect(
                                    new InetSocketAddress(enter_ip_address_label.getText(), port_number_integer), 3000);

                            // connection successful
                            System.out.println("Connected to server successfully");
                            Client client = new Client(client_socket, enter_username_field.getText());
                            Main.setClient(client);

                            Thread client_thread = new Thread(client);
                            Main.setClient_thread(client_thread);
                            client_thread.start();

                        } catch (UnknownHostException error) {
                            // invalid ip address
                            System.out.println("Exception: " + error.getMessage());
                            error.printStackTrace();

                            Label error_message_label = (Label) root.lookup("#error_message_label");
                            String error_message = bundle.getString("invalid_ip_address_error");
                            error_message_label.setText(error_message);

                        } catch (SocketTimeoutException error) {
                            System.out.println("Exception: " + error.getMessage());
                            error.printStackTrace();

                            Label error_message_label = (Label) root.lookup("#error_message_label");
                            String error_message = bundle.getString("io_exception_timed_out");
                            error_message_label.setText(error_message);
                        } catch (IOException error) {
                            System.out.println("Exception: " + error.getMessage());
                            error.printStackTrace();

                            Label error_message_label = (Label) root.lookup("#error_message_label");
                            String error_message = bundle.getString("io_exception_refused");
                            error_message_label.setText(error_message);
                        }

                    }
                } catch (NumberFormatException error) {
                    // not a proper port number
                    System.err.println("Invalid port number");
                    String error_message = bundle.getString("invalid_port_number_error");
                    Label error_message_label = (Label) root.lookup("#error_message_label");
                    error_message_label.setText(error_message);
                }

            });

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
            if (server_port_number_label != null) {
                StringBuilder text = new StringBuilder(server_port_number_label.getText());
                text.append(" " + Main.getServerPortNumber());
                server_port_number_label.setText(text.toString());
            }
            Label server_username_label = (Label) root.lookup("#server_username_label");
            if (server_username_label != null) {
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
            stage.show();
            
            // host (server) switches to game view from thread, since this scene will
            // be shown first

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToMainMenu() {
        MainController mc = new MainController();
        mc.switchToMainMenu(null);
    }

    public static boolean validateIpAddress(String ip_address) {
        if (ip_address == null || ip_address.isEmpty()) {
            return false;
        }

        // Regular expression for IPv4 addresses
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";

        return ip_address.matches(ipPattern);
    }

}
