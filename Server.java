import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements Runnable {

    private String username;
    private String client_username;
    private ServerSocket socket;
    private Socket client_socket;
    private GameController game_controller;

    private String client_message;
    private String server_message;

    public Server(ServerSocket server_socket, String username) {
        this.socket = server_socket;
        this.username = username;
        this.game_controller = new GameController();
    }

    public void run() {
        // server implementation
        try {

            client_socket = socket.accept();
            System.out.println("Client connected successfully");

            // used to send to client
            OutputStreamWriter output = new OutputStreamWriter(client_socket.getOutputStream());
            BufferedWriter buffered_writer = new BufferedWriter(output);

            // used to receive from client
            InputStreamReader input = new InputStreamReader(client_socket.getInputStream());
            BufferedReader buffered_reader = new BufferedReader(input);

            // send host username after client connects
            server_message = "username=" + username;
            send_message(buffered_writer, server_message);

            // receive client username
            client_message = buffered_reader.readLine();
            logClientMessage(client_message);
            this.client_username = client_message.split("=")[1];

            // set usernames for game controller
            game_controller.username = username;
            game_controller.opponent_username = client_username;
            game_controller.is_host = true;

            // switch to game scene (this includes randomly placing ships)
            game_controller.switchToGameScene();

            // send ready to client
            server_message = "ready";
            send_message(buffered_writer, server_message);

            // receive ready from client
            client_message = buffered_reader.readLine();
            logClientMessage(client_message);

            if(client_message.equals("ready")){
                while (true) { // game loop
                    // host starts first
                    
                    // send hit coordinates
                    game_controller.my_turn = true;
                    server_message = getCoordinate();
                    game_controller.user_chosen_cell = null; // reset coordinates
                    game_controller.my_turn = false;

                    send_message(buffered_writer, server_message);
                    
                    // receive hit coordinates
                    client_message = buffered_reader.readLine();
                    logClientMessage(client_message);
    
                    String[] split = client_message.split(":");
                    String command = split[0];
                    String value = split[1];
    
                    if (command.equals("destroy")) {
                        System.out.println("Client Destroy");
                        // client hit a square
                        if (shipHit(value)) {
    
                        }
                    }
    
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            MainController main_controller = new MainController();
            main_controller.switchToMainMenu("connection_lost");
        }
    }

    public void stopServer() throws IOException {
        this.socket.close();
    }

    public String getCoordinate(){
        while(true){
            if(game_controller.user_chosen_cell == null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            else{
                return "destroy:" + game_controller.user_chosen_cell;
            }
                
        } 
    }

    boolean shipHit(String coordinates) {
        // if coordinate hits a ship, return true
        return false;
    }

    void send_message(BufferedWriter buffered_writer, String text) throws IOException {
        // this method is used to send a single message
        buffered_writer.write(text);
        buffered_writer.newLine();
        buffered_writer.flush();
    }

    void logClientMessage(String client_message) {
        System.out.println("[Client] - " + client_message);
    }
}
