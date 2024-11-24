
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {

    private static Locale locale = new Locale("en");

    public static Stage stage;

    // used to go back to server information scene

    public static String server_ip_address; // used with host-player (server)
    public static String server_port_number; // used with host-player (server)
    public static String host_username; // used with host-player (server)
    public static String client_username; // used with joining-player (client)

    public static Thread server_thread;
    public static Server server;

    public static Thread client_thread;
    public static Client client;

    // the variables above are stored in the main class because
    // they will be used between different scenes

    @Override
    public void start(Stage stage) {

        setStage(stage);
        stage.setResizable(false);

        MainController main_controller = new MainController();
        main_controller.switchToMainMenu();
    }

    public static Thread getClient_thread() {
        return client_thread;
    }
    public static void setClient_thread(Thread my_client_thread) {
        client_thread = my_client_thread;
    }

    public static Client getClient() {
        return client;
    }
    public static void setClient(Client my_client) {
        client = my_client;
    }



    public static void setServer(Server my_server){
        server = my_server;
    }
    public static Server getServer(){
        return server;
    }
    public static void setServerThread(Thread my_server_thread){
        server_thread = my_server_thread;
    }
    public static Thread getServerThread(){
        return server_thread;
    }

    public static void setServerIpAddress(String my_server_ip_address){
        server_ip_address = my_server_ip_address;
    }
    public static String getServerIpAddress(){
        return server_ip_address;
    }

    public static void setServerPortNumber(String my_server_port_number){
        server_port_number = my_server_port_number;
    }
    public static String getServerPortNumber(){
        return server_port_number;
    }

    public static void setHostUsername(String my_host_username){
        host_username = my_host_username;
    }
    public static String getHostUsername(){
        return host_username;
    }

    public static void setClientUsername(String my_client_username){
        client_username = my_client_username;
    }
    public static String getClientUsername(){
        return client_username;
    }

    void setStage(Stage myStage) {
        stage = myStage;
    }

    public static Stage getStage() {
        return stage;
    }

    static void setLocale(Locale newLocale){
        locale = newLocale;
    }

    static Locale getLocale(){
        return locale;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
