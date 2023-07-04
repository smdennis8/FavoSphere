import { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import default_icon from '../assets/default_icon.png';

function Profile(){
    

    return(<>
    <div className="profile-page-placer">
        <div className="profile-item-ctn">
            <div className="profile-img-ctn">
                <img src={default_icon} alt="profile avatar" />
            </div>
            <h5>Full User Name</h5>
            <h5>{localStorage.getItem('username')}</h5>
        </div>
    </div>
    </>);
}

export default Profile;