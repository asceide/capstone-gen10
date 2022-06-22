import React, { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import UserContext from '../context/UserContext';
import { authenticate } from '../services/authentication';
import { findByEmail } from '../services/users';
import { useForm } from 'react-hook-form';
import { Button, InputLabel, Typography, Input } from '@mui/material';



export default function Login() {

    const [errors, setErrors] = useState(false);

    const navigate = useNavigate();

    const { login, pkey, encryption } = useContext(AuthContext);
    const { userInfo } = useContext(UserContext);

    const { register, handleSubmit } = useForm();

    const onSubmit = (data, evt) => {
        evt.preventDefault();

        let password = encryption(pkey, data.password);
        let username = data.username.toLowerCase();


        authenticate({ username, password })
            .then(user => {
                login(user);
                findByEmail(username).then(info => {
                    userInfo(info);
                }).catch(() => { setErrors(true) });
                navigate('/');
            })
            .catch(() => { setErrors(true); });

    };






    return (
        <>
            <div className='container mt-3 mb-3'>
                <Typography variant='h3'>Login</Typography>
            </div>
            <div className='container'>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className='mb-2'>
                        <InputLabel htmlFor="username" >Username</InputLabel>
                        <Input id="username" name="username" type="email" {...register("username")} />
                    </div>
                    <div className='mb-3'>
                        <InputLabel htmlFor="password" >Password</InputLabel>
                        <Input id="password" name="password" type="password" {...register("password")} />
                    </div>
                    <div className>
                        <Button type="submit" variant="contained" color="inherit" className='mr-2'>Login</Button>
                        <Link to="/"><Button variant='contained' color='warning'>Cancel</Button></Link>
                    </div>
                </form>
                {errors && <p>Invalid username or password</p>}
            </div>
        </>
    )
}