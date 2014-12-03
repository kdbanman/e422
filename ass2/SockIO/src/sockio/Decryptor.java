package sockio;

/**
 * Encrypt and decrypt use a padding protocol.  Between 8 and 15 bytes are
 * added to the end of the plaintext to bring the cipher length to 0 mod 8.
 * If the final byte of the plaintext is 0x00, the pad bytes are 0x01.
 * Otherwise (including plaintext length 0), the pad bytes are 0x00.
 * @author kdbanman
 */
public class Decryptor {
    public static String decString(byte[] cipher, byte[] key) {
        return new String(dec(cipher, key));
    }
    
    public static byte[] dec(byte[] cipher, byte[] key) {
        byte[] intermediate = decNative(cipher, key);
        
        // final byte of intermediate is pad value
        byte pad = intermediate[intermediate.length - 1];
        
        // find rightmost nonpad value
        int rBound = intermediate.length - 1;
        while (intermediate[rBound] == pad) rBound--;
        
        // create array excluding the pad
        byte[] plain = new byte[rBound + 1];
        for (int i = 0; i < plain.length; i++) {
            plain[i] = intermediate[i];
        }
        
        return plain;
    }
    
    static { System.load(System.getProperty("java.library.path") +
                                    "/libdecrypt.so"); }
    
    private static native byte[] decNative(byte[] cipher, byte[] key);
}
