import { useState, useEffect } from "react";
import { findAll } from "../services/trail";
import TrailMini from "./TrailMini";
import { stateList as states } from "../helpers/states";
import { useForm } from "react-hook-form";
import { Select, MenuItem, InputLabel, Button } from "@mui/material";


export default function Home() {

    const [trails, setTrails] = useState([]);
    const [searched, setSearched] = useState(false);
    const [input, setInput] = useState("");
    const [filtered, setFiltered] = useState([]);

    useEffect(() => {
        findAll()
            .then(setTrails)
            .catch(console.error);

    }, [])

    const { register, handleSubmit } = useForm();

    const onSubmit = (data, evt) => {
        evt.preventDefault();

        console.log(data.state)
    };



    return (
        <div className="container">
            <div className="row">
                <div className="col" style={{ textAlign: "center" }}>
                    <h1>Hiking Home / ハイキングホーム</h1>
                </div>
            </div>

            <form onSubmit={handleSubmit(onSubmit)}>
                <div className="form-row justify-content-center">
                    <div className="col-2 d-flex align-self-center">
                        <InputLabel htmlFor="state" className="p-1">State</InputLabel>
                        <Select id="state" name="state" defaultValue={states[0].key} {...register("state")}>
                            {states.map(state => {
                                return (
                                    <MenuItem key={state.key + 'h'} value={state.value} >
                                        {state.text}
                                    </MenuItem>
                                );
                            }
                            )}
                        </Select>
                    </div>
                    <div className="col-1 d-flex align-self-center">
                        <Button type="submit" variant="contained" color="inherit">Search</Button>
                    </div>
                </div>
            </form>


            <div>
                {searched && <div>{filtered.map(i => {
                    return (<TrailMini trail={i} key={i?.trailId} />)
                })}</div>}
            </div>




        </div>
    )
}