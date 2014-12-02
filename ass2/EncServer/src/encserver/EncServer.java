package encserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    private ArrayList<User> users;
    private ServerSocket serverSocket;
   
   public EncServer() throws IOException {
       users = new ArrayList<>();
       
       users.add(new User("test"));
       users.add(new User("kirby"));
       users.add(new User("scott"));
       
       serverSocket = new ServerSocket(16000);
   }

   public void run() {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port 16000...");
            Socket cSock = serverSocket.accept();
            System.out.println("Just connected to "
                  + cSock.getRemoteSocketAddress());
            
            DataInputStream in =
                  new DataInputStream(cSock.getInputStream());
            System.out.println(in.readUTF());
            
            DataOutputStream out =
                 new DataOutputStream(cSock.getOutputStream());
            out.writeUTF("Thank you for connecting to "
              + cSock.getLocalSocketAddress() + "\nGoodbye!");
            
            cSock.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      try
      {
         EncServer server = new EncServer();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
