import { useState, useEffect, useContext } from 'react';
import { Link, useNavigate, useParams } from "react-router-dom";
import { findById } from "../services/spot";
import { findBySpot } from "../services/photos";

export default function Spot() {

    const [spot, setSpot] = useState({
        spotId: 0,
        name: "",
        gpsLat: 0,
        gpsLong: 0,
        rating: 0,
        description: "",
        appUserId: 0,
        ratingCount: 0,
        trails: []
    });

    const [photos, setPhotos] = useState([]);

    const navigate = useNavigate();

    const { spotId } = useParams();

    useEffect(() => {
        findById(spotId)
            .then(setSpot)
            .catch(() => navigate("/"));

        findBySpot(spotId)
            .then(setPhotos)
            .catch(console.error);
    }, []);


    return (<div>
        <div className="container">

            <div className="row align-items-center" style={{ marginBottom: 7 }}>
                <div className="col-4" style={{ marginTop: 2, padding: 10, border: "2px solid black" }}>
                    <h1>{spot.name}</h1>
                    <h2>{spot.description}</h2>
                    <p>Located {spot.gpsLat} X {spot.gpsLong}</p>
                    <h3>User rating: {spot.rating}/5</h3>
                    <p>Based on {spot.ratingCount} ratings</p>
                    <p>Added by {spot.uploader?.firstName} from {spot.uploader?.city}, {spot.uploader?.state}</p>
                </div>
                <div className="col" style={{ marginTop: 2, padding: 10 }}>
                    {photos[0] &&
                        <img src={photos[0].photoUrl}
                            className="w-100 shadow-1-strong rounded mb-4" />}
                </div>
            </div>

            <div className="row align-items-center" style={{ marginTop: 2, padding: 10, border: "2px solid black" }}>
                {photos.filter((i,x) => i.photoId !== photos[0].photoId && x < 5).map(i => {
                    return (
                        <div key={i.photoUrl} className="col">
                            <img src={i.photoUrl} className="w-100 shadow-1-strong rounded mb-4" />
                        </div>
                    )
                })}

            </div>


            
                {photos.length > 2 && 
                <div className="row" style={{marginTop: 2}}>
                    <div className="col-5"></div>
                    <div className="col">
                        <button className="btn btn-outline-secondary">More photos</button>
                    </div>
                </div>}   
            
        </div>

    </div>);
}