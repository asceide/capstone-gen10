import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { authenticate } from '../services/authentication';

// To use with encryption.
import { JSEncrypt } from 'jsencrypt';

 const publicKeyFile = 'public_key/publicKey'
export default function Login() {
    const [username, setUsername] = useState('');
    const [uepassword, setUepassword] = useState('');
    const [errors, setErrors] = useState(false);
    const [pkey, setPkey] = useState('');

    const navigate = useNavigate();

    const { login } = React.useContext(AuthContext);


    useEffect(() => {
        // We nab the public key from the file in the public folder. It is in a text format.
        fetch(publicKeyFile).then(res => res.text()).then(res => setPkey(res.replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replace("\n", "")));
    }, [setPkey]);

    const handleUsername = (ev) => {
        setUsername(ev.target.value);
    };

    const handlePassword = (ev) => {
        setUepassword(ev.target.value);
    ;}

    const handleSubmit = (ev) => {
        // As always, to prevent reloading
        ev.preventDefault();

        // The reason we named the password gotten from the user as uepassword (short for unencrypted password) is because when we sent the authentication request, we need the
        // credentials object to have that key called password.
        let password = encryptPass(uepassword);

        // Sent, Authenticate, and Navigate home.
        authenticate({ username, password })
        .then(user => {
            login(user);
            navigate('/');
        })
        .catch(() => {setErrors(true);});
    };

    const encryptPass = (pass) => {
        // Create a new JSEncrypt object with the default key size of 2048 (which is the size we are using in the back end)
        let encrypt = new JSEncrypt({
            default_key_size: 2048
        });

        // Set the public key to the contents of the publicKeyFile
        encrypt.setPublicKey(pkey);
        // Encrypt the password using the public key. It already does Base64 encoding for us so there is no need to use btoa()
        let encryptedPass= encrypt.encrypt(pass);

        // Return the password.
        return encryptedPass;

    }

    return (
        <div>
            <h1>Login</h1>
            <div>
                <label htmlFor='username' className='form-label'>Username</label>
                <input type='email' className='form-control' id='username' name='username' value={username} onChange={handleUsername} />
            </div>
            <div>
                <label htmlFor='password' className='form-label'>Password</label>
                <input type='password' className='form-control' id='password' name='password' value={uepassword} onChange={handlePassword} />
            </div>
            <div>
                <button type='submit' className='btn btn-primary-outline' onClick={handleSubmit}>Login</button>
                <Link to="/" className='btn btn-warning-outline'>Cancel</Link>
            </div>
            {errors && <p>Invalid username or password</p>}
        </div>
    )
}