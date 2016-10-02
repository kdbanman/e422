package encserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 *
 * @author kdbanman
 */
public class EncServer {

    public static void main(String[] args) {

        ArrayList<User> users;
        ServerSocket serverSocket;
     
        int port = 16000;

        // add hardcoded users and keys
        users = new ArrayList<>();

        users.add(new User("test", "testtest"));
        users.add(new User("kirby", "kirbybanman"));
        users.add(new User("scott", "0123456789012345678901234567890123456789"));

        try {
            // bind to socket
            serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    System.out.println("Waiting for client on port 16000...");
                    Socket cSock = serverSocket.accept();
                    
                    ClientService svc = new ClientService(cSock, users);
                    svc.start();
                    
                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not bind to port 16000!");
            e.printStackTrace();
        }
    }
}
