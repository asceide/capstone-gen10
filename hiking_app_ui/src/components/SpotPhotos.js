import { findBySpot} from "../services/photo";
import { findById } from "../services/spot";
import { useState, useEffect, useContext } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { AuthContext } from "../context";
import PhotoSubmit from "./PhotoSubmit";


export default function SpotPhotos() {

    const [photos, setPhotos] = useState([]);
    const [spot, setSpot] = useState();
    const navigate = useNavigate();
    const {spotId} = useParams();
    const {user} = useContext(AuthContext);
    const [photoForm, setPhotoForm] = useState(false);

    useEffect(() => {
        findById(spotId)
            .then(setSpot)
            .catch(console.error);

        findBySpot(spotId)
            .then(setPhotos)
            .catch(console.error);
    }, [spotId]);

    const toggleForm = () => {
        setPhotoForm(!photoForm);
    }

    return (<div className="container">
        <div className="row align-items-center">
            <div className="col-1">
                <Link className="btn btn-outline-dark" to={`/spot/${spotId}`}>Spot {spotId}</Link>
            </div>
            <div className="col"  style={{textAlign: "center"}}>
                <h1>{spot?.name}: Photos</h1>
            </div>
           
            {user && 
                <div className="col-1">
                    <div className="float-right">
                        <button className="btn btn-outline-dark" onClick={toggleForm}>Add Photo</button>
                    </div>
                </div>
            }
            
            
        </div>
        <div className="row g-1">
            {photos?.map(i => {
                return (
                <div key={i.photoId} className="col" style={{marginTop: 2, marginBottom: 2}}>
                    <div className="float-center">
                        <img src={i.photoUrl} width="350" height="300" alt={`Photo of spot ${spotId}`}/>
                    </div>
                    
                </div>)
            })}
        </div>
        <div className="row">
            <div className="col-10"></div>
            <div className="col-2">
                {user && <button className="btn btn-outline-dark" onClick={toggleForm}>Add Photo</button>}
            </div>
        </div>
        <div className="row">
            {photoForm && 
            <PhotoSubmit spotId={spotId} toggleForm={toggleForm} photos={photos} setPhotos={setPhotos} />
            }
            
        </div>
    </div>)
    
}