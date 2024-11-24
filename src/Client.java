import java.net.Socket;

public class Client implements Runnable{

    private String host_username;
    private String username;
    private Socket socket;
    private GameController game_controller;
    
    public Client(Socket client_socket, String client_username){
        this.username = client_username;
        this.socket = client_socket;
        this.game_controller = new GameController();
    }

    public void run(){
        // switch to game scene
        game_controller.switchToGameScene();
        // receive host username

        // send client username
    }
}
