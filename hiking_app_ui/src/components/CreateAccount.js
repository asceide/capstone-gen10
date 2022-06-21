import { Link, useNavigate } from 'react-router-dom';
import { stateList as states } from '../helpers/states';
import { emailRegex, passRegex } from '../helpers/regex';
import { useForm } from 'react-hook-form';
import { Select, MenuItem, FormHelperText, InputLabel, Input, Button } from '@mui/material';
import { create, addUserInfo } from '../services/users';
import { useContext, useState } from 'react';
import { AuthContext } from '../context/';
import { authenticate } from '../services/authentication';
import { encrypt } from '../helpers/encryption';


export default function CreateAccount(){

    // React Form Hooks
    const { register, handleSubmit, getValues, formState: { errors }} = useForm({mode: 'onChange'});
    // Functions or states - pkey which holds the public key, encryption to encrypt the password, login to set the user.
    const { pkey, encryption, login } = useContext(AuthContext);
    // Errors in case there is a problem with the response
    const [ err, setErr] = useState(false);
    // At the very least, know which response function failed.
    const [ errInfo, setErrInfo] = useState([]);

    const navigate = useNavigate();
    const onSubmit = (data, evt) => {
        evt.preventDefault();

        // Cleaning up the data - If the string is blank, then set it to null.
        if(data.firstName ===""){
            data.firstName=null;
        }
        if(data.lastName === "" ){
            data.lastName=null;
        }

        if(data.city===""){
            data.city=null;
        }
        if(data.state===""){
            data.state=null;
        }

        //Set the email to lowercase
        data.username = data.username.toLowerCase();

        // If there aren't any errors in the form
        if(!errors.username && !errors.password && !errors.city && !errors.state){
            // First we create the user
            create(data, pkey, encryption).then( rsp => {
                // If the user is created, then we authenticate and start the session with that user.
                authenticate({username: rsp.username, password: encrypt(pkey, data.password)})
                .then(user => {
                    // If that user is authenticated, then we set the user in the context.
                    login(user);

                    let newUserInfo = {
                        appUserId: rsp.appUserId,
                        firstName: data.firstName,
                        lastName: data.lastName,
                        city: data.city,
                        state: data.state
                    }
                    // We authenticate in order to get the jwt key in order to post the new user info.
                    addUserInfo(newUserInfo).then(() => {
                        // If the userinfo posts, there are two choices really:
                        // We can logout the user and then navigate to the home page while  forcing them to login again, or we can just navigate
                        navigate('/');
                    }
                    ).catch(() =>{
                        // Just setting errors in case any of the requests fail.
                        setErr(true);
                        setErrInfo(oldInfo => [...oldInfo, "Error adding user info"]);
                     });
        }).catch( () =>{ 
            setErr(true);
            setErrInfo(oldInfo => [...oldInfo, "Error authenticating user"]);
        }
        )}).catch(() => {
            setErr(true);
            setErrInfo(oldInfo => [...oldInfo, "Error creating user"]);  
        })};


    };




    return(
        <>
            <div className="container">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div>
                        <InputLabel htmlFor="username">Email</InputLabel>
                        <Input id="username" name="username" type="email" {...register("username", {required:true, pattern: {value: emailRegex, message: "invalid email"}})} />
                        {errors.username && <FormHelperText>Please enter a valid email</FormHelperText>}
                        <InputLabel htmlFor="Password">Password</InputLabel>{}
                        <Input id="password" name="password" type="password" {...register("password", {required: true, minLength: 8, pattern: {value: passRegex, message: "invalid pass"}})} />{
                            errors.password && <FormHelperText>Please enter a valid password</FormHelperText>
                        }
                    </div>
                    <div>
                        <InputLabel htmlFor="firstName">First Name</InputLabel>
                        <Input label_id="firstName" name="firstName" {...register("firstName")} />
                        <InputLabel htmlFor="lastName">Last Name</InputLabel>
                        <Input id="lastName" name="lastName" {...register("lastName")} />
                        <InputLabel htmlFor="city">City</InputLabel>
                        <Input id="city" name="city" {...register("city", {validate: (value) => {
                            if(value){
                                if(!getValues("state")){
                                    return false;
                                }else{
                                    return true;
                                }
                            }else
                                return true;
                        }})} />{
                            errors.state && <FormHelperText>Please enter a city</FormHelperText>
                        }
                        <InputLabel htmlFor="state">State</InputLabel>
                        <Select id="state" name="state" defaultValue='' {...register("state", {validate: (value) => {
                            if(value){
                                if(!getValues("city")){
                                    return false;
                                }else{
                                    return true;
                                }
                            }else
                                return true;
                        }})}>
                        {
                            states?.map( state => {
                                return (
                                    <MenuItem key={state.key} value={state.value} >
                                        {state.text}
                                    </MenuItem>
                                );
                            })
                        }
                        </Select>{
                            errors.city && <FormHelperText>Please select a state</FormHelperText>
                        }

                    </div>
                    <Button type="submit" variant="contained" color="primary">Submit</Button>
                    <Link to="/"><Button variant="contained" color="warning">Cancel</Button></Link>
                </form>
                <div>
                    {err && errInfo.map( info => {
                        return <p>{info}</p>
                    })}
                </div>
            </div>
        </>
    )
}