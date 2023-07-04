import { useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findAllFavoritesByUserId } from "../services/FavoriteApi";

import AuthContext from "../contexts/AuthContext";

function FavoriteGallery() {

    const [favorites, setFavorites] = useState([]);

    const location = useLocation();

    const auth = useContext(AuthContext);

    const currentUserId = localStorage.getItem('appUserId');
    useEffect(() => {
        findAllFavoritesByUserId(currentUserId)
            .then(data => setFavorites(data));
    }, [currentUserId]);

    return(<>
        <div className="row">
            <div className="col">
            <Link to="/add" className="btn btn-dark">Add New Favorite</Link>
            </div>
        </div>

        <div className="row">
        {favorites.length > 0
        ? favorites.map(f =>
        <div className="card col-sm-6 col-lg-4" key={f.favoriteId}>
            {auth.isLoggedIn() &&
                <Link to={`/card/${f.favoriteId}`}>
                    {f.imageUrl !== null &&
                    <img src={f.imageUrl} className="card-img-top" alt={"Title image for " + f.title} />}
                    {f.gifUrl !== null && 
                    <img src={f.gifUrl} className="card-gif-mid" alt={"Title gif for " + f.title} />}
                    <div className="card-body">
                        <h5 className="card-title">{f.title}</h5>
                        <h6 className="card-source">{f.source}</h6>
                        <h6 className="card-creator">by: {f.creator}</h6>
                        <h6 className="card-type">type: {f.type}</h6>
                        <h6 className="card-description">{f.description}</h6>
                        <h7 className="card-createdOn">{f.createdOn}</h7>
                        <p></p>
                        <h7 className="card-updatedOn">{f.updatedOn}</h7>
                    </div>
                </Link>
                
            }
        </div>)
    : <div className="col-sm-6 col-lg-4">No Favorites</div>}
            <div className="row">
                {location.state && 
                <div className="alert alert-info">
                    {location.state.msg}
                </div>}
            </div>
        </div>
        
    </>);
}

export default FavoriteGallery;