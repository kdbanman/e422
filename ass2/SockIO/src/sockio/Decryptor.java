package sockio;

/**
 *
 * @author kdbanman
 */
public class Decryptor {
    public static String dec(byte[] plain, byte[] key) {
        return new String(plain);
    }
    
    private static native byte[] decNative(byte[] cipher, byte[] key);
}
