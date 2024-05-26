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

## `ENTITY_RENDER` event

```lua
function events.ENTITY_RENDER(entity)
    return entity:getType() ~= "minecraft:zombie" -- Don't render zombies
end
```
