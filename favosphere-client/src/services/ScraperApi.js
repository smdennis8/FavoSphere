const axios = require('axios');
const cheerio = require('cheerio');

callScraperFromUrl("https://en.wikipedia.org/wiki/Capsule_hotel");
//scrapeYouTube("https://www.youtube.com/watch?v=U4xOOnLBPB8");

function callScraperFromUrl(URL) {
    const url_domain_name = URL.split(".")[1];
    console.log(url_domain_name);
    switch (url_domain_name) {
        case "youtube":
          scrapeYouTube(URL);
          break;
        case "wikipedia":
          scrapeWikipedia(URL);
          break;
        default:
          break;
      }
}

async function scrapeYouTube(URL){
    const suggestion_data = {
        title: "",
        description: "",
        creator: "",
        source: "YouTube",
        imageUrl: ""
    };
    let tool;
    await axios.get(URL).then((response) => {
        const html = response.data;
        const $ = cheerio.load(html);
        suggestion_data.creator = $('#watch7-content span link:nth-child(2)').attr('content');
        suggestion_data.title = $('meta[property="og:title"]').attr('content');
        suggestion_data.description = $('meta[property="og:description"]').attr('content');
        suggestion_data.imageUrl = $('meta[property="og:image"]').attr('content');
        }).catch((err) => {
            console.log(err);
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
    }).catch((err) => {
        console.log(err);
    });
    await axios("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles="+suggestion_data.title)
    .then((response) => {
    const html = response.data;
    const $ = cheerio.load(html);
    suggestion_data.description = response.data.query.pages[Object.keys(response.data.query.pages)[0]].extract;
    }).catch((err) => {
        console.log(err);
    });
}