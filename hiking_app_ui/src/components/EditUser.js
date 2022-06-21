import { useContext, useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { Select, MenuItem, FormHelperText, InputLabel, Input, Button, CircularProgress } from '@mui/material';
import { UserContext, AuthContext } from '../context';
import { stateList as states } from '../helpers/states';
import { editUserInfo } from '../services/users';


export default function EditUser(){

    const { userInfo, update } = useContext(UserContext);
    const { user } = useContext(AuthContext);
    const [ err, setErr] = useState(false);
    const [errInfo, setErrInfo] = useState([])
    const [info, setInfo ] = useState({});
    const [showSpinner, setShowSpinner] = useState(false);
    const { register, handleSubmit, getValues, formState: { errors }} = useForm({mode: 'onChange'});
    const navigate = useNavigate();

    useEffect(() => {
        setInfo(userInfo);
    }
    , [userInfo]);

    useEffect( () => {
        if(!user){
            setTimeout( () => {
                setShowSpinner(true);
            }, 5000)
        }
    }, [user]);
    
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

        let newData = {...data, appUserId: userInfo.appUserId};

        if(!errors.city && !errors.state){
            editUserInfo(newData).then(rsp => {
                setErr(false);
                setErrInfo([]);
                update(newData);
                navigate('/');
            }).catch(() => {
                setErr(true);
                setErrInfo(["There was an error editing your information."]);
            })
        }
    }

    const editForm = () => {
        return (

            <div className="container">
            {
                user ? <h3>Username: {user.sub}</h3> : <><h3>Username:</h3> <CircularProgress color="secondary" /></>
            }

            <form onSubmit={handleSubmit(onSubmit)}>
            <div>
                <InputLabel htmlFor="firstName" >First Name</InputLabel>
                <Input id="firstName" name="firstName" defaultValue={userInfo.firstName} {...register("firstName")}/>
                <InputLabel htmlFor="lastName">Last Name</InputLabel>
                <Input id="lastName" name="lastName" defaultValue={userInfo.lastName} {...register("lastName")}/>
                <InputLabel htmlFor="city">City</InputLabel>
                <Input id="city" name="city" defaultValue={userInfo.city} {...register("city", {validate: (value) => {
                    if(value){
                        if(!getValues("state")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }else
                        return true;
                }})} />{
                    errors.state && <FormHelperText>Please enter a city</FormHelperText>
                }
                <InputLabel htmlFor="state">State</InputLabel>
                <Select id="state" name="state" defaultValue={userInfo.state} {...register("state", {validate: (value) => {
                    if(value){
                        if(!getValues("city")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }else
                        return true;
                }})}>{
                    states?.map( state => {
                        return (
                            <MenuItem key={state.key} value={state.value} >
                                {state.text}
                            </MenuItem>
                        );
                })}
                </Select>{
                    errors.city && <FormHelperText>Please select a state</FormHelperText>
                }
                </div>
                <Button type="submit" variant="contained" color="primary">Submit</Button>
                <Link to="/"><Button variant="contained" color="warning">Back</Button></Link>
            </form>
        </div>
        )
    }

    return (
        <div>
            <h1>Edit User</h1>{
                userInfo? editForm() : <>
                <CircularProgress color="secondary" />{
                    showSpinner ? <h4>Server response is slow or you're not logged in!</h4> : <></>
                }
                </>
            }
            <div>
                {err && errInfo.map(errin => {
                    return <p>{errin}</p>
                })}
            </div>
        </div>
    )

}