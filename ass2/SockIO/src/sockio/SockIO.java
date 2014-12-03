package sockio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * Basic send receive protocol.  Every send is prefaced by 4 byte length.
 * Length headers are not encrypted.
 * @author kdbanman
 */
public class SockIO {
    private Socket sock;
    private InputStream is;
    private OutputStream os;
    
    public SockIO(Socket client) throws IOException {
        sock = client;
        is = sock.getInputStream();
        os = sock.getOutputStream();
    }
    
    public String recvString() throws IOException {
        return new String(recv());
    }
    
    public String recvString(byte[] key) throws IOException {
        return Decryptor.decString(recv(), key);
    }
    
    public byte[] recv(byte[] key) throws IOException {
        return Decryptor.dec(recv(), key);
    }
    
    public byte[] recv() throws IOException {
        System.out.println("  --Waiting to receive...");
        byte[] lenBuf = new byte[4];
        int read = 0;
        read = is.read(lenBuf, 0, lenBuf.length);
        if (read != 4) throw new IOException("Size header could not be read.");
        System.out.println("  --Done");
        
        int len = ByteBuffer.wrap(lenBuf).getInt();
        
        System.out.println("  --Reading " + Integer.toString(len) + " byte payload...");
        byte[] msg = new byte[len];
        is.read(msg, 0, len);
        System.out.println("  --Done");
        
        System.out.println("  --Received: " + new String(msg));
        
        return msg;
    }
    
    public void send(String toSend, byte[] key) throws IOException {
        send(Encryptor.enc(toSend, key));
    }
    
    public void send(byte[] toSend, byte[] key) throws IOException {
        send(Encryptor.enc(toSend, key));
    }
    
    public void send(String toSend) throws IOException {
        send(toSend.getBytes());
    }
    
    public void send(byte[] bytes) throws IOException {
        System.out.println("  --Sending length header...");
        byte[] len = ByteBuffer.allocate(4).putInt(bytes.length).array();
        os.write(len, 0, len.length);
        os.flush();
        System.out.println("  --Done");

        System.out.println("  --Sending " + Integer.toString(bytes.length) + " byte payload...");
        os.write(bytes, 0, bytes.length);
        os.flush();
        System.out.println("  --Done");
    }
}
