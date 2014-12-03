package encserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import sockio.Decryptor;
import sockio.SockIO;

/**
 *
 * @author kdbanman
 */
public class ClientService extends Thread {
    private ArrayList<User> users;
    private Socket sock;
    private SockIO sio;
    private String name;
    
    public ClientService(Socket cSock, ArrayList<User> users) {
        this.users = users;
        sock = cSock;
        sio = null;
        name = null;
    }
    
    @Override
    public void run() {
        try {
            sio = new SockIO(sock);
            System.out.println("Connected to " + sock.getRemoteSocketAddress());

            // block user until valid encrypted username given
            authenticate();
            
            sock.close();
        } catch (IOException ex) {
            System.err.println("Socket Stream I/O Error.");
        }
    }
    
    private void authenticate() throws IOException {
        // receive messages until valid encrypted username received
        boolean auth = false;
        while (!auth) {
            byte[] nameCipher = sio.recv();
            
            // check each user name using that user's key
            for (User u : users) {
                String possibleName = Decryptor.dec(nameCipher, u.key);
                
                if (possibleName.equals(u.name)) {
                    // grant access if username match found
                    auth = true;
                    name = possibleName;
                    System.out.println("Access granted to " + name);
                    sio.send("access-granted");
                    break;
                }
            }
            if (!auth) {
                // deny access if username not found
                System.out.println("Unathorized access attempt.");
                sio.send("access-denied");
            }
        }
    }
}
