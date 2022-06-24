import { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../context';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { findAll, updateRoles } from "../services/authorization";
import { RadioGroup, Typography, Radio, Button, FormControl, FormLabel, FormControlLabel } from '@mui/material';

export default function Administration() {

    const { register, handleSubmit} = useForm();
    const [userInfo, setUserInfo] = useState([]);
    const { user } = useContext(AuthContext);
    const [err, setErr] = useState(false);
    const [errInfo, setErrInfo] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {
        findAll()
            .then(setUserInfo)
            .catch(setUserInfo([]));
    }, []);

    // Three paramaters -> Data from the register, the event, and the data from that list item.
    const onSubmit = (data, evt, otherData) => {
        evt.preventDefault();

        // Check if the roles are the same. No need to update if they are.
        if (otherData.role === data.role) {
            setErr(true);
            setErrInfo(["The roles are the same."]);
            return;
        }

        // Create an object using old data and set the new role
        let newData = { ...otherData, role: data.role };

        // Pass in the new data, no need to navigate since we are assuming that the user may do multiple edits.
        updateRoles(newData)
            .then().catch();
    }
    const userlist = () => {
        return userInfo.map(a => {
            return (
                <div key={a.appUserId + a.username}>
                    {(user.sub && user.sub === a.username) ?
                        <></> : changeRoleForm(a)
                    }</div>
            )
        })
    }

    const changeRoleForm = (a) => {
        return (
            <>
                <form onSubmit={handleSubmit((data, evt) => onSubmit(data, evt, a))}>
                    <div className='row d-flex justify-contents-center' style={{backgroundColor: 'lightgray', width: '41em', borderStyle: 'dotted'}}>
                    <div className='col-4 d-flex align-items-center' style={{ borderRightStyle: 'solid'}}>
                    <Typography variant="h7">{a.username}</Typography>
                    </div>
                    <div className='col-4' style={{borderRightStyle:'solid'}}>
                    <FormControl className='ml-5' component="fieldset">
                        <FormLabel id="role-label">Role</FormLabel>
                        <RadioGroup
                            row
                            aria-labelledby='role-label'
                            name="role"
                            defaultValue={a.role}
                        >
                            <FormControlLabel value="ADMIN" control={<Radio />} label="Admin" {...register("role")} />
                            <FormControlLabel value="USER" control={<Radio />} label="User" {...register("role")} />
                        </RadioGroup>
                    </FormControl>
                    </div>
                    <div className='d-flex align-items-center'>
                    <Button variant="contained" color="primary" className='ml-5' type='submit'>Submit</Button>
                    </div>
                    </div>
                </form>
            </>
        )
    }


    return (
        <>
        <div className='container mt-3'>
            {
                userInfo ? userlist() : <p>Loading...</p>
            }
        </div>
        <div className='container mt-2'>
            <Button variant='contained' color='inherit' component={Link} to='/'>Return</Button>
            {
                err ? <p>{errInfo}</p> : <></>
            }
        </div>
        </>
    )
}