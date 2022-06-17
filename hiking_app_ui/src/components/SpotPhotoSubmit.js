import { useState } from "react";
import { bucketUpload, addPhoto } from "../services/photo";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function SpotPhotoSubmit() {

    const bucketUrl = "https:hikinh-test-bucket.s3.us-east-1.amazonaws.com"
    const [message, setMessage] = useState();
    const [file, setFile] = useState();



    const {spotId} = useParams();
    const navigate = useNavigate();


    const handleSubmit = async (evt) => {
        
        evt.preventDefault();
        const imageName = Math.floor(Math.random() * 5000);
        const photoUrl = `${bucketUrl}/${imageName}`;
        const uploadedPhoto = {
            spotId,
            photoUrl
        }

        bucketUpload(photoUrl, file)
            .then(addPhoto(`spot-id=${spotId}`, uploadedPhoto)
                    .then(navigate(`/spot/${spotId}`))
                    .catch(setMessage))
            .catch(setMessage)

        

        
    }

    const handleChange = (evt) => {
        setFile(evt.target.files[0])
    }


    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                <label htmlFor="spotId">Spot #: </label>
                    <input type="text" id="spotId" name="spotId" value={spotId} readOnly></input>
                </div>
                <div className="form-group" style={{margin: 3}}>
                    <label htmlFor="file">Select photo to upload</label>
                    <input type="file" id="file" name="file" accept="image/*" 
                        className="form-control" onChange={handleChange} />
                </div>
                
                <button type="submit" className="btn btn-outline-dark" style={{margin: 3}}>Add photo</button>
            </form>
            <div>
                <h2>{message}</h2>
            </div>

        </div>
    )
}