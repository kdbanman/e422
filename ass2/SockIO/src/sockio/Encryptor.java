package sockio;

/**
 *
 * @author kdbanman
 */
public class Encryptor {
    public native byte[] enc(byte[] plain, byte[] key);
}
