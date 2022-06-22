import {useState, useEffect} from 'react';
import { findByTrail } from '../services/photo';
import { useNavigate } from 'react-router-dom';

export default function TrailMini(trail) {

    const [photos, setPhotos] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        findByTrail(trail?.trail?.trailId)
            .then(setPhotos)
            .catch(console.error);
    }, [trail])

    return(
        <div className="row border mt-md-4" onClick={() => navigate(`/trails/${trail?.trail?.trailId}`)}>
            <div className="col-auto ">
            <div className="card-header"> </div>

            <img className="card-img-left" height="200" width="300" src={photos[0]?.photoUrl}  /> 
            </div>
            <div className="col">
            <div className="card-body">
            <h4 className="card-title">{trail?.trail?.name}</h4>
            <h3>{trail?.trail?.city} {trail?.trail?.state}</h3>
         
            </div>
            </div>
            </div>
    )
}