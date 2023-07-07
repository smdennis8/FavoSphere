import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import AuthContext from "../contexts/AuthContext";
import { deleteEmailById, findAllEmails, refreshEmailsByUserId } from '../services/EmailApi';
import { scrapeYouTube, callScraperFromUrl } from "../services/ScraperApi";

function FavoriteStaging() {
    // state variables
    const [email, setEmail] = useState([]);
    const [emails, setEmails] = useState([]);
    const navigate = useNavigate();

        const url = 'http://localhost:8080/email';

        const auth = useContext(AuthContext);

    

    useEffect(() => {
        refreshEmailsByUserId(localStorage.getItem("appUserId"))
                .then(data => {setEmails(data);});
        //scrapeYouTube("https://www.youtube.com/watch?v=U4xOOnLBPB8").then(data => console.log(data));

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
    };

    const handleRefreshEmail = () => {
        const newEmails = refreshEmailsByUserId(localStorage.getItem("appUserId"));
        if(newEmails.size > emails.size){
            setEmails(newEmails);
        }
        refreshEmailsByUserId(localStorage.getItem("appUserId")).then(data => setEmails(data));
    };

    return(<>
            <div className="button-banner-placer">
            <button className="btn btn-secondary" onClick={() => handleRefreshEmail()}>Refresh Inbox</button>
            <Link to="/add" className="btn btn-secondary">Add New Favorite</Link>
            </div>
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
                                        <Link 
                                        className="btn btn-primary btn-sm mr-2" 
                                        state={{ urlToScrape: email.url }} 
                                        to={`/add-from-email`}>
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