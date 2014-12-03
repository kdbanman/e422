package sockio;

/**
 * Encrypt and decrypt use a padding protocol.  Between 8 and 15 bytes are
 * added to the end of the plaintext to bring the cipher length to 0 mod 8.
 * If the final byte of the plaintext is 0x00, the pad bytes are 0x01.
 * Otherwise (including plaintext length 0), the pad bytes are 0x00.
 * @author kdbanman
 */
public class Encryptor {
    public static byte[] enc(String plain, byte[] key) {
        
        return enc(plain.getBytes(), key);
    }
    
    public static byte[] enc(byte[] plain, byte[] key) {
        // find nonzero padding length to bring plaintext length to 0 mod 8
        int padLen = 8;
        while ((plain.length + padLen) % 8 != 0) padLen++;
        
        // determine pad value (must differ from final plaintext character)
        byte pad = 0x00;
        if (plain.length == 0 || plain[plain.length - 1] == 0x00) pad = 0x01;
        
        // copy plaintext and padding to intermediate plaintext
        byte[] intermediate = new byte[plain.length + padLen];
        for (int i = 0; i < intermediate.length; i++) {
            if (i < plain.length) intermediate[i] = plain[i];
            else intermediate[i] = pad;
        }
        
        return encNative(intermediate, key);
    }
    
    static { System.load(System.getProperty("java.library.path") +
                                    "/libencrypt.so"); }
    
    private static native byte[] encNative(byte[] plain, byte[] key);
}
