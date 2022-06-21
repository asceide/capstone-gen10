import { useContext, useState } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import UserContext from "../context/UserContext";
import { Avatar, Stack, Menu, MenuItem } from "@mui/material";

function stringToColor(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  let colour = "#";
  for (let i = 0; i < 3; i++) {
    let value = (hash >> (i * 8)) & 0xff;
    colour += ("00" + value.toString(16)).substr(-2);
  }
  return colour;
}

function stringAvatar(name){
    return {
        sx: {
            bgcolor: stringToColor(name),
        },
        children: name.charAt(0)
    };
}
export default function NavBar() {
    const { user, logout } = useContext(AuthContext);
    const {userInfo} = useContext(UserContext);
    // To show and hide the menu
    const [showMenu, setShowMenu] = useState(false);

    const open = Boolean(showMenu);

    const handleClick = (evt) => {
        setShowMenu(evt.currentTarget);
    }

    const handleClose = () => {
        setShowMenu(null);
    }

    const menu = () => {
        return (
            <>
                <Menu>

                </Menu>
            </>
        );
    }

    const accountIcon = () => {
        return (
            <>{
                (user && userInfo)?  <Avatar{...stringAvatar(userInfo.firstName? userInfo.firstName : user.sub)} /> : <Avatar sx = {{bgcolor: '#fff'}}>U</Avatar>
            }</>
        )
    }

    return(
        <div>
            <nav className="navbar navbar-dark bg-dark">
                <div className="container">
                    <Link to="/" className="navbar-brand">Hiking App</Link>
                    <Link to="/trails" className="btn btn-primary p-2 mx-3">Trails</Link> 
                    <div className="col d-flex justify-content-center">
                    {
                        (user && userInfo)? 
                        <h3 className="text-white">Welcome {userInfo.firstName? userInfo.firstName: `User ${userInfo.appUserId}`}</h3> : <></>
                    }
                    </div>
                    <div className="col d-flex justify-content-end">
                        <Stack direction="row" spacing={2}>
                            {
                               accountIcon()
                            }
                        </Stack>
                        {user ? <button className="btn btn-outline-danger" onClick={logout}>Logout</button> 
                        : <Link to="/login" className="btn btn-outline-primary">Login</Link>}
                    </div>
                </div>
            </nav>
        </div>
    )

}
