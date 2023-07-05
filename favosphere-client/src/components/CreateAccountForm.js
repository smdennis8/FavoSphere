import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { createAppUser } from "../services/AppUserApi";
import Errors from "./Errors";

const EMPTY_USER = {
    appUserId: 0,
    firstName: null,
    middleName: null,
    lastName: null,
    email: null,
    password: null,
    registeredOn: new Date().toISOString().slice(0, 10),
    lastLogin: new Date().toISOString().slice(0, 10),
    enabled: true,
    roles: ['USER']
};

function CreateAccountForm() {
    const [user, setUser] = useState(EMPTY_USER);
    const [errors, setErrors] = useState([]);
    const navigate = useNavigate();

    

    const handleChange = (event) => {
        const nextUser = { ...user };
        let nextValue = event.target.value;
        nextUser[event.target.name] = nextValue;
        setUser(nextUser);
    }

    const handleCreateAccount = (event) => {
        event.preventDefault();
        createAppUser(user)
            .then(() => {
                navigate("/create-account", {
                    state: { 
                        msgType: 'success',
                        msg: `Your FavoSphere account was created!` }
                });
            })
            .catch(err => setErrors(err))
    }

    return <div className="container-fluid">
        <form onSubmit={handleCreateAccount}>
            <div className="mb-3">
                <label htmlFor="email" className="form-label">Email</label>
                <input type="text" className="form-control form-acc" id="email" name="email" value={user.email} placeholder="youremail@domain.com" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" className="form-control form-acc" id="password" name="password" value={user.password} placeholder="************" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <button type="submit" className="btn btn-primary">Submit</button>
                <Link to="/" type="button" className="btn btn-secondary">Cancel</Link>
                <Link to="/" type="button" className="btn btn-secondary">Back to Log-In Page</Link>
            </div>
        </form>
        <Errors errors={errors} />
    </div>;
}

export default CreateAccountForm;