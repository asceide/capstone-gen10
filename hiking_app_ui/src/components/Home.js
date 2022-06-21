import { useState, useEffect } from "react";
import { findAll } from "../services/trail";
import TrailMini from "./TrailMini";

export default function Home(){

    const [trails, setTrails] = useState([]);
    const [searched, setSearched] = useState(false);
    const [input, setInput] = useState("");
    const [filtered, setFiltered] = useState([]);

    useEffect(() => {
        findAll()
            .then(setTrails)
            .catch(console.error);

    }, [])


    const handleChange = (evt) => {
        setInput(evt.target.value);
    }

    const handleSearch = (evt) => {
        evt.preventDefault();
        setSearched(true);
        setFiltered(trails.filter(i => i.state === input));
    }

    const handleClear = () => {
        setInput("");
        setSearched(false);
    }

    return(
        <div className="container">
            <div className="row">
                <div className="col" style={{textAlign: "center"}}>
                    <h1>Hiking Home / ハイキングホーム</h1>
                </div> 
            </div>

            <form onSubmit={handleSearch}>
            <div className="form-row">
                <div className="col">
                    <div className="float-right">
                        <input type="text" id="state" name="state" placeholder="Search for trails by state" 
                            onChange={handleChange} value={input}/>
                    </div>   
                </div>
                <div className="col">
                    <button type="submit" className="btn btn-outline-dark">Search</button>
                    <button className="btn btn-outline-danger" onClick={handleClear}>Clear</button>
                </div>
            </div>
            </form>

            <div>
                {searched && <div>{filtered.map(i => {
                        return(<TrailMini trail={i} key={i?.trailId} />)
                    })}</div>} 
            </div>

                

            
        </div>
 )
}