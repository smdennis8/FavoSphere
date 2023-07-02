import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Errors from "./Errors";
import AuthContext from "../contexts/AuthContext";
import { authenticate } from "../services/AuthApi";

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

    return <div className="container-fluid">
        <h1>{auth.user.username}</h1>
        <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="email" className="form-label">Email</label>
                <input type="text" className="form-control" id="email"
                    name="email" value={credentials.email}
                    onChange={handleChange} required />
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" className="form-control" id="password"
                    name="password" value={credentials.password}
                    onChange={handleChange} required />
            </div>
            <div className="mb-3">
                <button type="submit" className="btn btn-primary">Log In</button>
                <Link to="/" type="button" className="btn btn-secondary">Cancel</Link>
            </div>
        </form>
        <Errors errors={errors} />
    </div>;
}

export default LoginForm;