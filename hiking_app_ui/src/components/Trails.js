import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import TrailMini from './TrailMini';

function Trails() {
    const [trails, setTrails] = useState([]);

    useEffect(()=>{
        //make a get all request
        fetch("http://localhost:8080/api/trail")
        .then(resp=>resp.json())
        .then(data=> {
   
            setTrails(data); 
        })
    }, [] );

     const navigate = useNavigate();

    
    return(<div className="container">
    <h2 className="text-center">Trails</h2> 
    <hr></hr>
    <button type="button" class="btn btn-outline-info ml-1" onClick={() => navigate(`/trails/add`)}>Add Trail </button>
    <br></br>
   
         <div className="mt-2">

             
        
         {
             
         trails.map(a => {
           return(
            <div> 
                <TrailMini key={a?.trailId} trail={a} /> 
            </div>
  

           )
         })
         }
         
         </div>
         </div> );
    


}
 export default Trails;

 