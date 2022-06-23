import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { findAll } from "../services/authorization";
import { RadioGroup, Typography, Radio, Button, FormControl, FormLabel, FormControlLabel } from '@mui/material';

export default function Administration(){

    const { register, handleSubmit, formState: { errors }} = useForm();
    const [userInfo, setUserInfo] = useState([]);

    useEffect( () => {
        findAll()
            .then(setUserInfo)
            .catch(setUserInfo([]));
    }, []);

    const onSubmit = (data, evt) => {
        evt.preventDefault();
        console.log(data);
    }
    const userlist = () => {
        return userInfo.map(a => {
            return (
                <div key={a.username + "ad"}>
                    <Typography variant="h7">{a.username}</Typography>
                    <FormControl className='ml-5' component="fieldset">
                        <FormLabel id="role-label">Role</FormLabel>
                        <RadioGroup
                            row
                            aria-labelledby='role-label'
                            defaultValue={a.role}
                            >
                                <FormControlLabel value="ADMIN" control={<Radio />} name="role" label="Admin" {...register("role")} />
                                <FormControlLabel value="USER" control={<Radio />} name="role" label="User" {...register("role")}/>
                            </RadioGroup>
                    </FormControl>
                </div>
            )
        })}

    return(
        <>
        <form onSubmit={handleSubmit(onSubmit)}>{
            userInfo? userlist(): <p>Loading...</p>
        }
        </form>
        </>
    )
}