import { Link } from 'react-router-dom';

function LeftPanel(){
    return(<>
        <nav>
                <Link to={'/'}>Home</Link>
                <Link to={'/list'}>Profile</Link>
                <Link to={'/list'}>Favorites Gallery</Link>
                <Link to={'/list'}>Favorites Table</Link>
                <Link to={'/'}>Share Favorites</Link>
                <Link to={'/list'}>Favorites Staging</Link>
                <Link to={'/'}>Contact</Link>
        </nav>
    </>)
}

export default LeftPanel;