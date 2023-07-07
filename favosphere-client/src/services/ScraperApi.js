//const axios = require('axios');
//const cheerio = require('cheerio');
import axios from 'axios';
import cheerio from 'cheerio';

let results = {};
const cors_anywhere = "https://cors-anywhere.herokuapp.com/";

const getUrlSuggestions = async function(urlList, results) {
    
    const result = []; 
    urlList.forEach(element => {
        const url_domain_name = element.split(".")[1];
        switch (url_domain_name) {
            case "youtube":
                const y = scrapeYouTube(element);
                results.push(y);
            break;
            case "wikipedia":
                const w = scrapeWikipedia(element);
                results.push(w);
            break;
            default:
                results.push({id:-1, data: {}});
            break;
        }
    });


}


export async function callScraperFromUrl(URL) {
    const url_domain_name = URL.split(".")[1];
    switch (url_domain_name) {
        case "youtube":
          return await scrapeYouTube(URL);
          break;
        case "wikipedia":
          return await scrapeWikipedia(URL);
          break;
        default:
            return {
                title: "",
                description: "",
                creator: "",
                source: "",
                imageUrl: ""
            };
          break;
      }
}

const scrapeYouTube = async function(URL){
//async function scrapeYouTube(URL){
    const corsURL = cors_anywhere+URL;
    const suggestion_data = {
        title: "",
        description: "",
        creator: "",
        source: "YouTube",
        imageUrl: ""
    };
    await axios
    .get(corsURL).then((response) => {
        const html = response.data;
        const $ = cheerio.load(html);
        suggestion_data.creator = $('#watch7-content span link:nth-child(2)').attr('content');
        suggestion_data.title = $('meta[property="og:title"]').attr('content');
        suggestion_data.description = $('meta[property="og:description"]').attr('content');
        suggestion_data.imageUrl = $('meta[property="og:image"]').attr('content');
        }).catch((err) => {
            console.log(err);
        }).finally(() => {
            console.log("Youtube returning:");
        });
        return suggestion_data;
}

async function scrapeWikipedia(URL){
    const corsURL = cors_anywhere+URL;
    const suggestion_data = {
        title: "",
        description: "",
        creator: "",
        source: "Wikipedia",
        imageUrl: ""
    };
    //const wiki_api = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&pageids=21721040";
    
    await axios.get(corsURL).then((response) => {
        const html = response.data;
        const $ = cheerio.load(html);
        //suggestion_data.title = $(".mw-page-title-main").text();
        const rawTitle = $('meta[property="og:title"]').attr('content');
        if(rawTitle.includes("Wikipedia")){
            suggestion_data.title = rawTitle.split("-")[0].trim();
        } else {
            suggestion_data.title = rawTitle; 
        }
        suggestion_data.imageUrl = $('meta[property="og:image"]').attr('content');
    });
    //console.log(cors_anywhere+"https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles="+suggestion_data.title);

    // await axios.get(
    //     cors_anywhere+
    //     "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles="+
    //     suggestion_data.title)
    await axios({
        method: 'get',
        url: cors_anywhere+"https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles="+suggestion_data.title,
        headers: {'x-requested-with': 'XMLHttpRequest'}
      }).then((response) => {
            const html = response.data;
            const $ = cheerio.load(html);
            const d = response.data.query;
            suggestion_data.description = response.data.query.pages[Object.keys(response.data.query.pages)[0]].extract;
        }).catch((err) => console.log(err))
        .finally(() => {console.log("Wikipedia returning:"); 
        });
    return suggestion_data;
}

//const listSuggs = getUrlSuggestions(["https://en.wikipedia.org/wiki/Capsule_hotel","https://www.youtube.com/watch?v=U4xOOnLBPB8"], results);
//let dat;
//scrapeYouTube("https://www.youtube.com/watch?v=U4xOOnLBPB8").then(data => {dat = data;});

// let dat = null;
// (async () => {
//     // console.log(await callScraperFromUrl("https://www.youtube.com/watch?v=U4xOOnLBPB8"))
//     dat = await callScraperFromUrl("https://www.youtube.com/watch?v=U4xOOnLBPB8");
//     console.log(dat);
//  })()
// console.log("pause;");

// let dat = null;
// (async () => {
//     console.log(await scrapeWikipedia("https://en.wikipedia.org/wiki/Pok%C3%A9mon"))
//     // dat = await callScraperFromUrl("https://www.youtube.com/watch?v=U4xOOnLBPB8");
//     // console.log(dat);
//  })()
// console.log("pause;");

