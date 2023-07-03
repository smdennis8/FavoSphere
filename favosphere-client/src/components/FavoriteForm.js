import { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { findFavoriteById, createFavorite, updateFavorite, deleteFavoriteById } from "../services/FavoriteApi";
import Errors from "./Errors";
import AuthContext from "../contexts/AuthContext";

const EMPTY_FAVORITE = {
    favoriteId: 0,
    userId: 0,
    url: "",
    source: "",
    creator: "",
    type: "",
    title: "",
    description: "",
    gifUrl: "",
    imageUrl: "",
    createdOn: null,
    updatedOn: null,
    customTitle: false,
    customDescription: false,
    customImage: false,
    customGif: false
};

function FavoriteForm() {

    const [favorite, setFavorite] = useState(EMPTY_FAVORITE);
    const [favorites, setFavorites] = useState([]);
    const [errors, setErrors] = useState([]);
    const url = 'http://localhost:8080/favorite';
    const { id } = useParams();

    const navigate = useNavigate();

    const auth = useContext(AuthContext);

    useEffect(() => {
        if (id) {
            findFavoriteById(id)
            .then(data => setFavorite(data))
            .catch(err => {
                navigate("/error", {
                state: { msg: err }
                });
            });
        } 
        else {
            setFavorite(EMPTY_FAVORITE);
        }
    }, [id, navigate]);

    const handleChange = (event) => {
        const nextFavorite = { ...favorite };


        let nextValue = event.target.value;
        if (event.target.type === 'number') {
            nextValue = parseFloat(nextValue, 10);
            if (isNaN(nextValue)) {
                nextValue = event.target.value;
            }
        }
        nextFavorite[event.target.name] = nextValue;

        setFavorite(nextFavorite);
    }

    const handleSaveFavorite = (event) => {
        event.preventDefault();

        if (favorite.favoriteId === 0) {

            createFavorite(favorite)
            .then(() => {
                navigate("/favorite", {
                    state: { msg: `Favorite #${favorite.favoriteId} was added!` }
                });
            })
            .catch(err => setErrors(err))
        } 
        else {
            updateFavorite(favorite)
            .then(() => {
                navigate("/favorite", {
                    state: {
                        msgType: 'success',
                        msg: `Favorite #${favorite.favoriteId} was updated!`
                    }
                });
            })
            .catch(err => setErrors(err));
        }
    }

    const handleDeleteFavorite = (favoriteId) => {

        // const favorite = findFavoriteById(id);
        if (window.confirm(`CONFIRM DELETE\n\nFavorite with title:\n"${favorite.title}"?`)) {
            deleteFavoriteById(favoriteId)
            .then(() => {
                navigate("/gallery", {
                    state: { msg: `"${favorite.title}" was deleted.` }
                });
            })
            const init = {
                method: 'DELETE'
            };
            fetch(`${url}/${favoriteId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        const newFavorites = favorites.filter(favorites => favorites.favoriteId !== favoriteId);
                        setFavorite(newFavorites);
                    } else {
                        return Promise.reject(`Unexpected Status code: ${response.status}`);
                    }
                })
                .catch(console.log);
        }
    }    

    return <div className="container-fluid">
        <form onSubmit={handleSaveFavorite}>

            <div className="mb-3">
                <label htmlFor="source" className="form-label">Source</label>
                <input type="text" className="form-control" id="source" name="source" value={favorite.source} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="creator" className="form-label">Creator</label>
                <input type="text" className="form-control" id="creator" name="creator" value={favorite.creator} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="type" className="form-label">Type</label>
                <input type="text" className="form-control" id="type" name="type" value={favorite.type} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="title" className="form-label">Title</label>
                <input type="text" className="form-control" id="title" name="title" value={favorite.title} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="description" className="form-label">Description</label>
                <input type="text" className="form-control" id="description" name="description" value={favorite.description} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="url" className="form-label">URL</label>
                <input type="text" className="form-control" id="url" name="url" value={favorite.url} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="gifUrl" className="form-label">Gif URL</label>
                <input type="text" className="form-control" id="gifUrl" name="gifUrl" value={favorite.gifUrl} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="imageUrl" className="form-label">Image URL</label>
                <input type="text" className="form-control" id="imageUrl" name="imageUrl" value={favorite.imageUrl} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="createdOn" className="form-label">Created On</label>
                <input type="date" className="form-control" id="createdOn" name="createdOn" value={favorite.createdOn} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="updatedOn" className="form-label">Updated On</label>
                <input type="date" className="form-control" id="updatedOn" name="updatedOn" value={favorite.updatedOn} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="customTitle" className="form-label">Custom Title</label>
                <input type="checkbox" className="form-control" id="customTitle" name="customTitle" value={favorite.customTitle} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customDescription" className="form-label">Custom Description</label>
                <input type="checkbox" className="form-control" id="customDescription" name="customDescription" value={favorite.customDescription} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customImage" className="form-label">Custom Image</label>
                <input type="checkbox" className="form-control" id="customImage" name="customImage" value={favorite.customImage} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customGif" className="form-label">Custom Gif</label>
                <input type="checkbox" className="form-control" id="customGif" name="customGif" value={favorite.customGif} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <button type="submit" className="btn btn-primary">Submit</button>
                <Link to="/gallery" type="button" className="btn btn-secondary">Cancel</Link>
                {auth.isLoggedIn() && favorite.favoriteId !== 0 &&
                <button className="btn btn-danger btn-sm" onClick={() => handleDeleteFavorite(favorite.favoriteId)}>
                    <i className="bi bi-trash"></i> Delete
                </button>
            }
            </div>
            
        </form>
        <Errors errors={errors} />
    </div>;
}

export default FavoriteForm;