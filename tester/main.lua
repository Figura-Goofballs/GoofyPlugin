local tests, total, success, errors = listFiles "tests", 0, 0, {}
goofy:infoToLog("running " .. #tests .. " tests")
for _, testName in ipairs(tests) do
	goofy:infoToLog("running test " .. testName)
	local ok, err = pcall(require, "tests" .. testName)
	total = total + 1
	if ok then
		success = success + 1
	else
		errors[testName] = err
	end
end
if total == success then
	goofy:infoToLog("All tests pass!")
else
	goofy:errorToLog("Test failures encountered!")
	for name, err in pairs(errors) do
		goofy:errorToLog("Test '" .. name .. "': " .. err)
	end
end
-- goofy:exitWith(total - success)
