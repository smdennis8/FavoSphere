import { Link } from 'react-router-dom';
import React from 'react';

const LeftPanel = (props) => {
    return (
        <div className="leftPanel">
                <Link to={'/'} a href="home">Home</Link>
                <Link to={'/'} a href="profile">Profile</Link>
                <Link to={'/gallery'} a href="gallery">Favorites Gallery</Link>
                <Link to={'/gallery'} a href="table">Favorites Table</Link>
                <Link to={'/'} a href="share">Share Favorites</Link>
                <Link to={'/gallery'} a href="staging">Favorites Staging</Link>
                <Link to={'/'} a href="contact">Contact</Link>
        </div>
    );
};

export default LeftPanel;