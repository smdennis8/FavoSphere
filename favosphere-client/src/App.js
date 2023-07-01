import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";

import FavoriteForm from "./components/FavoriteForm";
import LoginForm from "./components/LoginForm";
import AuthContext from "./contexts/AuthContext";
import { refreshToken, signOut } from "./services/AuthApi";
import NotFound from "./NotFound";

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
      return !!user.email;
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

  const maybeRedirect = (component, role) => {
    if (!auth.isLoggedIn() || (role && !auth.hasRole(role))) {
      return <Navigate to="/" />;
    }
    return component;
  }

  return (<>
    <AuthContext.Provider value={auth}>
      <Router>
        <Routes>
          <Route
            path="/login"
            element={<LoginForm />} />
          <Route
            path="/add"
            element={auth.isLoggedIn()
              ? <FavoriteForm />
              : <Navigate to="/" />} />
          <Route
            path="/edit/:id"
            element={auth.isLoggedIn()
              ? <FavoriteForm />
              : <Navigate to="/login" />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </AuthContext.Provider>
  </>);
}

export default App;
