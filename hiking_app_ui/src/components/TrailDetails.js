import React, { useEffect, useState, useContext } from 'react';
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { AuthContext } from '../context';
import { deleteTrail, findById } from "../services/trail";
import { findByTrail } from "../services/spot";
import { findByTrail as findPhotos } from "../services/photo";
import SpotMini from './SpotMini';
import PhotoSubmit from './PhotoSubmit';



function TrailDetails() {

    const navigate = useNavigate();
    const { trailId } = useParams();
    const { user } = useContext(AuthContext);

    const [photos, setPhotos] = useState([]);
    const [photoForm, setPhotoForm] = useState(false);


    const [trail, setTrail] = useState({
        trailId: 0,
        name: "",
        city: "",
        state: "",
        trailLength: "",
        rating: "",
        trailMap: "",
        description: "",
        appUserId: 2
    });

    const [spots, setSpots] = useState([]);
    const [formData, setFormData] = useState({
        spotId: 0,
        name: "",
        gpsLat: 0,
        gpsLong: 0,
        rating: 0,
        description: "",
        appUserId: 0,
        ratingCount: 0

    });

    const handleDelete = async (evt) => {
        evt.preventDefault();
        await deleteTrail(trailId)
            .then((resp) => { navigate(`/trails`); })
            .catch(console.error)
    }

    useEffect(() => {
        findById(trailId)
            .then(setTrail)
            .catch(() => navigate("/"));

    }, []);

    useEffect(() => {
        findByTrail(trailId)
            .then(setSpots)
            .catch(() => navigate("/"));
        findPhotos(trailId)
            .then(setPhotos)
            .catch(setPhotos([]));
    }, []);

    const togglePhotoForm = () => {
        setPhotoForm(!photoForm);
    }



    return (<div className="container">
        <br></br>
        {user && <div>
            <button type="button" className="btn btn-outline-info ml-1 mb-2" onClick={() => navigate(`/spot/add/${trailId}`)}>Add Spot</button>
            <button type="button" className="btn btn-outline-info ml-1 mb-2" onClick={() => navigate(`/trails/edit/${trailId}`)}>Edit Trail </button>
            <button className="btn btn-outline-dark ml-1 mb-2" onClick={togglePhotoForm}>Add Photo</button>
            <button type="button" className="btn btn-outline-danger ml-1 mb-2" onClick={handleDelete}>Delete Trail</button>
            {photoForm && <PhotoSubmit trailId={trail.trailId} toggleForm={togglePhotoForm} photos={photos} setPhotos={setPhotos} />}
        </div>}


        <h2 className="text-center">{trail.name}</h2>
        <hr></hr>

        {photos?.length < 1 ? <div className="text-center">
            <img src="https://hiking-app-photos.s3.amazonaws.com/5661.png" width="250" height="250" className="rounded" alt="Responsive image" />
            <p>Placeholder image: upload your own to show off this trail.</p>
            <h3>{trail.city}, {trail.state}</h3>
        </div> :
        <div className="text-center">
            <img src={photos[0]?.photoUrl} width="400" height="450" className="rounded" alt="Responsive image" />
            <h3>{trail.city}, {trail.state}</h3>
        </div>}
        <br></br>

        <h5 style={{ marginLeft: 180 }}>Difficulty - {trail.rating} </h5>
        <h5 style={{ marginLeft: 180 }}>Length - {trail.trailLength} miles </h5>
        <br></br>
        <h3 style={{ marginLeft: 180 }}>Description:</h3>
        <h5 style={{ marginLeft: 180 }}>{trail.description} </h5>
        


        <div className="container">
            <h2 className="text-center">Spots</h2>
            <hr></hr>
            <br></br>

            <div className="row g-1">

                {
                    spots.map(a => {
                        return (
                            <div className="col border mt-md-4" onClick={() => navigate(`/spot/${a.spotId}`)}>
                                <SpotMini spot={a} />
                            </div>)
                    })
                }

            </div>
        </div>


    </div>);
}
export default TrailDetails;