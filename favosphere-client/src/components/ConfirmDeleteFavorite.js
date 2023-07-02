import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { findFavoriteById, deleteFavoriteById } from "../services/FavoriteApi";

function ConfirmDeleteFavorite() {

  const [favorite, setFavorite] = useState({ title: '' });

  const { id } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    findFavoriteById(id)
      .then(data => setFavorite(data))
      .catch(() => {
        navigate("/", {
          state: { msg: `Favorite: ${id} was not found.` }
        });
      });
  }, [id, navigate]);

  const handleDelete = () => {
    deleteFavoriteById(id)
      .then(res => {
        navigate("/gallery", {
          state: { msg: `${favorite.title} was deleted.` }
        });
      })
      .catch(() => {
        navigate("/gallery", {
          state: { msg: `Favorite: ${id} was not found.` }
        });
      });
  }

  return <div className="container-fluid">
    <div className="row">
      <div className="col">
        <h2>{favorite.title}</h2>
      </div>
    </div>
    <div className="row">
      <div className="col">
        <div className=" alert alert-danger">
          Are you sure you want to permanently delete this favorite card?
        </div>
      </div>
    </div>
    <div className="row">
      <div className="col">
        <button type="button" className="btn btn-danger" onClick={handleDelete}>Delete Forever</button>
        <Link to="/gallery" type="button" className="btn btn-secondary">Cancel</Link>
      </div>
    </div>
  </div>
}

export default ConfirmDeleteFavorite;