import { useContext } from "react";
import { Link, useNavigate } from 'react-router-dom';
import React from 'react';
import { signOut } from '../services/AuthApi';
import AuthContext from '../contexts/AuthContext';

const LeftPanel = () => {
    const auth = useContext(AuthContext);
    const navigate = useNavigate();
    const username = localStorage.getItem('username');
    const handleSignOut = () => {
        auth.signOut();
        navigate('/');
    }
    return (
        <div className="group-left-panel">
        <div className="leftPanel">
            <div className='menu-items-ctn'>
                <div className='lpi'>
                    <Link to={'/gallery'} href="home">Home</Link>
                </div>
                <div className='lpi'>
                    <Link to={'/gallery'} href="gallery">My Favorites Gallery</Link>
                </div>
                <div className='lpi'>
                    <Link to={'/staging'} href="staging">Favorites Staging</Link>
                </div>
                <div className="lpi">
                    <Link to={'/add'} href="add">Add New Favorite</Link>
                </div>
                <div className='lpi'>
                    <Link to="/" onClick={handleSignOut}>Sign Out</Link>
                </div>
                <div className='space'></div>
                <div className='log-status'>
                    <p>Logged in as: {username}</p> 
                </div>
            </div>    
        </div>
        </div>
    );
};

export default LeftPanel;