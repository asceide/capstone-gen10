import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import UserContext from "../context/UserContext";

export default function NavBar() {
    const { user, logout } = useContext(AuthContext);
    const userInfo = useContext(UserContext);

    return(
        <div>
            <nav className="navbar navbar-dark bg-dark">
                <div className="container">
                    <Link to="/" className="navbar-brand">Hiking App</Link>
                    <div className="col d-flex justify-content-center">
                    {
                        user && userInfo? 
                        <h3 className="text-white">Welcome {userInfo.firstName}</h3> : <></>
                    }
                    </div>
                    <div className="col d-flex justify-content-end">
                        {user ? <button className="btn btn-outline-danger" onClick={logout}>Logout</button> 
                        : <Link to="/login" className="btn btn-outline-primary">Login</Link>}
                    </div>
                </div>
            </nav>
        </div>
    )

}
