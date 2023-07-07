import { useRef, useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findAllFavoritesByUserId, findAllFavorites } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";
import { Grid } from "@mui/material";

function FavoriteGallery() {

    const descriptionRef = useRef(null);
    const [favorites, setFavorites] = useState([]);
    const location = useLocation();
    const auth = useContext(AuthContext);
    const roles = localStorage.getItem('roles');
    const currentUserId = localStorage.getItem('appUserId');
    useEffect(() => {
        if(roles.includes('ADMIN')) {
            findAllFavorites()
            .then(data => setFavorites(data));
        } else {
            findAllFavoritesByUserId(currentUserId)
            .then(data => setFavorites(data));
        }
    }, [currentUserId]);

    return(<>
    {favorites.length > 0 &&
    <div className="gallery-title">
        <h1>NOW ALL YOUR FAVS IN ONE PLACE...</h1>
    </div>}
    {favorites.length > 0 ? 
    <Grid container spacing={4} columnSpacing={{ xs: 5 }}>
        {favorites.map(f => (
            <Grid item md={4}>
                <div className="card col-sm-6 col-lg-4" key={f.favoriteId}>
                {auth.isLoggedIn() &&
                <Link to={`/card/${f.favoriteId}`}>
                    {f.imageUrl !== null && f.imageUrl !== "" &&
                    <img src={f.imageUrl} className="card-img-top" alt={"Image for favorite titled " + f.title} />}
                    {f.gifUrl !== null &&  f.gifUrl !== "" &&
                    <img src={f.gifUrl} className="card-gif-mid" alt={"Gif for favorite titled " + f.title} />}
                    <div className="card-body">
                        <h1 className="card-title">{f.title}</h1>
                        <h2 className="card-source">Source: {f.source}</h2>
                        <h2 className="card-creator">By: {f.creator}</h2>
                        <h2 className="card-type">Favorite Item Type: {f.type}</h2>
                        <p></p>
                        <h2 ref={descriptionRef} className="card-description">Description:<p></p><p>{f.description}</p></h2>
                    </div>
                    <div className="card-footer">
                        <h3 className="card-createdOn">Created On: {f.createdOn}</h3>
                        <h3 className="card-updatedOn">Updated On: {f.updatedOn}</h3>
                    </div>
                </Link>}
                </div>
            </Grid>))}
    </Grid>
    : 
    <div className="col-sm-6 col-lg-4">No Favorites</div>}
    <div className="row">
        {location.state && 
        <div className="alert alert-info">
            {location.state.msg}
        </div>}
    </div>
    </>);
}

export default FavoriteGallery;