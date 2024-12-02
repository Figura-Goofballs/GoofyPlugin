# Miscellaneous Functions

- `goofy:loadAvatar(uuid)` load's a player's avatar based on their UUID. useful for accessing avatar variables without someone placing the head down or the person joining the game, e.g. Chloe's Piano  
- `goofy:reloadAvatar(uuid)` reloads a player's avatar based on their UUID.  
- `goofy:stopAvatar()` stops the avatar as if an error occured 
- `goofy:loadLocalAvatar(path)` loads a local avatar 
- `goofy:uploadAvatar()` uploads the current avatar

* `goofy:checkFeatures(tbl)`\
  Checks compatibility with each feature (string key) listed in `tbl`. Each version specified (numeric value) must be less than or equal to the client's version of that feature, and greater than or equal to the version of that feature's last breaking change. If any of these are false, the viewer will be advised to update Goofy. The following features are currently implemented:
  | Feature | Current | Compatible | Description |
  | :- | :-: | :-: | :- |
  | `BASE` | 0 | 0 | Represents features prior to the feature-checking system.
