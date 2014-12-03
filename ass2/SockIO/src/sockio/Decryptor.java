package sockio;

/**
 *
 * @author kdbanman
 */
public class Decryptor {
    public static String decString(byte[] plain, byte[] key) {
        return new String(plain);
    }
    
    public static byte[] dec(byte[] cipher, byte[] key) {
        return cipher;
    }
    
    static { System.load(System.getProperty("java.library.path") +
                                    "/libdecrypt.so"); }
    
    public static native byte[] decNative(byte[] cipher, byte[] key);
}
