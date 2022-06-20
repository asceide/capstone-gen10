import React, {  useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import UserContext from '../context/UserContext';
import { authenticate } from '../services/authentication';
import { findByEmail } from '../services/users';



export default function Login() {
    const [username, setUsername] = useState('');
    const [uepassword, setUepassword] = useState('');
    const [errors, setErrors] = useState(false);

    const navigate = useNavigate();

    const { login, pkey, encryption } = useContext(AuthContext);
    const { userInfo } = useContext(UserContext);



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
        let password = encryption(pkey, uepassword);

        // Sent, Authenticate, and Navigate home.
        authenticate({ username, password })
        .then(user => {
            login(user);
            findByEmail(username).then(info => {
                userInfo(info);
            }).catch(() => {setErrors(true)});
            navigate('/');
        })
        .catch(() => {setErrors(true);});
    };


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