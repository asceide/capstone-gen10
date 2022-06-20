import { useNavigate, useParams } from 'react-router-dom';

import { useEffect, useState } from 'react';
import Map from './Map';
import {useLoadScript} from '@react-google-maps/api';
import {findById as findTrail} from "../services/trail";
import { findById as findSpot, findByTrail } from '../services/spot';

export default function SpotForm() {



    const { trailId, spotId } = useParams();
    const navigate = useNavigate();
    const [trail, setTrail] = useState();
    const [spot, setSpot] = useState();

    const {isLoaded} = useLoadScript({
        googleMapsApiKey: "",
        libraries: ["places"],
      })


      useEffect(() => {
        if(spotId) {
            findSpot(spotId)
                .then(setSpot)
                .then(findTrail(spot?.trails[0]?.trailId).then(setTrail))
                .catch(navigate("/"))
        } else {
            findTrail(trailId)
            .then(setTrail)
            .catch()
        }
        }, []);

        

      if(!isLoaded) return <div>Loading...</div>



    return (
        <div className="container">
            <div className="row" style={{ margin: 3 }}>
                <div className="col" style={{ textAlign: "center" }}>
                    <h1>Create new spot for trail {trailId}</h1>
                </div>
            </div>

            <div className="row">
                <div className="col">
                    <form>
                        <div className="form-row" style={{ margin: 3 }}>
                            <div className="col-4">
                                <input className="form-control" type="text" id="spotName"
                                    name="spotName" placeholder="Spot name" required />
                            </div>
                            <div className="col">
                                <input className="form-control" type="text" id="spotDescription"
                                    name="spotDescription" placeholder='Description' required />
                            </div>
                        </div>

                        <div className="form-row" style={{ margin: 3 }}>
                            <div className="col-4">
                                <input className="form-control" type="text" id="spotLat"
                                    name="spotLat" placeholder='Latitude' required />
                            </div>
                            <div className="col-4">
                                <input className="form-control" type="text" id="spotLong"
                                    name="spotLong" placeholder='Longitude' required />
                            </div>

                            <div className="col">
                                <input type="number" className="form-control" id="rating"
                                    name="rating" placeholder='Rating' required />
                            </div>
                            <div className="form-row" style={{ margin: 3 }}>
                                <div className="col">
                                    <label htmlFor="file">Select photo to upload</label>
                                    <input type="file" id="file" name="file" accept="image/*"
                                        className="form-control" required />
                                </div>
                            </div>

                        </div>
                        <button type="submit" className="btn btn-outline-dark">Submit</button>

                    </form>
                </div>
                
            </div>
            <div className="row">
            <div className="col">
                    <Map />
                </div>
            </div>



        </div>
    )
}