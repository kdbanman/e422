package encserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
            log("Connected to " + sock.getRemoteSocketAddress());

            // block user until valid encrypted username given
            authenticate();
            
            // service file requests from client until "finished"
            getFiles();
            
            log("Client is finished.");
            sock.close();
            this.interrupt();
        } catch (IOException ex) {
            ex.printStackTrace();
            log("Socket Stream I/O Error.");
        }
    }
    
    private void authenticate() throws IOException {
        // receive messages until valid encrypted username received
        boolean auth = false;
        while (!auth) {
            log("Waiting for user auth...");
            byte[] nameCipher = sio.recv();
            
            // check each user name using that user's key
            for (User u : users) {
                String possibleName = Decryptor.decString(nameCipher, u.key);
                
                if (possibleName.equals(u.name)) {
                    // grant access if username match found
                    auth = true;
                    user = u;
                    log("Access granted to user \"" + user.name + "\"");
                    sio.send("access-granted", user.key);
                    break;
                }
            }
            if (!auth) {
                // deny access if username not found
                log("Unathorized access attempt.");
                sio.send("access-denied");
            }
        }
    }
    
    private void getFiles() throws IOException {
        String request = "";
        while (true) {
            log("Waiting for file path request...");
            // get client request
            request = sio.recvString(user.key);

            // detect if client is done requesting files
            if (request.equalsIgnoreCase("finished")) return;

            // make sure client cannot request files outside server
            // process directory
            if (request.contains("..") || request.startsWith("/")) {
                sio.send("not-found", user.key);
                log("Could not send file \"" + request + "\"");
                continue;
            }

            // make sure file exists and is not a directory and is readable
            File f = new File(request);
            if (!(f.exists() && !f.isDirectory()  && f.canRead())) {
                sio.send("not-found", user.key);
                log("Could not send file \"" + request + "\"");
                continue;
            }

            // acknowdlegde file existence to client
            sio.send("found", user.key);
            
            sendFile(f);
            
            log("File sent");
        }
    }
    
    private void sendFile(File file) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            log("Reading file...");
            
            // prepare buffer
            byte[] fileBytes = new byte[(int) file.length()];
            
            // prepare streams
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            
            // read file into buffer
            bis.read(fileBytes, 0, fileBytes.length);
            
            log("Sending file...");
            
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
    
    private void log(String s) {
        System.out.println(sock.getRemoteSocketAddress() + ":  " + s);
    }
}
