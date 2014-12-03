package encserver;

/**
 *
 * @author kdbanman
 */
public class User {
    // key is interpreted as 4 long elements, each 8 bytes.
    int keyLength = 32;
    byte[] key;
    String name;
    
    /**
     * Key is generated from username, because this is a toy and I don't care.
     * @param name 
     */
    public User(String name, String key) {
        this.name = name;
        this.key = new byte[keyLength];
        
        byte[] keyBytes = key.getBytes();
        
        for (int i = 0; i < keyLength && i < key.length(); i++) {
            this.key[i] = keyBytes[i];
        }
    }
}
