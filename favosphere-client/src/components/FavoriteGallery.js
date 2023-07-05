import { useRef, useContext, useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { findAllFavoritesByUserId } from "../services/FavoriteApi";
import AuthContext from "../contexts/AuthContext";

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

    useEffect(() => {
        const calculateEllipsis = () => {
            if (descriptionRef.current) {
                const element = descriptionRef.current;
                const maxLines = 3; // Adjust the maximum number of lines to display
        
                // Set explicit line-height on the element
                element.style.lineHeight = "20px";
        
                // Calculate the height based on the line-height and maxLines
                const lineHeight = parseInt(window.getComputedStyle(element).lineHeight);
                const maxHeight = lineHeight * maxLines;
        
                if (element.scrollHeight > maxHeight) {
                    element.style.display = "-webkit-box";
                    element.style.WebkitLineClamp = maxLines;
                    element.style.WebkitBoxOrient = "vertical";
                    element.style.overflow = "hidden";
                    element.style.textOverflow = "ellipsis";
                }
            }
        };
    
        calculateEllipsis();
    }, [favorites.length]);

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
                        <h2 className="card-source">Source:<p>{f.source}</p></h2>
                        <h2 className="card-creator">By:<p>{f.creator}</p></h2>
                        <h2 className="card-type">Type:<p>{f.type}</p></h2>
                        <h2 ref={descriptionRef} className="card-description">{f.description}</h2>
                        <p></p>
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