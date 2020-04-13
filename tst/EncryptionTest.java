import org.junit.Test;

import backend.Encryption;


public class EncryptionTest {
    @Test
    public void decryptTest() {
        assert(Encryption.decrypt(
                Encryption.encrypt("Password", "Master78901234567890"),
                "Master78901234567890"
            ).equals("Password")
        );
    }
}
