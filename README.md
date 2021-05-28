# PlaceholderWeb - JSON API for Placeholders
Spigot plugin to retrieve PlaceholderAPI placeholders using a JSON api.

### Config
```
# port to listen on
port: 8000
# requires all requests to have a key attached
auth: false
# used if auth is enabled - use /pweb generate to generate new keys.
keys: []
# debug mode - prints request information in console.
debug: false
```
### Usage
Requests are made in the format `/<playerName>/<placeholders>`. 

For example, if you were running your server locally and have the port set to `8000` in the config,
you could get the `player_name` and `player_ping` placeholders for player `ExplosiveNight` by going to http://localhost:8000/ExplosiveNight/player_name,player_ping. This returns:
```
{
  "player_ping": "86",
  "player_name": "ExplosiveNight"
}
```
You may include commas in the request to retrieve multiple placeholders at one time.
### Credits
- [ExtendedClip](https://github.com/extendedclip) (PlaceholderAPI)
- [Tipsy](https://github.com/tipsy) (Javalin Framework)
