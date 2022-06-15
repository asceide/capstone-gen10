package hiking.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*
    This class should only run if there isn't a private key present in the keys folder.
    Rather than having the private key packaged with the source code, you could upload it to a really secure
    S3 bucket and then use Amazon's AWS JDK to check or download the key.
    If not, generate and upload the key to S3
    Of course, everything would be done with environmental variables in that case.
    But this is just a demo of the idea behind it.
 */
public class GenerateKeys {

    // To be used to generate the keys.
    private KeyPairGenerator keyPairGenerator;

    // To be used to get the public and private keys
    private KeyPair keyPair;
    // Holds the public key
    private PublicKey publicKey;

    // Holds the private key.
    private PrivateKey privateKey;

    // When the class is instantiated, it initialize a KeyPairGenerator object.
    // It takes in a keyLength. Valid lengths are 512, 1024, 2048, and 4096. aka 2^9, 2^10, 2^11, and 2^12.
    // The default is 2048.
    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
        // We use RSA for our encryption.
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPairGenerator.initialize(keyLength);
    }

    // This method generates the keys and saves then into their respective Objects.
    public void generateKey(){
        this.keyPair = this.keyPairGenerator.generateKeyPair();
        this.publicKey = this.keyPair.getPublic();
        this.privateKey = this.keyPair.getPrivate();
    }

    public PublicKey getPublicKey(){
        return this.publicKey;
    }

    public PrivateKey getPrivateKey(){
        return this.privateKey;
    }

    // Writes the bytes of the keys to a file.
    // We will be using the default generated file for the private key
    // But we will be getting the public key in the form of a string that is typical for a .pem file.
    public void writeToFile(String path, byte[] key) throws IOException{
        Path paths = Paths.get(path);
        List<String> lines = new ArrayList<>();

        lines.add(new String(key));

        try{
            Files.write(paths, key);
        }catch(FileNotFoundException ex){
            try{
                Files.write(paths, lines, StandardOpenOption.CREATE_NEW);
            }catch(IOException e){
                e.printStackTrace();
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }


    // In this method we will be generating the key files. Again, the private key is in the default generated form.
    // The public key will be instead Base64 Mime encoded to a String that we can use in the front end.
    public static void makeFiles(String path) throws NoSuchAlgorithmException {
        // To generate the keys, in the same class!
        GenerateKeys keys;
        // Of course, there are always exceptions...
        try{
            // Generate new keys with a key length of 2048.
            keys = new GenerateKeys(2048);
            // Generate the keys.
            keys.generateKey();
            // Holds the private key path
            Path of = Path.of(path + "/privateKey");
            // If there is a private key, that means there is already a public key generated.
            // Likewise, we don't want to create a private key that will cause the public key to be unable to be decrypted
            if(!Files.exists(Paths.get(path+"/publicKey")) && !Files.exists(of)){
                // Create the public key file
                String pub_key= "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.getMimeEncoder().encodeToString(keys.getPublicKey().getEncoded())+"\n-----END PUBLIC KEY-----");
                keys.writeToFile(path+"/publicKey", pub_key.getBytes());
            }
            if(!Files.exists(of)){
                keys.writeToFile(path+"/privateKey", keys.getPrivateKey().getEncoded());
            }
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
