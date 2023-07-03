import { Link } from 'react-router-dom';
import React from 'react';

const LeftPanel = () => {
    return (
        <div className="leftPanel">
                <Link to={'/gallery'} href="home">Home</Link>
                <Link to={'/notFound'} href="profile">Profile</Link>
                <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                <Link to={'/gallery'} href="table">Favorites Table</Link>
                <Link to={'/notFound'} href="share">Share Favorites</Link>
                <Link to={'/gallery'} href="staging">Favorites Staging</Link>
                <Link to={'/notFound'} href="contact">Contact</Link>
        </div>
    );
};

export default LeftPanel;