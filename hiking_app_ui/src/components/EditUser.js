import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Select, MenuItem, FormHelperText, InputLabel, Input, Button } from '@mui/material';
import { UserContext, AuthContext } from '../context';
import { emailRegex, passRegex } from '../helpers/regex';


export default function EditUser(){

    const { userInfo } = useContext(UserContext);
    const { user } = useContext(AuthContext);

    const onSubmit = (data, evt) => {
        evt.preventDefault();

        // Cleaning up the data - If the string is blank, then set it to null.
        if (data.firstName === "") {
            data.firstName = null;
        }
        if (data.lastName === "") {
            data.lastName = null;
        }

        if (data.city === "") {
            data.city = null;
        }
        if (data.state === "") {
            data.state = null;
        }
    }

    return (
        <div>
            <h1>Edit User</h1>
            <div className="container">
                <h3>User: {user.sub} </h3>
                <form onSubmit={onSubmit}>

                </form>
            </div>
        </div>
    )

}