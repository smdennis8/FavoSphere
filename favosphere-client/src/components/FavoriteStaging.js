import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from "../contexts/AuthContext";
import { deleteEmailById, findAllEmails } from '../services/EmailApi';

function FavoriteStaging(){
// state variables
const [email, setEmail] = useState([]);
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
    if (window.confirm(`Delete email Id ${emailId}: \nURL: ${email.url}\nTime: ${email.time}?`)) {        deleteEmailById(emailId)
        .then(() => {
            navigate("/gallery", {
                state: { msg: `"${email.url}" was deleted.` }
            });
        })
        const init = {
            method: 'DELETE'
        };
        fetch(`${url}/${emailId}`, init)
            .then(response => {
                if (response.status === 204) {
                    const newEmails = emails.filter(emails => emails.emailId !== emailId);
                    setEmails(newEmails);
                } else {
                    return Promise.reject(`Unexpected Status code: ${response.status}`);
                }
            })
            .catch(console.log);
    }
}

return(<>
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