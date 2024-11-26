local assertions = {
  -- Logging assertions
  [{"isDebugEnabled"}] = false;
  [{"isErrorEnabled"}] = true;
  [{"isInfoEnabled"}] = false;
  [{"isTraceEnabled"}] = false;
  [{"isWarnEnabled"}] = true;

  -- Regex assertions
  [{"regexSub", "this is a test string", " [^ ]{2} ", " "}] = "this a test string";
  --[{"regexMatch", "this is another test string", " [^ ]{2} "}] = " is ";

  [{"getBumpscocity"}] = 1000;
  [{"getAvatarColor", avatar:getUUID()}] = "#5865f2";
}

local assertionError = "$$UNIT_TEST_FAILED$$ goofy:%s expected value %s, but got value %s"

for funcAndArgs, expectedValue in pairs(assertions) do
  local func = funcAndArgs[1]
  table.remove(funcAndArgs, 1)

  local gotValue = goofy[func](goofy, table.unpack(funcAndArgs))

  assert(gotValue == expectedValue, assertionError:format(func, tostring(expectedValue), tostring(gotValue)))
end

