import { useState, useEffect } from "react";
import { findAll } from "../services/trail";
import TrailMini from "./TrailMini";
import { stateList as states } from "../helpers/states";
import { useForm } from "react-hook-form";
import { Select, MenuItem, InputLabel, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";


export default function Home() {

    const [trails, setTrails] = useState([]);
    const [searched, setSearched] = useState(false);
    const [filtered, setFiltered] = useState([]);

    useEffect(() => {
        findAll()
            .then(setTrails)
            .catch(console.error);

    }, [])

    const navigate = useNavigate();
    const { register, handleSubmit } = useForm();

    const onSubmit = (data, evt) => {
        evt.preventDefault();
        setFiltered(trails.filter(i => i.state === data.state));
        setSearched(true);
    };



    return (
        <div className="container">
            <br></br>
            <div className="row">
                <div className="col" style={{ textAlign: "center" }}>
                    <h1>Hiking Home / ハイキングホーム</h1>
                </div>
            </div>

            <div id="carouselExampleIndicators" className="carousel slide" data-ride="carousel">
  <ol className="carousel-indicators">
    <li data-target="#carouselExampleIndicators" data-slide-to="0" className="active"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
  </ol>
  <div className="carousel-inner">
    <div className="carousel-item active">
      <img className="d-block w-100" src="./WaiheeRidgeTrail.jpg"  width="800" height ="600" alt="First slide" onClick={() => navigate(`/trails`)}/>
      <div className="carousel-caption d-none d-md-block">
    <h1>Find Your Next Hiking Trail</h1>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
    <h5>Waihee Ridge Trail</h5>
    <p>Maui, HI</p>
  </div>
    </div>
    <div className="carousel-item">
      <img className="d-block w-100" src="./SlidingSands.jpg" width="800" height ="600"  alt="Second slide" onClick={() => navigate(`/trails`)}/>
      <div className="carousel-caption d-none d-md-block">
    <h5>Sliding Sands</h5>
    <p>Maui, HI</p>
  </div>
    </div>
    <div className="carousel-item">
      <img className="d-block w-100" src="./Kahakapao.jpg" width="800" height ="600" alt="Third slide" onClick={() => navigate(`/trails`)}/>
      <div className="carousel-caption d-none d-md-block">
    <h5>Kahakapao Loop Trail</h5>
    <p>Maui, HI</p>
  </div>
    </div>
  </div>
  <a className="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
    <span className="carousel-control-prev-icon" aria-hidden="true"></span>
    <span className="sr-only">Previous</span>
  </a>
  <a className="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
    <span className="carousel-control-next-icon" aria-hidden="true"></span>
    <span className="sr-only">Next</span>
  </a>
</div>
<br></br>


            <form onSubmit={handleSubmit(onSubmit)}>
                <div className="form-row justify-content-center">
                    <div className="col-2 d-flex align-self-center">
                        <InputLabel htmlFor="state" className="p-1" style={{ margin: 8 }}>State</InputLabel>
                        <Select id="state" name="state" defaultValue={states[0].key} {...register("state")}>
                            {states.map(state => {
                                return (
                                    <MenuItem key={state.key + 'h'} value={state.value} >
                                        {state.text}
                                    </MenuItem>
                                );
                            }
                            )}
                        </Select>
                    </div>
                    <div className="col-1 d-flex align-self-center">
                        <Button type="submit" variant="contained" color="inherit">Search</Button>
                    </div>
                </div>
            </form>


            <div>
                {searched && <div>{filtered.map(i => {
                    return (<TrailMini trail={i} key={i?.trailId} />)
                })}</div>}
            </div>




        </div>
    )
}