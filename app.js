const express = require('express');
const app = express();
const path = require('path');
const router = express.Router();
const https = require('https');
const request = require('request');
const { response } = require('express');
const fetch = require('node-fetch');
const Geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');
var url= require('url');


app.listen(3000, (req, res) =>{
	console.log('Express API  is running at port 3000')

})


// app.get("/",(req, res) => {

//res.send(Geohash.encode(52.20, 0.12, 6));
//   //res.send("Hello from server")
// });

app.get("/api/keyword/:autokey", (req, res) => {
   var autokey = req.params.autokey;
      fetch('https://app.ticketmaster.com/discovery/v2/suggest?apikey=X6u9EWgGffYrWEcBL4NUJRYfH39R4A78&keyword='+autokey)
      .then(response => response.json())
      .then(data => {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Cache-Control", "no-cache");
        res.send(data);
      })
      .catch(error => console.error(error))
      
    });


app.get("/api/searchresult/:responsekey/:responsecat/:distancevalue/:measure/:hash",(req, res) => {
  //console.log(geohash.encode(37.8324, 112.5584));
  var hash = req.params.hash;
  console.log(hash);
  var responsekey = req.params.responsekey;
  var distancevalue = req.params.distancevalue;
  var measure = req.params.measure;
  var responsecat = req.params.responsecat;
  if (responsecat =='default'){
    responsecat = ""
  }
  fetch('https://app.ticketmaster.com/discovery/v2/events.json?apikey=X6u9EWgGffYrWEcBL4NUJRYfH39R4A78&keyword='+responsekey+'&segmentId='+responsecat+'&radius='+distancevalue+'&unit='+measure+'&geoPoint='+hash+'&sort=date,asc')
      .then(response => response.json())
      .then(data => {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Cache-Control", "no-cache");
        res.send(data);
      })
      .catch(error => console.error(error))
});

app.get("/api/googleapi/:loc_details",(req, res) => {
  var loc_details = req.params.loc_details;

  // var responsekey = req.params.responsekey;
  // var responsecat = req.params.responsecat;
  fetch("https://maps.googleapis.com/maps/api/geocode/json?address="+loc_details+"&key=AIzaSyA144ETcnXTpwaW6hNt3jDRbG8E659IPTQ")
      .then(response => response.json())
      .then(data => {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Cache-Control", "no-cache");
        res.send(data);
      })
      .catch(error => console.error(error))
});

app.get("/api/geohash/:lat/:long",(req, res) => {
  var lat = req.params.lat;
  var long = req.params.long;
  var data = Geohash.encode(lat, long, 6);
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Cache-Control", "no-cache");
  res.send({"status":"200","url":data})

});

   app.post('/enroll', function(req, res) {
     console.log(req.body)
     res.status(200).send({"message":"Data received"})                 
     });

     app.get("/api/eventdetails/:evdetails",(req, res) => {
      var evdetails = req.params.evdetails;
    
      // var responsekey = req.params.responsekey;
      // var responsecat = req.params.responsecat;
      fetch( "https://app.ticketmaster.com/discovery/v2/events/"+evdetails+".json?apikey=X6u9EWgGffYrWEcBL4NUJRYfH39R4A78&")
          .then(response => response.json())
          .then(data => {
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Cache-Control", "no-cache");
            res.send(data);
          })
          .catch(error => console.error(error))
    });

var spotifyApi = new SpotifyWebApi({
    clientId: '237fa28c584647149cbd33200fef12e3',
    clientSecret: '2941bd0612f44c76a23dc43a883a4833',
    redirectUri: 'http://www.example.com/callback'
});

spotifyApi.setAccessToken ('')
spotifyApi.clientCredentialsGrant().then(
  function(data) {
    console.log('The access token expires in ' + data.body['expires_in']);
    console.log('The access token is ' + data.body['access_token']);

    // Save the access token so that it's used in future calls
    spotifyApi.setAccessToken(data.body['access_token']);
  },
  function(err) {
    console.log('Something went wrong when retrieving an access token', err);
  }
);
app.get('/spotify',function(req, res)
    {
      spotifyApi.clientCredentialsGrant().then(
        function(data) {
          console.log('The access token expires in ' + data.body['expires_in']);
          console.log('The access token is ' + data.body['access_token']);
      
          // Save the access token so that it's used in future calls
          spotifyApi.setAccessToken(data.body['access_token']);
        },
        function(err) {
          console.log('Something went wrong when retrieving an access token', err);
        }
      );
     
        res.setHeader("Content-Type","text/plain");
        res.setHeader("Access-Control-Allow-Origin","*");
        var params = url.parse(req.url, true).query;
        console.log(params)
      var url_text_spotify= "https://api.spotify.com/v1/search?q="+params.Keyword+"type=artist";
   
            
        spotifyApi.searchArtists(params.Keyword)
      .then(function(data) {
        console.log('Search artists by ', data.body);
        res.send(data.body);
      }, function(err) 
      {
        console.error(err);
      });        
                   Authorization: 'Bearer BQDA9j6B41WNrpvsKmI7L7O-V8gR_lOi2La_jIvHCPSbCNGiIyh65613Fobr6gZgXSTQ0YKuTypeUgpftdfEH-Jahge5DtHfv8a8tSYflTN8vTUrfGVp8mpoARIzko6-KzhHe-AZKxt0_8HVuHo3Cv3AOkitv4Y0qQ';
    });

    // Start the server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log('Press Ctrl+C to quit.');
});
// [END gae_node_request_example]


app.use(express.static(path.join(__dirname,'/dist/entertainmentsearch')));


app.use('/*', (req, res) => {
	res.sendFile(path.join(__dirname+'/dist/entertainmentsearch/index.html'));
})
