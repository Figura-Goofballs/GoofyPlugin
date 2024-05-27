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
function events.ENTITY_RENDER(delta, entity) -- delta is the time between since the previous tikc, pass it into entity:getPos() or math.lerp for smoother stuff
    return entity:getType() ~= "minecraft:zombie" -- Don't render zombies
end
```
