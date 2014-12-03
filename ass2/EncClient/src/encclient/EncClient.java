package encclient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import sockio.SockIO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import sockio.Encryptor;

/**
 *
 * @author kdbanman
 */
public class EncClient {
    static String name;
    static int keyLength;
    static byte[] key;
    
    static BufferedReader getUserIn() {
        return new BufferedReader(new InputStreamReader(System.in));
    }
    
    static byte[] getKey(String keyStr) {
        byte[] key = new byte[keyLength];
        
        byte[] keyBytes = keyStr.getBytes();
        
        for (int i = 0; i < keyLength && i < keyStr.length(); i++) {
            key[i] = keyBytes[i];
        }
        
        return key;
    }
    
    static void authenticate(SockIO sio) throws IOException {
        // allow user input from stdin
        BufferedReader userIn = getUserIn();

        String id = null;
        String keyStr = null;
        String response = "";
        while (!response.equalsIgnoreCase("access-granted")) {
            // get user id and key from stdin
            System.out.print("User ID?  ");
            id = userIn.readLine().trim().toLowerCase();
            System.out.print("Passkey?  ");
            keyStr = userIn.readLine().trim().toLowerCase();

            byte[] cipher = Encryptor.enc(id, getKey(keyStr));
            
            System.out.println("Sending " + new String(cipher) + "...");
            sio.send(cipher);
            System.out.println("Sent.");
            
            response = sio.recvString(getKey(keyStr));
            
        }
        System.out.println("Authenticated.");
        
        name = id;
        key = getKey(keyStr);
    }

    static void getFiles(SockIO sio) throws IOException {
        BufferedReader userIn = getUserIn();
        
        // while the user is not "finished"
        while (true) {
            // get file path request or finished command
            System.out.print("File path (or \"finished\")?  ");
            String path = userIn.readLine().trim();
            
            // send request to server
            sio.send(path, key);
            
            if (path.equalsIgnoreCase("finished")) break;
            
            System.out.println("Awaiting server response...");
            byte[] response = sio.recv(key);
            
            if (new String(response).equalsIgnoreCase("not-found")) {
                System.out.println("Server could not send file.");
            } else {
                System.out.println("Server sending file...");
                saveFile(sio, path);
            }
        }
    }
    
    static void saveFile(SockIO sio, String path) throws IOException {
        // set up output streams to save file
        FileOutputStream fos = new FileOutputStream(path);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        
        // receive file from server
        byte[] fileBytes = sio.recv(key);
        
        // save file to output stream
        bos.write(fileBytes, 0, fileBytes.length);
        bos.flush();
        fos.close();
        bos.close();
    }
    
    public static void main(String[] args) {
        keyLength = 32;
        name = null;
        
        try {
            // Connect and get I/O streams for socket
            
            System.out.println("Connecting to port 16000...");
            Socket sock = new Socket("localhost", 16000);
            System.out.println("Connected");

            SockIO sio = new SockIO(sock);
            
            // block user until server authenticates
            authenticate(sio);
            
            // get and save files from server using stdin
            getFiles(sio);
            
            sock.close();
        } catch (IOException e) {
            System.err.println("Socket Stream I/O Error.");
        }
    }
}
