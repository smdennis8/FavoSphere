import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { GoogleLogin, googleLogout, useGoogleLogin } from "@react-oauth/google";

import AuthContext from "../contexts/AuthContext";

import { authenticate, handleGoogleLogin } from "../services/AuthApi";
import { findUserByUsername } from "../services/AppUserApi";

import Errors from "./Errors";
import CreateAccountForm from "./CreateAccountForm";

import default_icon from '../assets/default_icon.png';

import jwt_decode from "jwt-decode";

function LoginForm() {

    const [errors, setErrors] = useState([]);
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    const navigate = useNavigate();

    const auth = useContext(AuthContext);

    const handleChange = (evt) => {
        const nextCredentials = { ...credentials };
        nextCredentials[evt.target.name] = evt.target.value;
        setCredentials(nextCredentials);
    };

    const handleVerifiedGoogleLogin = (googleCredentialJwt) => {
        const tokenParts = googleCredentialJwt.split('.');
        if (tokenParts.length > 1) {
            const userData = tokenParts[1];
            const decodedUserData = JSON.parse(atob(userData));

            let newCredentials = {email: '', password: ''};
            findUserByUsername(decodedUserData.email)
            .then( data => {
                localStorage.setItem('appUserId',data.appUserId);
                newCredentials.email = data.email;
                newCredentials.password = "P@ssw0rd!"; // CHANGE TO backend google login handler
                setCredentials(newCredentials);
            });
            localStorage.setItem('username', decodedUserData.email);
            authenticate(credentials).then(user => {
                auth.onAuthenticated(user);
                navigate('/gallery');
            })
                .catch(err => setErrors(err));    
        }
    };

    const handleSubmit = (evt) => {
        evt.preventDefault();
        authenticate(credentials).then(user => {
            auth.onAuthenticated(user);
            navigate('/gallery');
        })
            .catch(err => setErrors(err));
    };

    const ColoredLine = ({ color }) => (
        <hr
            style={{
                color: color,
                backgroundColor: color,
                height: 2,
                marginRight: 20
            }}
        />
    );


    return(
        <div className="center-all-full-page">
            <div className="login-input">
                <div className="login-logo-container">
                    <img src={default_icon} alt="Logo" />
                </div>
            <div className="google-login">
                {/* {profile ? (navigate('/gallery')) : (<button onClick={() => login()}>Sign in with Google 🚀 </button>)} */}
                {/* <button className="external-login-btn" onClick={() => login()}>Login with Google</button> */}
                <GoogleLogin
                    onSuccess={(credentialResponse) => {
                        handleVerifiedGoogleLogin(credentialResponse.credential);
                    }}
                    onError={() => {console.log("Login Failed");}}
                    theme="filled_blue"
                    shape="circle"
                />
                </div>
                <ColoredLine color="black" />
                <div className="container-fluid">
                    <h1>{auth.user.email}</h1>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="email" className="">Email</label>
                            <input type="text" className="form-control form-control-fav" id="email"
                                name="email" value={credentials.email} placeholder="youremail@domain.com"
                                onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input type="password" className="form-control" id="password"
                                name="password" value={credentials.password} placeholder="************"
                                onChange={handleChange} required />
                        </div>
                        <div className="forgot-sumbit-ctn">
                            <button type="submit" className="btn btn-primary button-prm">Log In</button>
                        </div>
                        <div className="forgot-sumbit-ctn">
                            <Link to="/create-account" className="btn btn-secondary button-scnd">Create Account</Link>
                        </div>
                    </form>
                    <Errors errors={errors} />
                </div>
            </div>
        </div>);
    }
    
    export default LoginForm;