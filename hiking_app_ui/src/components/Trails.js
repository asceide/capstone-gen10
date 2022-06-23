import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import TrailMini from './TrailMini';
import {findAll} from "../services/trail";

function Trails() {
    const [trails, setTrails] = useState([]);

    useEffect(() => {
        //make a get all request
        findAll()
            .then(setTrails)
            .catch(setTrails([]));
    }, []);

    const navigate = useNavigate();


    return (<div className="container">
        <h2 className="text-center">Trails</h2>
        <hr></hr>
        <button type="button" className="btn btn-outline-info ml-1" onClick={() => navigate(`/trails/add`)}>Add Trail </button>
        <br></br>

        <div className="mt-2">



            {

                trails.map(a => {
                    return (
                        <div>
                            <TrailMini key={a?.trailId} trail={a} />
                        </div>


                    )
                })
            }

        </div>
    </div>);



}
export default Trails;

