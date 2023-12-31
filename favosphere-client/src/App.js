import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import FavoriteForm from "./components/FavoriteForm";
import EmailFavoriteForm from "./components/EmailFavoriteForm";
import FavoriteGallery from "./components/FavoriteGallery";
import LeftPanel from "./components/LeftPanel";
import LoginForm from "./components/LoginForm";
import AuthContext from "./contexts/AuthContext";
import { refreshToken, signOut } from "./services/AuthApi";
import NotFound from "./NotFound";
import FavoriteStaging from "./components/FavoriteStaging";
import CreateAccountForm from "./components/CreateAccountForm";
import Profile from "./components/Profile";
import MaybeShowLeftPanel from "./MaybeShowLeftPanel";

// import FavoriteCard from "./components/FavoriteCard";

const EMPTY_USER = {
  username: '',
  roles: []
};

const WAIT_TIME = 1000 * 60 * 14;

function App() {

  const [user, setUser] = useState(EMPTY_USER);

  const refreshUser = useCallback(() => {
    refreshToken()
      .then(existingUser => {
        setUser(existingUser);
        setTimeout(refreshUser, WAIT_TIME);
      })
      .catch(err => {
        console.log(err);
      });
  }, []);

  useEffect(() => {
    refreshUser();
  }, [refreshUser]);

  const auth = {
    user: user,
    isLoggedIn() {
      return !!user.username;
    },
    hasRole(role) {
      return user.roles.includes(role);
    },
    onAuthenticated(user) {
      setUser(user);
      setTimeout(refreshUser, WAIT_TIME);
    },
    signOut() {
      setUser(EMPTY_USER);
      signOut();
    }
  };

  // const maybeRedirect = (component, role) => {
  //   if (!auth.isLoggedIn() || (role && !auth.hasRole(role))) {
  //     return <Navigate to="/" />;
  //   }
  //   return component;
  // }

  return (<>
    <AuthContext.Provider value={auth}>
      <Router>
        <div className="container-fav">
          <MaybeShowLeftPanel>
            <LeftPanel />
          </MaybeShowLeftPanel>
          <div className="content-fav">
            <Routes>
              <Route
                path="/" element={<LoginForm />} />
              <Route
                path="/create-account" element={<CreateAccountForm />} />
              <Route
                path="/staging"
                element={auth.isLoggedIn()
                  ? <FavoriteStaging />
                  : <Navigate to="/" />} />
              <Route
                path="/profile"
                element={auth.isLoggedIn()
                  ? <Profile />
                  : <Navigate to="/" />} />
              <Route
                path="/add-from-email"
                element={auth.isLoggedIn()
                  ? <EmailFavoriteForm />
                  : <Navigate to="/" />} />
              <Route
                path="/add"
                element={auth.isLoggedIn()
                  ? <FavoriteForm />
                  : <Navigate to="/" />} />
              <Route
                path="/card/:id"
                element={auth.isLoggedIn()
                  ? <FavoriteForm />
                  : <Navigate to="/card/:id" />} />
              <Route path="/notfound" element={<NotFound />} />
              <Route path="/gallery" element={<FavoriteGallery/>}/>   {/*Add Auth Login Back*/}
            </Routes>
          </div>
        </div>
      </Router>
    </AuthContext.Provider>
  </>);
}

export default App;