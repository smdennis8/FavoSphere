import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from "../contexts/AuthContext";
import { findAllEmails, deleteEmailById } from '../services/EmailApi';

function FavoriteStaging() {
    // state variables
    const [emails, setEmails] = useState([]);
    const navigate = useNavigate();

    const url = 'http://localhost:8080/email';

    const auth = useContext(AuthContext);

    useEffect(() => {
        findAllEmails()
            .then(data => setEmails(data));
    }, []);

    const handleDeleteEmail = (emailId) => {
        const email = emails.find(email => email.emailId === emailId);
        if (window.confirm(`Delete email Id ${email.emailId}: \nURL: ${email.url}\nTime: ${email.time}?`)) {
            deleteEmailById(emailId)
                .then(res => {
                    navigate("/staging", {
                        state: { msg: `Email: ${emailId} was deleted.` }
                    });
                })
                .catch(() => {
                    navigate("/staging", {
                        state: { msg: `Email: ${emailId}} was not found.` }
                    });
                })
            }
    }

    return (<>
        <section id="listContainer" className='list'>
            <table>
                <thead>
                    <tr>
                        <th>Sent On</th>
                        <th>Url</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {emails.map(email => (
                        <tr key={email.emailId}>
                            <td>{email.time}</td>
                            <td>{email.url}</td>
                            <td>
                                <div className="mr-2">
                                    <Link className="btn btn-primary btn-sm mr-2" to={`/add`}>
                                        <i className="bi bi-pencil-square"></i> Edit
                                    </Link>
                                    <button className="btn btn-danger btn-sm" onClick={() => handleDeleteEmail(email.emailId)}>
                                        <i className="bi bi-trash"></i> Delete
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </section>
    </>);
}

export default FavoriteStaging;