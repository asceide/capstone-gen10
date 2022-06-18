// The JSEncrypt library is used to encrypt the password.
import { JSEncrypt } from 'jsencrypt';


export function encrypt(pkey, password){

    // Create a new instance of the JSEncrypt object with the default key size of 2048
    const encrypt = new JSEncrypt({
        default_key_size: 2048
    });

    // Set the public key to be used by the encryptor
    encrypt.setPublicKey(pkey);
    
    // Encrypt the password
    return encrypt.encrypt(password);


}