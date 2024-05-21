# Logging to the Minecraft log
**We are aware that this is in base figura (PR [#214](https://github.com/FiguraMC/Figura/pull/214)), but this still gives more freedom with how you can log.**

If your script prints a large amount of debug information, you likely do not want it to fill up chat. You can use the functions `goofy:infoToLog(...)` to print text to the Minecraft log, which will appear in your launcher and latest.log, but not chat. However, logging will only be enabled if you have the permission (and if on non host, the client has log others enabled). You can use `goofy:isInfoEnabled()` to check if logging is enabled.

For example, you can do the following:  
```lua
if goofy then -- Check if plugin is installed
  if goofy:isDebugEnabled() then
    goofy:debugToLog("This is a debug message!")
  else
    goofy:infoToLog("This is an info message because debug is not enabled!")
  end
end
```

## Alternative Log Types

Scripts can log using any supported log type, if enabled. The following log types are present:

* `error`, always enabled and usually highlighted red in logs
* `warn`, always enabled and usually highlighted yellow in logs
* `info`, usually enabled and the most common log type
* `debug`, which is hidden from latest.log and your launcher's logs (but still present in debug.log iirc), and can be used for verbose output
* `trace`, which is hidden by default everywhere, so not sure why you'd use this.

