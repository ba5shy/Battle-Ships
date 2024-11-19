import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private int port_number;
    private String host_username;
    private String client_usernamel;
    private ServerSocket server_socket;
    private Socket client_socket;
    

    public Server(int port_number, String host_username){
        this.port_number = port_number;
        this.host_username = host_username;
    }

    public void run() {
        // server implementation
        try{
            while(true){
                server_socket = new ServerSocket(this.port_number);
                
                System.out.println("Server Started");
                
                client_socket = server_socket.accept();

                System.out.println("Client Connected");
                
            }
            
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stopServer() throws IOException{
        this.server_socket.close();
    }
}
