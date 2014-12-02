package encclient;

import sockio.SockIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author kdbanman
 */
public class EncClient {
    
    public static void authenticate(PrintWriter out, BufferedReader in) throws IOException {
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

        String input = "";
        String response = "";
        while (!response.equalsIgnoreCase("access-granted")) {
            System.out.print("User ID?  ");
            input = userIn.readLine().trim().toLowerCase();

            System.out.println("WHAT");
            out.print((input + "\r\n").getBytes());
            
            System.out.println("THE HELL");
            
            response = in.readLine().trim().toLowerCase();
            
            System.out.println(response);
        }
        System.out.println("Authenticated.");
    }

    public static void main(String[] args) {
        try {
            // Connect and get I/O streams for socket
            
            System.out.println("Connecting to port 16000...");
            Socket sock = new Socket("localhost", 16000);
            System.out.println("Connected");

            SockIO sockio = new SockIO(sock);
            
            System.out.println(new String(sockio.recv()));
            
            sockio.send("asspiss".getBytes());
            
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
