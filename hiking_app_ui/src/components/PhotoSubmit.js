import { useState } from "react";
import { bucketUpload, addPhoto } from "../services/photo";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function PhotoSubmit({ spotId = 0, trailId = 0, toggleForm, photos, setPhotos }) {

    const bucketUrl = window.BUCKET_URL? window.BUCKET_URL : process.env.REACT_APP_BUCKET_URL;
    const [message, setMessage] = useState();
    const [file, setFile] = useState();


    const navigate = useNavigate();


    const handleSubmit = async (evt) => {

        let newPhotos = [...photos];

        evt.preventDefault();
        const imageName = file.name;
        const photoUrl = `${bucketUrl}/${imageName}`;
        let uploadedPhoto;
        if (spotId) {
            uploadedPhoto = {
                spotId,
                photoUrl
            }

            await bucketUpload(photoUrl, file)
                .then(toggleForm())
                .then(newPhotos = newPhotos.concat([{
                    photoUrl: photoUrl
                }]))
                .then(setPhotos(newPhotos))
                .catch(setMessage)
            addPhoto(`spot-id=${spotId}`, uploadedPhoto)
                .catch(setMessage);
        } else if (trailId) {
            uploadedPhoto = {
                trailId,
                photoUrl
            }
            await bucketUpload(photoUrl, file)
                .then(toggleForm())
                .then(newPhotos = newPhotos.concat([{
                    photoUrl: photoUrl
                }]))
                .then(setPhotos(newPhotos))
                .catch(setMessage)
            addPhoto(`trail-id=${trailId}`, uploadedPhoto)
                .catch(setMessage);
        }





    }

    const handleChange = (evt) => {
        setFile(evt.target.files[0])
    }


    return (
        <div>
            <form onSubmit={handleSubmit}>
                {spotId ? <div className="form-group">
                    <label htmlFor="spotId">Spot #: </label>
                    <input type="text" id="spotId" name="spotId" value={spotId} readOnly></input>
                </div> :
                    <div className="form-group">
                        <label htmlFor="trailId">Trail #: </label>
                        <input type="text" id="trailId" name="trailId" value={trailId} readOnly></input>
                    </div>
                }

                <div className="form-group" style={{ margin: 3 }}>
                    <label htmlFor="file">Select photo to upload</label>
                    <input type="file" id="file" name="file" accept="image/*"
                        className="form-control" onChange={handleChange} />
                </div>

                <button type="submit" className="btn btn-outline-dark" style={{ margin: 3 }}>Add photo</button>
                <button className="btn btn-outline-danger" style={{ margin: 3 }} onClick={toggleForm}>Cancel</button>
            </form>
            <div>
                <h2>{message}</h2>
            </div>

        </div>
    )
}