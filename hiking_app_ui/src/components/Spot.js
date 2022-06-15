import {useState, useEffect, useContext} from 'react';
import {Link, useNavigate, useParams } from "react-router-dom";
import {findById} from "../services/spot";
import {findBySpot} from "../services/photos";

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
    }, [spotId, navigate]);

    return (<div>
        <div className="container">
            <h1>{spot.name}</h1>
            <h2>{spot.description}</h2>
            {photos.map(i => {
                return <h3>{i.photoUrl}</h3>
            })}
        </div>
        
    </div>);
}