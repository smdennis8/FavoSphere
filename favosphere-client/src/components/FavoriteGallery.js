import { useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findAllFavorites } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";

function FavoriteGallery() {

    const [favorites, setFavorites] = useState([]);

    const location = useLocation();

    const auth = useContext(AuthContext);

    useEffect(() => {
        findAllFavorites()
            .then(data => setFavorites(data));
    }, []);

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
                    {f.imageUrl !== null && f.imageUrl !== "" &&
                    <img src={f.imageUrl} className="card-img-top" alt={"Title image for " + f.title} />}
                    {f.gifUrl !== null &&  f.gifUrl !== "" &&
                    <img src={f.gifUrl} className="card-gif-mid" alt={"Title gif for " + f.title} />}
                    <div className="card-body">
                        <h1 className="card-title">{f.title}</h1>
                        <h2 className="card-source">{f.source}</h2>
                        <h2 className="card-creator">by: {f.creator}</h2>
                        <h2 className="card-type">type: {f.type}</h2>
                        <h2 className="card-description">{f.description}</h2>
                        <h3 className="card-createdOn">Created On: {f.createdOn}</h3>
                        <h3 className="card-updatedOn">Updated On: {f.updatedOn}</h3>
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