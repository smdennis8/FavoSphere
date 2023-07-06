//const axios = require('axios');
//const cheerio = require('cheerio');
import axios from 'axios';
import cheerio from 'cheerio';

const results = [];

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


async function callScraperFromUrl(URL) {
    const url_domain_name = URL.split(".")[1];
    console.log(url_domain_name);
    switch (url_domain_name) {
        case "youtube":
          return await scrapeYouTube(URL);
          break;
        case "wikipedia":
          return await scrapeWikipedia(URL);
          break;
        default:
          break;
      }
}

export const scrapeYouTube = async function(URL){
//async function scrapeYouTube(URL){
    const suggestion_data = {
        title: "",
        description: "",
        creator: "",
        source: "YouTube",
        imageUrl: ""
    };
    await axios({method: 'get',withCredentials: false})
    .get(URL).then((response) => {
        const html = response.data;
        const $ = cheerio.load(html);
        suggestion_data.creator = $('#watch7-content span link:nth-child(2)').attr('content');
        suggestion_data.title = $('meta[property="og:title"]').attr('content');
        suggestion_data.description = $('meta[property="og:description"]').attr('content');
        suggestion_data.imageUrl = $('meta[property="og:image"]').attr('content');
        return suggestion_data;
        }).catch((err) => {
            console.log(err);
        }).finally(() => {
            console.log(suggestion_data);
        });
}

async function scrapeWikipedia(URL){
    const suggestion_data = {
        title: "",
        description: "",
        creator: "",
        source: "Wikipedia",
        imageUrl: ""
    };

    const wiki_api = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&pageids=21721040";
    await axios.get(URL).then((response) => {
    const html = response.data;
    const $ = cheerio.load(html);
    suggestion_data.title = $(".mw-page-title-main").text();
    suggestion_data.imageUrl = $('meta[property="og:image"]').attr('content');
    });
    await axios.get("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles="+suggestion_data.title)
    .then((response) => {
    const html = response.data;
    const $ = cheerio.load(html);
    suggestion_data.description = response.data.query.pages[Object.keys(response.data.query.pages)[0]].extract;
    return suggestion_data;
    }).finally(() => {
        console.log(suggestion_data);   
    });
}

//const listSuggs = getUrlSuggestions(["https://en.wikipedia.org/wiki/Capsule_hotel","https://www.youtube.com/watch?v=U4xOOnLBPB8"], results);

//console.log(listSuggs);
//console.log("Results:")
//console.log(results);
