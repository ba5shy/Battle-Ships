import java.net.Socket;

public class Client implements Runnable{

    private String host_username;
    private String username;
    private Socket socket;
    private GameController game_Controller;
    
    public Client(Socket client_socket, String client_username){
        this.username = client_username;
        this.socket = client_socket;
    }

    public void run(){
        // client already in game scene with ships placed
        // receive host username

        // send client username
    }
}
