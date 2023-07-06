import { useRef, useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findAllFavoritesByUserId } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";
import { Grid } from "@mui/material";

function FavoriteGallery() {

    const descriptionRef = useRef(null);
    const [favorites, setFavorites] = useState([]);
    const location = useLocation();
    const auth = useContext(AuthContext);

    const currentUserId = localStorage.getItem('appUserId');
    useEffect(() => {
        findAllFavoritesByUserId(currentUserId)
            .then(data => setFavorites(data));
    }, [currentUserId]);

    return(<>
        {favorites.length > 0 ? 
        <Grid container spacing={4} columnSpacing={{ xs: 5 }}>
            {favorites.map(f => (
                <Grid item md={4}>
                    <div className="card col-sm-6 col-lg-4" key={f.favoriteId}>
                    {auth.isLoggedIn() &&
                    <Link to={`/card/${f.favoriteId}`}>
                        {f.imageUrl !== null && f.imageUrl !== "" &&
                        <img src={f.imageUrl} className="card-img-top" alt={"Title image for " + f.title} />}
                        {f.gifUrl !== null &&  f.gifUrl !== "" &&
                        <img src={f.gifUrl} className="card-gif-mid" alt={"Title gif for " + f.title} />}
                        <div className="card-body">
                            <h1 className="card-title">{f.title}</h1>
                            <h2 className="card-source">Source:<p>{f.source}</p></h2>
                            <h2 className="card-creator">By:<p>{f.creator}</p></h2>
                            <h2 className="card-type">Type:<p>{f.type}</p></h2>
                            <h2 className="card-description">Description:<p>{f.description}</p></h2>
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