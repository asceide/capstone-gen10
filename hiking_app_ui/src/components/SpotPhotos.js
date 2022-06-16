import { findBySpot} from "../services/photos";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";


export default function SpotPhotos() {

    const [photos, setPhotos] = useState([]);
    const navigate = useNavigate();
    const {spotId} = useParams();

    useEffect(() => {
        findBySpot(spotId)
            .then(setPhotos)
            .catch(navigate(`/spot/${spotId}`));
    }, [spotId]);

    
}