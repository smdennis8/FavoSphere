import { Link, useNavigate } from 'react-router-dom';
import React from 'react';
import { signOut } from '../services/AuthApi';

const LeftPanel = () => {

    const navigate = useNavigate();
    const username = localStorage.getItem('username');

    return (
        <div className="leftPanel">
            
            
            <div className='menu-items-ctn'>
                <div className='lpi'>
                    <Link to={'/gallery'} href="home">Home</Link>
                </div>  
                <div className='lpi'>
                    <Link to={'/profile'} href="profile">Profile</Link>
                </div>
                <div className='lpi'>
                    <Link to={'/gallery'} href="gallery">Favorites Gallery</Link>
                </div>
                <div className='lpi'>
                    <Link to={'/staging'} href="staging">Favorites Staging</Link>
                </div>
                <div className='lpi'>
                    <Link onClick={signOut} to="/">Sign Out</Link>
                </div>
                <div className='space'></div>
                <div className='log-status'>
                    <p>Logged in as: {username}</p> 
                </div>
                
            </div>    
        </div>
    );
};

export default LeftPanel;