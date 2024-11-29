import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javafx.application.Platform;

public class Client implements Runnable {

    private String host_username;
    private String username;
    private Socket socket;
    private GameController game_controller;

    private String server_message;
    private String client_message;

    

    public Client(Socket client_socket, String client_username) {
        this.username = client_username;
        this.socket = client_socket;
        this.game_controller = new GameController();
    }

    public void run() {

        try {
            // receive from server
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader buffered_reader = new BufferedReader(input);

            // send to server
            OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter buffered_writer = new BufferedWriter(output);

            // after connecting with the host, receive host username
            server_message = buffered_reader.readLine();
            logServerMessage(server_message);
            this.host_username = server_message.split("=")[1];

            // send client username
            client_message = "username=" + username;
            send_message(buffered_writer, client_message);

            // set usernames for game controller
            game_controller.username = username;
            game_controller.opponent_username = host_username;
            game_controller.is_host = false;

            // switch to game scene (this includes randomly placing ships)
            game_controller.switchToGameScene();

            // receive "ready" from server
            server_message = buffered_reader.readLine();
            logServerMessage(server_message);

            // send ready to server
            client_message = "ready";
            send_message(buffered_writer, client_message);
            
            if(server_message.equals("ready")){
                String winner;
                game_controller.my_turn = false;

                while(true){ 
                    // game loop. host starts first 

                    // receive hit coordinates
                    server_message = buffered_reader.readLine();
                    logServerMessage(server_message);

                    String[] split = server_message.split(":");
                    String command = split[0];
                    String value = split[1]; // (x,y)

                    if (command.equals("destroy")) {
                        // server hit a square
                        client_message = game_controller.processDestroyCoordinatesFromOpponent(value);

                        // send result to opponent
                        send_message(buffered_writer, client_message);
                        
                    } else if(command.equals("end_game")){
                        if(value.equals("i won")){ // server won
                            winner = host_username + " (Opponent)";
                            Platform.runLater(() ->{
                                MainController mc = new MainController();
                                mc.switchToMainMenu("you_lost");
                                try{
                                    this.socket.close();
                                    Main.getClient_thread().stop();
                                } catch(IOException e){
                                    System.out.println(e.getMessage());
                                }
                            });
                        } 
                    }

                    client_message = getCoordinate();
                    game_controller.user_chosen_cell = null; // reset coordinates
                    game_controller.my_turn = false;

                    send_message(buffered_writer, client_message); // hit:[x,y]
    
                    // receive hit result
                    server_message = buffered_reader.readLine();
                    logServerMessage(server_message);

                    split = server_message.split(":");
                    command = split[0];
                    value = split[1];

                    if(command.equals("hit")){
                        if(game_controller.processHitCoordinates(value)){
                            winner = username;
                            client_message = "end_game:i won";
                            send_message(buffered_writer, client_message);
                            Platform.runLater(() ->{
                                MainController mc = new MainController();
                                mc.switchToMainMenu("you_won");
                            });
                        }
                    } else if(command.equals("miss")){
                        game_controller.processMissCoordinates(value);
                    } else if(command.equals("end_game")){
                        if(value.equals("i won")){ // server won
                            winner = host_username + " (Opponent)";
                            Platform.runLater(() ->{
                                MainController mc = new MainController();
                                mc.switchToMainMenu("you_lost");
                            });
                        } 
                    }
                    
                }
                
            }
            

        } catch (IOException e) {
            MainController main_controller = new MainController();
            main_controller.switchToMainMenu("connection_lost");
            e.printStackTrace();
        }

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

    void send_message(BufferedWriter buffered_writer, String text) throws IOException {
        // this method is used to send a single message
        buffered_writer.write(text);
        buffered_writer.newLine();
        buffered_writer.flush();
    }

    void logServerMessage(String server_message){
        System.out.println("[Server] - " + server_message);
    }
}
