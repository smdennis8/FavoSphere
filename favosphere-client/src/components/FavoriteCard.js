import { useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findFavoriteById } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";

function FavoriteCard() {

    <div className="col-sm-6 col-lg-4" key={f.favoriteId}>
    <div className="card">
        <img src={f.imageUrl} className="card-img-top" alt={"Title image for " + f.title} />
        <div className="card-body">
            <h5 className="card-title">{f.title}</h5>
            {auth.isLoggedIn() &&
                <Link to={`/edit/${f.favoriteId}`} className="btn btn-primary">Edit</Link>}
            {auth.hasRole('ADMIN') &&
                <Link to={`/delete/${f.favoriteId}`} className="btn btn-danger">Delete</Link>}
        </div>
    </div>
    </div>
}

export default FavoriteCard;