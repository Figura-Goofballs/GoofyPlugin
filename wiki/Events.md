# GoofyPlugin Events
## `ERROR` event

```lua
function events.ERROR(msg)
    if msg == "" then -- Blank error (this should never happen)
        return true -- Stop error from occuring
    else
        return false -- Let error occur
    end
end
```
