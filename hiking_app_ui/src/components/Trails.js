import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

function Trails() {
    const [trails, setTrails] = useState([]);
    const [editIndex, setEditIndex] = useState();
    const [formData, setFormData] = useState({
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

    useEffect(()=>{
        //make a get all request
        fetch("http://localhost:8080/api/trail")
        .then(resp=>resp.json())
        .then(data=> {
   
            setTrails(data); 
        })
    }, [] );

     const navigate = useNavigate();
    // const displayTrail = () => navigate('/trailDetails');

    
    return(<div className="container">
    <h2 className="text-center">Trails</h2> 
    <hr></hr>
    <button type="button" class="btn btn-outline-info ml-1" onClick={() => navigate(`/trails/add`)}>Add Trail </button>
    <br></br>
   
         <div className="mt-2">

             
        
         {
             
         trails.map(a => {
           return(
          

            

            <div className="row border mt-md-4" onClick={() => navigate(`/trails/${a.trailId}`)}>
            <div className="col-auto ">
            <div className="card-header"> </div>

            <img className="card-img-left" src="https://picsum.photos/300/200"   /> 
            </div>
            <div className="col">
            <div className="card-body">
            <h4 className="card-title">{a.name}</h4>
            <h3>{a.city} {a.state}</h3>
         
            </div>
            </div>
            </div>
  

           )
         })
         }
         
         </div>
         </div> );
    


}
 export default Trails;

 