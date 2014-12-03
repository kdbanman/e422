package sockio;

/**
 *
 * @author kdbanman
 */
public class Decryptor {
    public native byte[] dec(byte[] cipher, byte[] key);
}
