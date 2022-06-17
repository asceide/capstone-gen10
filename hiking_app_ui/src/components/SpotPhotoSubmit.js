

export default function PhotoSubmit() {

    const [file, setFile] = useState();

    const handleChange = (evt) => {
        setFile(evt.target.value);
    }

    const handleSubmit = (evt) => {

    }


    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="file">Select photo to upload</label>
                    <input type="file" id="file" name="file" accept="image/*" 
                        className="form-control" onChange={handleChange}/>
                </div>
                
                <button type="submit" className="btn btn-outline-dark">Add photo</button>
            </form>
        </div>
    )
}