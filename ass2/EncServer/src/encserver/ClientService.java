package encserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import sockio.Decryptor;
import sockio.Encryptor;
import sockio.SockIO;

/**
 *
 * @author kdbanman
 */
public class ClientService extends Thread {
    private ArrayList<User> users;
    private Socket sock;
    private SockIO sio;
    private User user;
    
    public ClientService(Socket cSock, ArrayList<User> users) {
        this.users = users;
        sock = cSock;
        sio = null;
        user = null;
    }
    
    @Override
    public void run() {
        try {
            sio = new SockIO(sock);
            System.out.println("Connected to " + sock.getRemoteSocketAddress());

            // block user until valid encrypted username given
            authenticate();
            
            // service file requests from client until "finished"
            getFiles();
            
            sock.close();
        } catch (IOException ex) {
            System.err.println("Socket Stream I/O Error.");
        }
    }
    
    private void authenticate() throws IOException {
        // receive messages until valid encrypted username received
        boolean auth = false;
        while (!auth) {
            System.out.println("Waiting for user auth...");
            byte[] nameCipher = sio.recv();
            
            // check each user name using that user's key
            for (User u : users) {
                String possibleName = Decryptor.decString(nameCipher, u.key);
                
                if (possibleName.equals(u.name)) {
                    // grant access if username match found
                    auth = true;
                    user = u;
                    System.out.println("Access granted to " + user.name);
                    sio.send("access-granted", user.key);
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
    
    private void getFiles() throws IOException {
        String request = "";
        while (true) {
            System.out.println("Waiting for file path request...");
            // get client request
            request = sio.recvString();

            // detect if client is done requesting files
            if (request.equalsIgnoreCase("finished")) return;

            // make sure client cannot request files outside server
            // process directory
            if (request.contains("..") || request.startsWith("/")) {
                sio.send("not-found", user.key);
                continue;
            }

            // make sure file exists and is not a directory and is readable
            File f = new File(request);
            if (!(f.exists() && !f.isDirectory()  && f.canRead())) {
                sio.send("not-found", user.key);
                continue;
            }

            // acknowdlegde file existence to client
            sio.send("found", user.key);
            
            sendFile(f);
        }
    }
    
    private void sendFile(File file) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // prepare buffer
            byte[] fileBytes = new byte[(int) file.length()];
            
            // prepare streams
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            
            // read file into buffer
            bis.read(fileBytes, 0, fileBytes.length);
            
            // send file
            sio.send(fileBytes, user.key);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }
}
