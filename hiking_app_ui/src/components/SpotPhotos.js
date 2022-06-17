import { findBySpot} from "../services/photo";
import { findById } from "../services/spot";
import { useState, useEffect, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AuthContext } from "../context";


export default function SpotPhotos() {

    const [photos, setPhotos] = useState([]);
    const [spot, setSpot] = useState();
    const navigate = useNavigate();
    const {spotId} = useParams();
    const {user} = useContext(AuthContext);

    useEffect(() => {
        findById(spotId)
            .then(setSpot)
            .catch(console.error);

        findBySpot(spotId)
            .then(setPhotos)
            .catch(console.error);
    }, [spotId]);

    return (<div className="container">
        <div className="row align-items-center">
            <div className="col"  style={{textAlign: "center"}}>
                <h1>{spot?.name}: Photos</h1>
            </div>
        </div>
        <div className="row g-1">
            {photos?.map(i => {
                return (
                <div key={i.photoId} className="col" style={{marginTop: 2, marginBottom: 2}}>
                    <img src={i.photoUrl} width="425" height="300" />
                </div>)
            })}
        </div>
        <div className="row">
            <div className="col-10"></div>
            <div className="col-2">
                {!user && <button className="btn btn-outline-dark">Add Photo</button>}
            </div>
        </div>
    </div>)
    
}