import { useNavigate, useParams } from 'react-router-dom';

import { useEffect, useState, useContext } from 'react';
import Map from './Map';
import {useLoadScript} from '@react-google-maps/api';
import {findById as findTrail} from "../services/trail";
import {addSpot} from '../services/spot';
import { AuthContext } from '../context';
import { getId } from '../services/users';

export default function SpotForm() {
    const {trailId} = useParams();
    const navigate = useNavigate();
    const [trail, setTrail] = useState();
    const [userId, setUserId] = useState();
    const {user} = useContext(AuthContext);
    const [spot, setSpot] = useState({
        spotId: 0,
        name: "",
        gpsLat: 0,
        gpsLong: 0,
        rating: 5,
        description: "",
        appUserId: 0,
        ratingCount: 1,
        trails: [],  
    });

    const [spotMarker, setSpotMarker] = useState();

    const {isLoaded} = useLoadScript({
        googleMapsApiKey: "",
      })


      useEffect(() => {
            getId(user?.sub)
                .then(i => setUserId(i));
            findTrail(trailId)
                .then(t => setTrail(t))
                .catch()
        }, [trailId, user?.sub]);


    const onMapClicked = (evt) => {
        
        setSpotMarker({lat: evt?.latLng.lat(), lng: evt?.latLng.lng()});
        const newSpot = {...spot};
        newSpot.gpsLat = evt.latLng.lat();
        newSpot.gpsLong = evt.latLng.lng();
        setSpot(newSpot);
    }

    const handleChange = (evt) => {
        const newSpot = {...spot};
        console.log(userId);
        newSpot[evt.target.name] = evt.target.value;
        newSpot.appUserId = userId;
        const trails = [trail];
        newSpot.trails = trails;
        console.log(spot);
        setSpot(newSpot);
    }

    const handleSubmit = async (evt) => {
        evt.preventDefault();
       
        await addSpot(spot)
            .then(setSpot)
            .then()
            .catch(console.error)  
        if(spot.spotId !== 0) {
            navigate(`/trail/${trailId}`)
        }    

    }


      if(!isLoaded) return <div>Loading...</div>

    return (
        <div className="container">
            <div className="row" style={{ margin: 3 }}>
                <div className="col" style={{ textAlign: "center" }}>
                    <h1>Create new spot for {trail?.name} </h1>
                </div>
            </div>

            <div className="row">
                <div className="col">
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Spot Name</label>
                            <input className="form-control" type="text" id="name"
                                    name="name" placeholder="Spot name" value={spot.name} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="description">Description</label>
                            <input className="form-control" type="text" id="description"
                                    name="description" placeholder='Description' value={spot.description} onChange={handleChange}required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="gpsLat">Latitude</label>
                            <input className="form-control" type="text" id="gpsLat"
                                    name="gpsLat" placeholder='Latitude' value={spot.gpsLat} onChange={handleChange}required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="gpsLat">Longitude</label>
                            <input className="form-control" type="text" id="gpsLong"
                                    name="gpsLong" placeholder='Longitude' value={spot.gpsLong} onChange={handleChange}required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="rating">Initial rating</label>
                            <input type="number" className="form-control" id="rating"
                                    name="rating" placeholder='Rating' value={spot.rating} onChange={handleChange}required />
                        </div>

                        <button type="submit" className="btn btn-outline-dark" style={{ margin: 3 }}>Submit</button>
                    </form>
                </div>
                <div className="col" style={{ textAlign: "center" }}>
                <h3>Select spot location on map:</h3>
                    <Map mapString={trail?.trailMap} onMapClicked={onMapClicked} spotMarker={spotMarker} />
                </div>
            
                
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