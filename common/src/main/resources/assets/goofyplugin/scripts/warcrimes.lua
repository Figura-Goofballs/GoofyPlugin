do
  local BackendAPI = backend
  
  local newMtable = getmetatable(goofy)
  newMtable.__index["backend"] = BackendAPI

  setmetatable(goofy, newMtable)

  backend = nil
end

