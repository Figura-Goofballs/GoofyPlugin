# GoofyPlugin Events
## `ERROR` event

```lua
function events.ERROR(msg)
    if msg == "" then -- Blank error (this should never happen)
        return false -- Stop error from occuring
    else
        return true -- Let error occur
    end
end
```
