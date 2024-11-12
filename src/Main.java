
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {

    private static Locale locale = new Locale("en");

    public static Stage stage;

    // used to go back to server information scene
    public static String server_ip_address;
    public static String server_port_number;
    public static String host_username;
    public static Thread server_thread;

    // the variables above are stored in the main class because
    // they will be used between different scenes

    @Override
    public void start(Stage stage) {

        setStage(stage);

        MainController main_controller = new MainController();
        main_controller.switchToMainMenu();
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
