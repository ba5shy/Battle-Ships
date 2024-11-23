import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private String host_username;
    private ServerSocket socket;
    private Socket client_socket;
    private GameController game_controller;

    public Server(ServerSocket server_socket, String host_username){
        this.socket = server_socket;
        this.host_username = host_username;
    }

    public void run() {
        // server implementation
        try{
            while(true){
                client_socket = socket.accept();        
                // after client connects, switch scene for server (host)
                
                // after switching to game scene, send host username

                // receive client username
                
            }
            
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stopServer() throws IOException{
        this.socket.close();
    }
}
