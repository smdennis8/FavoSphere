import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { createAppUser } from "../services/AppUserApi";
import Errors from "./Errors";

const EMPTY_USER = {
    appUserId: 0,
    firstName: null,
    middleName: null,
    lastName: null,
    phone: null,
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
                <label htmlFor="firstName" className="form-label">First Name</label>
                <input type="text" className="form-control form-control-add" id="firstName" name="firstName" value={user.firstName} placeholder="First Name" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <label htmlFor="middleName" className="form-label">Middle Name</label>
                <input type="text" className="form-control form-control-add" id="middleName" name="middleName" value={user.middleName} placeholder="Middle Initial" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <label htmlFor="lastName" className="form-label">Last Name</label>
                <input type="text" className="form-control form-control-add" id="lastName" name="lastName" value={user.lastName} placeholder="Last Name" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <label htmlFor="phone" className="form-label">Phone number</label>
                <input type="text" className="form-control form-control-add" id="phone" name="phone" value={user.phone} placeholder="Phone number" onChange={handleChange} />
            </div>
            <div className="mb-3">
                <label htmlFor="email" className="form-label">Email</label>
                <input type="text" className="form-control form-control-add" id="email" name="email" value={user.email} placeholder="youremail@domain.com" onChange={handleChange} required/>
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" className="form-control form-control-add" id="password" name="password" value={user.password} placeholder="************" onChange={handleChange} required/>
            </div>
            <div className="mb-3 buttons-crt-acc">
                <button type="submit" className="btn btn-primary button-prm acc">Submit</button>
                <Link to="/" type="button" className="btn btn-secondary button-scnd">Cancel</Link>
                <Link to="/" type="button" className="btn btn-secondary button-scnd">Back to Log-In Page</Link>
            </div>
        </form>
        <Errors errors={errors} />
    </div>;
}

export default CreateAccountForm;