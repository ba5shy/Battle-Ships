import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        if (choice == 1) {
            ServerSocket s_socket = new ServerSocket(9000);
            while (true) {
                System.out.println("server started");
                s_socket.accept();
            }
        } else {
            try {
                Socket socket = new Socket("localhost", 4000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

                if(e.getMessage().equals("Connection refused: connect"))
                System.out.println(true);
            }
        }

    }
}
