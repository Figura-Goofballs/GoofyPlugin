# Regex Functions
`goofy:regexMatch(str, pattern)`: Returns all matches in `str` based on `pattern` (a  Java flavor regex pattern)

`goofy:regexSub(str, pattern, replacement)`: Returns a string with all matches of `pattern` (a  Java flavor regex pattern) in `str` replaced with `replacement`


Here's an example on the usage of this:

```lua
local inputString = "www.google.com https://example.com/test/ garbage data"

for _, v in ipairs(goofy:regexMatch(inputString, "") do
    print("Got url " .. v)
end
```

The output would be:
```
Got url www.google.com
Got url example.com/test/
```

<hr>

You can make and test regex patterns at [regex101.com](https://regex101.com/) (we only support the pattern itself, so only use the stuff in-between the slashes)

