import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { AuthContext } from '../context';
import { findById } from "../services/trail";



function TrailDetails() {

    const search = useLocation().search;
   //– const trailId = new URLSearchParams(search).get('trailId');

    const navigate = useNavigate();
    const { trailId } = useParams();


    const [trail, setTrail] = useState({
        trailId:0,
        name:"",
        city:"",
        state:"",
        trailLength:"",
        rating:"",
        trailMap:"",
        description:"",
        appUserId:2
    });

    useEffect(() => {
        findById(trailId)
            .then(setTrail)
            .catch(() => navigate("/"));

    }, []);

    
    
    return(<div className="container">
        <br></br>
        <button type="button" class="btn btn-outline-info ml-1">Add Spot</button>
        <button type="button" class="btn btn-outline-info ml-1">Edit Trail </button>
        <button type="button" class="btn btn-outline-danger ml-1">Delete Trail</button>
        
    <h2 className="text-center">{trail.name}</h2> 
    <hr></hr>

    <div className="text-center">
  <img src="https://picsum.photos/800/400" class="rounded" alt="Responsive image"/>
    </div>
    <h3 style={{marginLeft:180}} >{trail.city}, {trail.state}</h3>

    <br></br>

    <h5 style={{marginLeft:180}}>Difficulty - {trail.rating} </h5> 
    <h5 style={{marginLeft:180}}>Lenght - {trail.trailLength} miles </h5> 
    <br></br>
    <h2 style={{marginLeft:180}}>Description:</h2>
    <h5 style={{marginLeft:180}}>{trail.description} </h5>
    

   
 

    





         </div> );
}
export default TrailDetails;