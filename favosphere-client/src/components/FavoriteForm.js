import { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { findFavoriteById, createFavorite, updateFavorite, deleteFavoriteById } from "../services/FavoriteApi";
import Errors from "./Errors";
import AuthContext from "../contexts/AuthContext";

const EMPTY_FAVORITE = {
    favoriteId: 0,
    userId: 0,
    url: null,
    source: null,
    creator: null,
    type: null,
    title: null,
    description: null,
    gifUrl: null,
    imageUrl: null,
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
        const { name, type, checked, value } = event.target;

        if (type === 'checkbox') {
            nextFavorite[name] = checked;
        }
        else {
            let nextValue = value;
            if (type === 'number') {
                nextValue = parseFloat(nextValue, 10);
                if (isNaN(nextValue)) {
                    nextValue = value;
                }
            }
            nextFavorite[name] = nextValue;
        }
        setFavorite(nextFavorite);
    }

    const handleSaveFavorite = (event) => {
        event.preventDefault();
        console.log('handleSaveFavorite called');

        if (favorite.favoriteId === 0) {
            favorite.userId = localStorage.getItem('appUserId');
            createFavorite(favorite)
            .then(() => {
                console.log('createFavorite resolved');
                navigate("/gallery", {
                    state: { msg: `Your favorite titled: '${favorite.title}' was added!` }
                });
            })
            .catch(err => {
            console.log('createFavorite rejected:', err);
                setErrors(err)
            });

        } 
        else {
            updateFavorite(favorite)
            .then(() => {
                navigate("/gallery", {
                    state: {
                        msgType: 'success',
                        msg: `Your favorite titled: '${favorite.title}' was updated!`
                    }
                });
            })
            .catch(err => setErrors(err));
        }
    }

    const handleDeleteFavorite = (favoriteId) => {
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
        else {
            navigate("/card/favoriteId", {
                state: { msg: `` }
            });
        }
    }    

    return <div className="container-fluid">
        <form onSubmit={handleSaveFavorite}>

            <div className="mb-3">
                <label htmlFor="source" className="form-label">Source</label>
                <input type="text" className="form-control form-control-add" id="source" name="source" value={favorite.source} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="creator" className="form-label">Creator</label>
                <input type="text" className="form-control form-control-add" id="creator" name="creator" value={favorite.creator} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="type" className="form-label">Type</label>
                <input type="text" className="form-control form-control-add" id="type" name="type" value={favorite.type} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="title" className="form-label">Title</label>
                <input type="text" className="form-control form-control-add" id="title" name="title" value={favorite.title} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="description" className="form-label">Description</label>
                <textarea className="form-control form-control-add" id="description" name="description" value={favorite.description} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="url" className="form-label">URL</label>
                <input type="text" className="form-control form-control-add" id="url" name="url" value={favorite.url} onChange={handleChange} required />
            </div>

            <div className="mb-3">
                <label htmlFor="gifUrl" className="form-label">Gif URL</label>
                <input type="text" className="form-control form-control-add" id="gifUrl" name="gifUrl" value={favorite.gifUrl} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="imageUrl" className="form-label">Image URL</label>
                <input type="text" className="form-control form-control-add" id="imageUrl" name="imageUrl" value={favorite.imageUrl} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customTitle" className="form-label">Custom Title</label>
                <input type="checkbox" id="customTitle" className="box-for-checking" name="customTitle" checked={favorite.customTitle} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customDescription" className="form-label">Custom Description</label>
                <input type="checkbox" id="customDescription" className="box-for-checking" name="customDescription" checked={favorite.customDescription} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customImage" className="form-label">Custom Image</label>
                <input type="checkbox" id="customImage" className="box-for-checking" name="customImage" checked={favorite.customImage} onChange={handleChange} />
            </div>

            <div className="mb-3">
                <label htmlFor="customGif" className="form-label">Custom Gif</label>
                <input type="checkbox" id="customGif" className="box-for-checking" name="customGif" checked={favorite.customGif} onChange={handleChange} />
            </div>

            <div className="mb-3 buttons-crt-acc">
                <button type="submit" className="btn btn-primary button-prm">Submit</button>
                <Link to="/gallery" type="button" className="btn btn-secondary button-scnd">Cancel</Link>
                {auth.isLoggedIn() && favorite.favoriteId !== 0 &&
                <button className="btn btn-danger btn-sm button-scnd delete necessary" onClick={() => handleDeleteFavorite(favorite.favoriteId)}>
                    <i className="bi bi-trash"></i> Delete
                </button>            
            }
            </div>
            
        </form>
        <Errors errors={errors} />
    </div>;
}

export default FavoriteForm;