import { useState, useEffect, useContext } from 'react';
import { Link, useNavigate, useParams } from "react-router-dom";
import { findById, rateSpot, findByTrail } from "../services/spot";
import { findBySpot } from "../services/photo";
import { AuthContext } from '../context';
import PhotoSubmit from './PhotoSubmit';

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

    const [rateMode, setRateMode] = useState(false);

    const [message, setMessage] = useState();

    const [rating, setRating] = useState(1);

    const [photoForm, setPhotoForm] = useState(false);

    const [uploaded, setUploaded] = useState(false);

    const { user } = useContext(AuthContext);

    const [photos, setPhotos] = useState([]);
    const [editMode, setEditMode] = useState(false);

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

    const toggleRate = () => {
        setRateMode(!rateMode);
    }

    const handleChange = (evt) => {
        setRating(evt.target.value);
    }

    const handleSubmit = (evt) => {
        evt.preventDefault();
        rateSpot(spotId, rating)
            .then(setSpot)
            .catch(setMessage("Unable to rate spot. Please try again."));

        toggleRate();
    }

    const toggleForm = () => {
        setPhotoForm(!photoForm);
    }

    const toggleEdit = () => {
        setEditMode(!editMode);
    }
    return (<div>
        <div className="container">

            <div className="row align-items-center" style={{ marginBottom: 7 }}>
                <div className="col-6" style={{ marginTop: 2, padding: 10, border: "2px solid black" }}>
                    <h1>{spot.name}</h1>
                    <h3>{spot.description}</h3>
                    <h3>On trail: {spot.trails[0]?.name}</h3>
                    <p>Coordinates: {spot.gpsLat} X {spot.gpsLong}</p>

                    <h3>User rating: {spot.rating}/5</h3>
                    <p>Based on {spot.ratingCount} ratings</p>
                    {user &&
                        <div>{rateMode ?
                            <form onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="rating" style={{ marginRight: 1 }}>Rating:</label>
                                    <input type="number" id="rating" name="rating"
                                        min="1" max="5" value={rating} onChange={handleChange} />
                                    <small id="ratingInfo" className="form-text text-muted">Out of 5</small>
                                </div>
                                <button type="submit" className="btn btn-outline-dark">Submit rating</button>
                            </form> :

                            <button className="btn btn-outline-dark" onClick={toggleRate}>Rate spot</button>}
                        </div>}
                    <small>Added by {spot.uploader?.firstName} from {spot.uploader?.city}, {spot.uploader?.state}</small>
                </div>
                <div className="col" style={{ marginTop: 2, padding: 10 }}>
                    {photos[0] &&
                        <img src={photos[0].photoUrl}
                            className="shadow-1-strong rounded mb-4" height="400" width="460" 
                            alt={`Main photo for spot ${spotId}`} />}
                </div>
            </div>

            <div className="row align-items-center" style={{ marginTop: 2, padding: 10, border: "2px solid black" }}>
                {photos.filter((i, x) => x > 0 && x < 5).map(i => {
                    return (
                        <div key={i.photoUrl} className="col">
                            <img src={i.photoUrl} className="shadow-1-strong rounded mb-4" 
                            height="200" width="200" 
                            alt={`Additional photo for spot ${spotId}`}/>
                        </div>
                    )
                })}

            </div>



            <div className="row" style={{ marginTop: 2 }}>
                <div className="col-5">
                    <div className="float-left">
                            <button className="btn btn-outline-secondary" onClick={toggleEdit}>Edit spot</button>
                    </div>  
                </div>
                {(user && photoForm) ?
                    <div className="row" style={{ marginTop: 2 }}>
                        <PhotoSubmit spotId={spot.spotId} toggleForm={toggleForm} photos={photos} setPhotos={setPhotos} />
                    </div> :
                    <div className="col-5">
                        <button className="btn btn-outline-dark" onClick={toggleForm}>Add photo</button>
                    </div>}

                {photos.length > 5 &&
                    <div className="col">
                        <div className="float-right">
                            <Link className="btn btn-outline-secondary" to={`/spot/photo/${spotId}`}>More photos</Link>
                        </div>
                    </div>}
            </div>


            {user?.authorities === "ADMIN" &&
                <div className="row">
                    <div className="col-10"></div>
                    <div className="col">
                        <div className="float-right">
                            <buton className="btn btn-danger">Delete Spot</buton>
                        </div>
                    </div>
                </div>}

        </div>

        <div className="row">
            {editMode && <div></div>}
        </div>

        

    </div>);
}