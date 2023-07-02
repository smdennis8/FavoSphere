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
                <div className="col-sm-6 col-lg-4" key={f.favoriteId}>
                    <div className="card">
                        <img src={f.imageUrl} className="card-img-top" alt={"Title image for " + f.title} />
                        <div className="card-body">
                            <h5 className="card-title">{f.title}</h5>
                            {auth.hasRole('ADMIN') &&
                                <Link to={`/card/${f.favoriteId}`} className="btn btn-primary">Edit</Link>}
                            {auth.hasRole('ADMIN') &&
                                <Link to={`/delete/${f.favoriteId}`} className="btn btn-danger">Delete</Link>}
                        </div>
                    </div>
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