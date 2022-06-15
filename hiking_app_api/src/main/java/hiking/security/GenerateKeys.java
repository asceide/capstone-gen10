package hiking.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GenerateKeys {

    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPairGenerator.initialize(keyLength);
    }

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

    public void writeToFile(String path, byte[] key) throws IOException{
        Path paths = Paths.get(path);
        List<String> lines = new ArrayList<>();

        lines.add(new String(key, "UTF-8"));

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

    public static void makeFiles(String path){
        GenerateKeys keys;
        try{
            keys = new GenerateKeys(2048);
            keys.generateKey();
            Path of = Path.of(path + "/privateKey");
            if(!Files.exists(Paths.get(path+"/publicKey")) && !Files.exists(of)){
                keys.writeToFile(path+"/publicKey", Base64.getDecoder().decode(keys.getPublicKey().getEncoded()));
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
