import { useContext, useState, Fragment } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import UserContext from "../context/UserContext";
import { AppBar, Avatar, Box, Button, Menu, MenuItem, Toolbar, Tooltip, IconButton, ListItemIcon, Typography } from "@mui/material";
import { Logout, Home } from "@mui/icons-material"
import { stringAvatar } from "../helpers/stringcolors";



export default function NavBar() {

    const { user, logout } = useContext(AuthContext);
    const { userInfo } = useContext(UserContext);
    const [anchorEl, setAnchorEl] = useState(null);

    // To check if a menu is open or closed
    const open = Boolean(anchorEl);

    // On a click, set the anchor and show the menu
    const handleClick = (evt) => {
        setAnchorEl(evt.currentTarget);
    }

    // On close, set the anchor to null. Also closes the menu, imagine that
    const handleClose = () => {
        setAnchorEl(null);
    }


    const menu = () => {
        return (
            <>
                <Box sx={{ flexGrow: 0 }}>
                    <Tooltip title="User Account">
                        <IconButton
                            onClick={handleClick}
                            size="small"
                            sx={{ ml: 3 }}
                            aria-controls={open ? 'account-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={open ? 'true' : undefined}>{
                                accountIcon()
                            }
                        </IconButton>
                    </Tooltip>
                </Box>
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
                    {
                        user?.authorities?.includes('ROLE_ADMIN')? 
                        <MenuItem
                            component={Link} to="/user/administration">
                                <ListItemIcon >
                                    <Home fontSize="small" />
                                </ListItemIcon>
                                Admin
                        </MenuItem>: <></>
                    }
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
                userInfo ? <Avatar{...stringAvatar(userInfo.firstName ? userInfo.firstName : user.sub)} /> : <Avatar sx={{ bgcolor: '#fff' }}>U</Avatar>
            }</>
        )
    }

    const navbar = () => {
        return (
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="static" style={{ background: '#bcbcbc' }}>
                    <Toolbar>
                        <Typography variant="h6" color="#38761d" sx={{ flexGrow: 1 }} component={Link} to="/trails">
                            Trails
                        </Typography>
                        <Typography variant="h6" color="#38761d" sx={{ flexGrow: 1 }} component={Link} to="/" style={{ color: "#38761d", textDecoration: 'none' }} >
                            Hiking App
                        </Typography>
                        {
                            user ? menu() : <><Link to="/login"><Button color="success">Login</Button></Link><Link to="/register"><Button color="success">Register</Button></Link></>
                        }
                    </Toolbar>
                </AppBar>
            </Box>
        )
    }

    return (
        <div>
            {navbar()}
        </div>
    )

}
