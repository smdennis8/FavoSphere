import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { GoogleOAuthProvider } from '@react-oauth/google';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <GoogleOAuthProvider clientId="193661643963-qo67e0embf7ktg66jr9cqjo5q2fabjla.apps.googleusercontent.com">
        <React.StrictMode>
            <App />
        </React.StrictMode>
  </GoogleOAuthProvider>,
    document.getElementById('root')
);
