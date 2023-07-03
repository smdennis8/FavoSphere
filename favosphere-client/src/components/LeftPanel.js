import { Link } from 'react-router-dom';
import React from 'react';

const LeftPanel = () => {
    return (
        <div className="leftPanel">
                <Link to={'/gallery'} href="home">Home</Link>
                <Link to={'/notFound'} href="profile">Profile</Link>
                <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                <Link to={'/staging'} href="staging">Favorites Staging</Link>
        </div>
    );
};

export default LeftPanel;