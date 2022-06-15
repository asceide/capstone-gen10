package hiking.security;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

// This class will be used to decrypt the encrypted password from the front-end
// Registered as a component for Spring to be used in the controller
@Component
public class Cryptography {
    // To be used for encryption and decryption functionality.
    private Cipher cipher;

    // Since we are using RSA for our encryption...
    public Cryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("RSA");
    }

    // This method is used to load up the private key from the file.
    public PrivateKey getPrivateKey(String filename) throws Exception{
        // Reads the bytes of the private key.
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        // Private keys are encoded via PKCS.8 while Public keys are encoded via X.509.
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        // Creates a key factory to generate a private key from the spec
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // Return the private key
        return kf.generatePrivate(spec);
    }

    public String decrypt(String encryptedPass, PrivateKey key) throws GeneralSecurityException{
        // We init the cipher to decrypt using the private key from file.
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        // The password is Base64 encoded, and using new String(this.cipher.doFinal(password).getBytes doesn't work well and causes problems.
        // So to get the password, we need to decode and get its bytes.
        byte[] unconverted = Base64.getDecoder().decode(encryptedPass);
        try {
            // Then we decrypt the password with UTF-8 encoding and return it.
            return new String(this.cipher.doFinal(unconverted), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
