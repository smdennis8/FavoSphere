import { Link, useNavigate } from 'react-router-dom';
import React from 'react';
import { signOut } from '../services/AuthApi';

const LeftPanel = () => {

    const navigate = useNavigate();
    const username = localStorage.getItem('username');

    return (
        <div className="leftPanel">
            <p>Logged in as: {username}</p>
            <button className="btn btn-secondary" onClick={() => {signOut(); navigate("/");}}>Sign Out</button>
            
            <div className='menu-items-ctn'>
                <Link to={'/gallery'} href="home">Home</Link>
                <Link to={'/profile'} href="profile">Profile</Link>
                <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                <Link to={'/staging'} href="staging">Favorites Staging</Link>
            </div>    
        </div>
    );
};

export default LeftPanel;