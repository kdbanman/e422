package sockio;

/**
 *
 * @author kdbanman
 */
public class Encryptor {
    public static byte[] enc(String plain, byte[] key) {
        return plain.getBytes();
    }
    
    static { System.load(System.getProperty("java.library.path") +
                                    "/libencrypt.so"); }
    
    public static native byte[] enc(byte[] plain, byte[] key);
}
