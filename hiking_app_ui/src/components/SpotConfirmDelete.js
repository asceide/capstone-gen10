import {useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import {findById, deleteSpot} from "../services/spot";

export default function SpotConfirmDelete() {

    const [errs, setErrs] = useState();
    const [currentSpot, setCurrentSpot] = useState({
        name: ""
    })
    const {spotId} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if(spotId) {
            findById(spotId)
                .then(resp => setCurrentSpot(resp))
                .catch(navigate("/"));
        } else {
            navigate("/");
        }
    }, [spotId, navigate]);

    const handleDelete = () => {
        deleteSpot(currentSpot.spotId)
            .then(navigate("/"))
            .catch(err => setErrs([err]));
    };


    return (
        <div>
            <h1>Delete {currentSpot.name}?</h1>
            <div className="alert-danger">
                <p>All data will be lost</p>
            </div>
            {errs.length > 0 && <div className="alert alert-danger mt-2">
                <ul>
                    {errs.map(err => <li key={err}>{err}</li>)}
                </ul>
            </div>}
            <div>
                <button className="btn btn-danger" onClick={handleDelete}>Yes, Delete</button>
                <Link to="/" className="btn btn-outline-dark">Cancel</Link>
            </div>
        </div>
    )

}