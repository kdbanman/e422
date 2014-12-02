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
    private byte[] key;
    
    public SockIO(Socket client, byte[] encKey) {
        sock = client;
        key = encKey;
    }
    
    public byte[] recv() throws IOException {
        InputStream is = sock.getInputStream();
        
        System.out.println("Reading length header...");
        byte[] lenBuf = new byte[4];
        int read = 0;
        read = is.read(lenBuf, 0, lenBuf.length);
        if (read != 4) throw new IOException("Size header could not be read.");
        System.out.println("Done.");
        
        int len = ByteBuffer.wrap(lenBuf).getInt();
        
        System.out.println("Reading " + Integer.toString(len) + " byte payload...");
        byte[] msg = new byte[len];
        is.read(msg, 0, len);
        System.out.println("Done");
        
        //TODO decrypt here
        System.out.println(new String(msg));
        
        return msg;
    }
    
    public void send(byte[] bytes) throws IOException {
        OutputStream os = null;
        try {
            os = sock.getOutputStream();
            
            System.out.println("Sending length header...");
            byte[] len = ByteBuffer.allocate(4).putInt(bytes.length).array();
            os.write(len, 0, len.length);
            os.flush();
            System.out.println("Done.");
            
            //TODO encrypt here
            
            System.out.println("Sending " + Integer.toString(bytes.length) + " byte payload...");
            os.write(bytes, 0, bytes.length);
            os.flush();
            System.out.println("Done.");
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
    
    public void sendFile(String fname) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // send file
            File file = new File(fname);
            byte[] fileBytes = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bis.read(fileBytes, 0, fileBytes.length);
            send(fileBytes);
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }
    
    public void recvFile(String destPath) {
        //TODO get length
        //TODO read length bytes
        //TODO decrypt
        //TODO save file
    }
}
