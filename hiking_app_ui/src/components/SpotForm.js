import { useNavigate, useParams } from 'react-router-dom';

import { useEffect, useState, useContext } from 'react';
import Map from './Map';
import {useLoadScript} from '@react-google-maps/api';
import {findById as findTrail} from "../services/trail";
import {addSpot} from '../services/spot';
import { UserContext, AuthContext } from '../context';
import { getId } from '../services/users';

export default function SpotForm() {
    const {trailId} = useParams();
    const navigate = useNavigate();
    const [trail, setTrail] = useState();
    const [appUserId, setAppUserId] = useState();
    const {user} = useContext(AuthContext);
    const [spot, setSpot] = useState({
        appUserId: 4,
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
        googleMapsApiKey: "AIzaSyB1xlfgZo3hoQ9jkTzbtokiVp6nIeLw9vY",
      })


      useEffect(() => {
            getId(user?.sub)
                .then(setAppUserId);
            findTrail(trailId)
                .then(setTrail)
                .catch()
        }, []);


    const onMapClicked = (evt) => {
        setSpotMarker({lat: evt?.latLng.lat(), lng: evt?.latLng.lng()});
        const newSpot = {...spot};
        newSpot.gpsLat = evt.latLng.lat();
        newSpot.gpsLong = evt.latLng.lng();
        setSpot(newSpot);
    }

    const handleChange = (evt) => {
        const newSpot = {...spot};
        console.log(spot);
        newSpot[evt.target.name] = evt.target.value;
        newSpot.appUserId = appUserId;
        const trails = [trail];
        newSpot.trails = trails;
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

    const sendRequests = async () => {

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
                
            </div>
            <div className="row">
                <div className="col" style={{ textAlign: "center" }}>
                    <h3>Select spot location on map:</h3>
                </div>
                
            </div>
            <div className="row" style={{ margin: 3 }}>
            <div className="col">
                    <Map mapString={trail?.trailMap} onMapClicked={onMapClicked} spotMarker={spotMarker} />
                </div>
            </div>



        </div>
    )
}