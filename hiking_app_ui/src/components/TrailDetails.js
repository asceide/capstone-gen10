import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { AuthContext } from '../context';
import { deleteTrail, findById } from "../services/trail";
import { findByTrail } from "../services/spot";



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

    const [spots, setSpots] = useState([]);
        const [formData, setFormData] = useState({
        spotId: 0,
        name: "",
        gpsLat: 0,
        gpsLong: 0,
        rating: 0,
        description: "",
        appUserId: 0,
        ratingCount: 0
        
    });

    const handleDelete = async (evt) => {
        evt.preventDefault();
        await deleteTrail(trailId)
        .then((resp) => {navigate(`/trails`);})
        .catch(console.error)
    }

    useEffect(() => {
        findById(trailId)
            .then(setTrail)
            .catch(() => navigate("/"));

    }, []);

    useEffect(() => {
        findByTrail(trailId)
            .then(setSpots)
            .catch(() => navigate("/"));

    }, []);

    
    
    return(<div className="container">
        <br></br>
        <button type="button" class="btn btn-outline-info ml-1" onClick={() => navigate(`/spot/add/${trailId}`)}>Add Spot</button>
        <button type="button" class="btn btn-outline-info ml-1" onClick={() => navigate(`/trails/edit/${trailId}`)}>Edit Trail </button>
        <button type="button" class="btn btn-outline-danger ml-1" onClick={handleDelete}>Delete Trail</button>
        
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
    <h3 style={{marginLeft:180}}>Description:</h3>
    <h5 style={{marginLeft:180}}>{trail.description} </h5>
    <h3 style={{marginLeft:180}}>Location:</h3>
  

   <div className="container">
    <h2 className="text-center">Spots</h2> 
    <hr></hr>
    <br></br>
   
         <div className="mt-2">

             
        
         {
             
         spots.map(a => {
           return(
          

            

            <div className="row border mt-md-4" onClick={() => navigate(`/spot/${a.spotId}`)}>
            <div className="col-auto ">
            <div className="card-header"> </div>

            <img className="card-img-left" src="https://picsum.photos/300/200"   /> 
            </div>
            <div className="col">
            <div className="card-body">
            <h4 className="card-title">{a.name}</h4>
            <h3>{a.rating}</h3>
         
            </div>
            </div>
            </div>
  

           )
         })
         }
         
         </div>
         </div> );


         </div> );
}
export default TrailDetails;