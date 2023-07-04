import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Errors from "./Errors";
import AuthContext from "../contexts/AuthContext";
import { authenticate } from "../services/AuthApi";
import jwt_decode from "jwt-decode";
import { GoogleLogin, googleLogout, useGoogleLogin } from "@react-oauth/google";
import CreateAccountForm from "./CreateAccountForm";


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
                <h2>Login</h2>
            <div className="google-login">
                {/* {profile ? (navigate('/gallery')) : (<button onClick={() => login()}>Sign in with Google 🚀 </button>)} */}
                {/* <button className="external-login-btn" onClick={() => login()}>Login with Google</button> */}
                <GoogleLogin
                    onSuccess={(credentialResponse) => {
                        console.log(jwt_decode(credentialResponse.credential,{ header: true }));
    
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
                            <input type="text" className="form-control" id="email"
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
                            <button type="submit" className="btn btn-primary">Log In</button>
                        </div>
                        <div className="forgot-sumbit-ctn">
                            <Link to="/create-account" className="btn btn-secondary">Create Account</Link>
                        </div>
                    </form>
                    <Errors errors={errors} />
                </div>
            </div>
        </div>);
    }
    
    export default LoginForm;