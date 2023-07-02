import { Link } from 'react-router-dom';
import React from 'react';

const LeftPanel = () => {
    return (
        <div className="leftPanel">
                <Link to={'/'} href="home">Home</Link>
                <Link to={'/'} href="profile">Profile</Link>
                <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                <Link to={'/gallery'} href="table">Favorites Table</Link>
                <Link to={'/'} href="share">Share Favorites</Link>
                <Link to={'/gallery'} href="staging">Favorites Staging</Link>
                <Link to={'/'} href="contact">Contact</Link>
        </div>
    );
};

export default LeftPanel;