import { Link, useNavigate } from 'react-router-dom';
import React from 'react';
import { signOut } from '../services/AuthApi';

const LeftPanel = () => {

    const navigate = useNavigate();

    return (
        <div className="leftPanel">
            <button className="btn btn-secondary" onClick={() => {signOut(); navigate("/");}}>Sign Out</button>
                <Link to={'/gallery'} href="home">Home</Link>
                <Link to={'/profile'} href="profile">Profile</Link>
                <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                <Link to={'/staging'} href="staging">Favorites Staging</Link>
        </div>
    );
};

export default LeftPanel;