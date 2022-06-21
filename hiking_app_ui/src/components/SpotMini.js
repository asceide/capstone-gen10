import { useEffect } from "react";
import { findById } from "../services/spot";
import { findBySpot } from "../services/photo";


export default function SpotMini({spotId}) {

    const [spot, setSpot] = useState();
    const [photos, setPhotos] = useState([]);


    useEffect(() => {
        findById(spotId)
            .then(setSpot)
            .catch(console.error);
        findBySpot(spotId)
            .then(setPhotos)
            .catch(console.error);
    }, [spotId])

    return (
        <div className="card">
            <img src={photos[0].photoUrl} className="card-img-top" alt={`spot ${spotId}`}/>
            <div className="card-body">
                <h5 className="card-title">{spot.name}</h5>
                <p>Rated {spot.rating} /5 by {spot.ratingCount} users</p>
            </div>
        </div>
    )
}