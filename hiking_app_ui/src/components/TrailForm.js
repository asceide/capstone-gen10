import { useNavigate, useParams } from 'react-router-dom';

import { useEffect, useState, useContext } from 'react';
import TrailMap from './TrailMap';
import {useLoadScript} from '@react-google-maps/api';
import {findById as findTrail} from "../services/trail";
import {addTrail, updateTrail} from '../services/trail';
import { AuthContext } from '../context';
import { getId } from '../services/users';

export default function TrailForm() {

    const {trailId} = useParams();
    const title = (trailId !== undefined) ? 'Edit Trail':'Add Trail';
   
    
    const navigate = useNavigate();
    const [userId, setUserId] = useState();
    const {user} = useContext(AuthContext);
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

    const states = [ 'AL', 'AK', 'AS', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'DC', 'FM', 'FL', 'GA', 'GU', 'HI', 'ID', 'IL', 'IN', 'IA',
     'KS', 'KY', 'LA', 'ME', 'MH', 'MD', 'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'MP', 'OH', 
     'OK', 'OR', 'PW', 'PA', 'PR', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VI', 'VA', 'WA', 'WV', 'WI', 'WY' ];

     const values = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20']

    const [trailMarkers, setTrailMarkers] = useState([]);

    const {isLoaded} = useLoadScript({
        googleMapsApiKey: "",
      })


      useEffect(() => {
            getId(user?.sub)
                .then(i => setUserId(i));
                if (trailId !== undefined) {
            findTrail(trailId)
                .then(t => setTrail(t))
                
                .catch()
                }
        }, [trailId, user?.sub]);


    const onMapClicked = (evt) => {
        const updatedMarkers = [...trailMarkers];
        updatedMarkers.push({lat: evt.latLng.lat(), lng: evt.latLng.lng()});
        setTrailMarkers(updatedMarkers);
        makeMapString(updatedMarkers);   
    }

    const handleChange = (evt) => {
        const newTrail = {...trail};
        newTrail[evt.target.name] = evt.target.value;
        newTrail.appUserId = userId;
        setTrail(newTrail);
    }

    const handleClear = () => {
        setTrailMarkers([]);
    }

    const makeMapString = (markers) => {
        const newTrail = {...trail};
        if(markers.length > 0) {
            let mapString = "";
            for (let i = 0; i < markers.length; i++) {
                mapString = mapString + markers[i].lat + "," + markers[i].lng + ";";
            }
            newTrail.trailMap = mapString;
            setTrail(newTrail);
        }
    }


    const handleSubmit = async (evt) => {
        evt.preventDefault();
        makeMapString(trailMarkers);
        if(trailId !== undefined) {
          await updateTrail(trail)
          .then((resp) => {navigate(`/trails`);})
          .catch(console.error)
        } else {
        await addTrail(trail)
            .then((resp) => {navigate(`/trails/${resp.trailId}`);})
            .catch(console.error) 
        } 
    }


      if(!isLoaded) return <div>Loading...</div>

    return (
        <div className="container">
            <div className="row" style={{ margin: 3 }}>
                <div className="col" style={{ textAlign: "center" }}>
                    <h1>{title} </h1>
                </div>
            </div>

            <div className="row">
                <div className="col">
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Trail Name</label>
                            <input className="form-control" type="text" id="name"
                                    name="name" placeholder="Trail name" value={trail.name} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="city">City</label>
                            <input className="form-control" type="text" id="city"
                                    name="city" placeholder='City' value={trail.city} onChange={handleChange}required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="state">State</label>            
                            <select className="form-control" type="text" id="state" name="state" placeholder='State' value={trail.state} onChange={handleChange}required>
                                      {states.map(val => <option value={val}>{val}</option>)}
                             </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="trailLength">Trail Length</label>            
                            <select className="form-control" type="text" id="trailLength" name="trailLength" placeholder='trailLength' value={trail.trailLength} onChange={handleChange}required>
                                      {values.map(val => <option value={val}>{val}</option>)}
                             </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="rating">Difficulty</label>            
                            <select className="form-control" type="text" id="rating" name="rating" placeholder='rating' value={trail.rating} onChange={handleChange}required>
                              <option>Easy</option>
                              <option>Medium</option>
                              <option>Hard</option>
                            </select>
                        </div>



                        <div className="form-group">
                            <label htmlFor="description">Description</label>
                            <textarea className="form-control" type="text" id="description"
                                    name="description" placeholder='Description' value={trail.description} onChange={handleChange}required />
                        </div>
                        
                    

                        <button type="submit" className="btn btn-outline-dark" style={{ margin: 3 }}>Submit</button>   
                    </form>
                    <button className="btn btn-outline-dark" onClick={handleClear}>Clear Map</button>
                    </div>
                {!trailId &&
                <div className="col" style={{ textAlign: "center" }}>
                <h3>Map the trail:</h3>
                <small>Click the map to add markers</small>
                    <TrailMap onMapClicked={onMapClicked} trailMarkers={trailMarkers} />
                </div>
            }
                
                
            
                
            </div>
            <div className="row">
                <div className="col" style={{ textAlign: "center" }}>
                    
                </div>
                
            </div>
            <div className="row" style={{ margin: 3 }}> 
            </div>


        </div>
    )
}