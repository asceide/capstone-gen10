import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";

export default function Nav() {
    const { user, logout } = useContext(AuthContext);

    return(
        <div>
            <nav className="navbar navbar-dark bg-dark">
                <div className="container">
                    <Link to="/" className="navbar-brand">Hiking App</Link>
                    <div className="col d-flex justify-content-end">
                        {user ? <button className="btn btn-outline-danger" onClick={logout}>Logout</button> 
                        : <Link to="/login" className="btn btn-outline-primary">Login</Link>}
                    </div>
                </div>
            </nav>
        </div>
    )

}