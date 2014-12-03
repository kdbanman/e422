package sockio;

/**
 *
 * @author kdbanman
 */
public class Encryptor {
    public static byte[] enc(String plain, byte[] key) {
        return plain.getBytes();
    }
    
    public static native byte[] encNative(byte[] plain, byte[] key);
}
