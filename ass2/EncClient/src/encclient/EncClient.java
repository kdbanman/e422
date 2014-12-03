package encclient;

import java.io.BufferedReader;
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
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

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
            
            response = sio.recvString();
            
        }
        System.out.println("Authenticated.");
        
        name = id;
        key = getKey(keyStr);
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
            
            sock.close();
        } catch (IOException e) {
            System.err.println("Socket Stream I/O Error.");
        }
    }
}
