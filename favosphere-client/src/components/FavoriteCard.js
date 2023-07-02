import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { findFavoriteById } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";

function FavoriteCard() {

    const [favorite, setFavorite] = useState([]);

    const auth = useContext(AuthContext);

    useEffect(() => {
        findFavoriteById()
            .then(data => setFavorite(data));
    }, []);

    <div className="col-sm-6 col-lg-4" key={favorite.favoriteId}>
        <div className="card">
            <img src={favorite.imageUrl} className="card-img-top" alt={"Title image for " + favorite.title} />
            <div className="card-body">
                <h5 className="card-title">{favorite.title}</h5>
                {auth.isLoggedIn() &&
                    <Link to={`/edit/${favorite.favoriteId}`} className="btn btn-primary">Edit</Link>}
                {auth.isLoggedIn() &&
                    <Link to={`/delete/${favorite.favoriteId}`} className="btn btn-danger">Delete</Link>}
            </div>
        </div>
    </div>
}

export default FavoriteCard;