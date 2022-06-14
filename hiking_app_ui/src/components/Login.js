import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { authenticate } from '../services/authentication';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState(false);

    const navigate = useNavigate();

    const { login } = React.useContext(AuthContext);

    const handleUsername = (ev) => {
        setUsername(ev.target.value);
    };

    const handlePassword = (ev) => {
        setPassword(ev.target.value);
    ;}

    const handleSubmit = (ev) => {
        ev.preventDefault();
        authenticate({ username, password })
        .then(user => {
            login(user);
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
                <input type='password' className='form-control' id='password' name='password' value={password} onChange={handlePassword} />
            </div>
            <div>
                <button type='submit' className='btn btn-primary-outline' onClick={handleSubmit}>Login</button>
                <Link to="/" className='btn btn-warning-outline'>Cancel</Link>
            </div>
            {errors && <p>Invalid username or password</p>}
        </div>
    )
}