import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { GoogleOAuthProvider } from '@react-oauth/google';

// const axios = require("axios")
// const cheerio = require("cheerio")
// const htmlElementClass = $(".elementClass")
// const htmlElementId = $("#elementId")

// async function performScraping() {
//   const axiosResponse = await axios.request({
//       method: "GET",
//       url: "https://www.wikipedia.org",
//       headers: {
//         "User-Agent": 
//         "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
//       }
//   })
// }

// const $ = cheerio.load(axiosResponse.data)
// performScraping()

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <GoogleOAuthProvider clientId="193661643963-qo67e0embf7ktg66jr9cqjo5q2fabjla.apps.googleusercontent.com">
        <React.StrictMode>
            <App />
        </React.StrictMode>
  </GoogleOAuthProvider>,
    document.getElementById('root')
);