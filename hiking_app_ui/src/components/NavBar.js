import { useContext, useState, Fragment } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import UserContext from "../context/UserContext";
import { Avatar, Menu, MenuItem, Tooltip, IconButton, ListItemIcon } from "@mui/material";
import { Logout } from "@mui/icons-material"
import { stringAvatar } from "../helpers/stringcolors";


export default function NavBar() {
    const { user, logout } = useContext(AuthContext);
    const {userInfo} = useContext(UserContext);
    const [anchorEl, setAnchorEl] = useState(null);


    const open = Boolean(anchorEl);

    const handleClick = (evt) => {
        setAnchorEl(evt.currentTarget);
    }

    const handleClose = () => {
        setAnchorEl(null);
    }

    const menu = () => {
        return (
            <>
            <Fragment>
                    <Tooltip title = "User Account">
                        <IconButton 
                            onClick={handleClick}
                            size="small"
                            sx={{ml: 3}}
                            aria-controls={open? 'account-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={open? 'true' : undefined}>{
                            accountIcon()
                        }
                        </IconButton>
                    </Tooltip>
                </Fragment>
                <Menu
                    id="account-menu"
                    anchorEl={anchorEl}
                    open={open}
                    onClose={handleClose}
                    onClick={handleClose}
                    PaperProps={{
                        elevation: 0,
                        sx: {
                          overflow: 'visible',
                          filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                          mt: 1.5,
                          '& .MuiAvatar-root': {
                            width: 32,
                            height: 32,
                            ml: -0.5,
                            mr: 1,
                          },
                          '&:before': {
                            content: '""',
                            display: 'block',
                            position: 'absolute',
                            top: 0,
                            right: 20,
                            width: 10,
                            height: 10,
                            bgcolor: 'background.paper',
                            transform: 'translateY(-50%) rotate(45deg)',
                            zIndex: 0,
                          },
                        },
                      }}
                      transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                      anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
                    >
                        <MenuItem
                            component={Link} to="/user/edit">
                            {accountIcon()}Profile
                        </MenuItem>
                        <MenuItem
                            onClick={logout}>
                            <ListItemIcon>
                                <Logout fontSize="small" />
                            </ListItemIcon>
                            Logout
                        </MenuItem>
                    </Menu>
                    
            </>
        );
    }

    const accountIcon = () => {
        return (
            <>{
                userInfo? <Avatar{...stringAvatar(userInfo.firstName? userInfo.firstName : user.sub)} /> : <Avatar sx = {{bgcolor: '#fff'}}>U</Avatar>
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
                        {user ? menu() : <Link to="/login" className="btn btn-outline-primary">Login</Link>}
                    </div>
                </div>
            </nav>
        </div>
    )

}
