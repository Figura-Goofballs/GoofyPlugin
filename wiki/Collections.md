# Collection Operations

For bulk operations on tables, the `collections` API is also provided. This offloads large operations on tables to Java code, allowing you to save instructions and (somewhat) increasing speed.

## Elementwise Operations

* `collection:map(tbl: [a], fn: a → b) → [b]`
  Runs a function on each element of the array, replacing each element with the return value in-place. Returns the table modified. Similar to `<$>` operator in some languages.
  ```lua
  local tbl = {1, 2, 3}
  local tbl2 = collection:map(tbl, function(a) return a + 1 end)
  assert(tbl == tbl2) -- reference equality
  assert(tbl[1] == 2 and tbl[2] == 3 and tbl[3] == 4)
  ```
* `collection:each(tbl: [a], fn: a → _) → [a]`
  Like `collection:map`, but does not modify the table. The function's return value is discarded and the original table is returned.
  ```lua
  local tbl = {1, 2, 3}
  local tbl2 = collection:each(tbl, function(a) print(a) end)
  assert(tbl == tbl2)
  assert(tbl[1] == 1 and tbl[2] == 2 and tbl[3] == 3)
  ```
* `collection:flatMap(tbl: [a], fn: a → [b]) → [b]`
  Like `collection:map`, but the return values of the function are treated as arrays. All such arrays are merged in a newly created array, which is returned. Similar to `>>=` operator in some languages.
  ```lua
  local tbl = {1, 2, 3}
  local tbl2 = collection:flatMap(tbl, function(a) return {a, a+2} end)
  assert(tbl ~= tbl2) -- returns a newly allocated table
  assert(tbl2[1] == 1 and tbl2[2] == 3 and tbl2[3] == 2
     and tbl2[4] == 4 and tbl2[5] == 3 and tbl2[6] == 5)
  ```
* `collection:filter(tbl: [a], fn: a → boolean) → [a]`
  Returns a new table composed of only the elements for which the filter returns `true`. The original table is left unmodified.
  ```lua
  local tbl = {1, 2, 3, 4, 5, 6}
  local tbl2 = collection:filter(tbl, function(a) return a % 2 == 0 end)
  assert(tbl ~= tbl2)
  assert(tbl2[1] == 2 and tbl2[2] == 4 and tbl2[3] == 6)
  ```

## Reductive Operations

Each of these functions collect all values in the array, combining them into one result. All of these implemented so far are arithmetic, and only work for homogenous arrays of numbers (4x speed increase) or vectors (3x speed increase). An empty array returns `nil`.

* `collection:sum(vals: [a]) → a if a ∈ number | Vector2 | Vector3 | Vector4`
  Sum of all values. Vectors are summed pairwise.
  ```lua
  assert(collection:sum{1, 2, 3} == 6)
  assert(collection:sum{vec(1, 2), vec(3, 4), vec(5, 6)} == vec(9, 12))
  ```
* `collection:difference(vals: [a]) → a if a ∈ number | Vector2 | Vector3 | Vector4`
  Difference of all values, equivalent to negative sum. Vectors are summed pairwise.
  ```lua
  assert(collection:difference{1, 2, 3} == -6)
  assert(collection:difference{vec(1, 2), vec(3, 4), vec(5, 6)} == vec(-9, -12))
  ```
* `collection:product(vals: [a]) if a ∈ number | Vector2 | Vector3 | Vector4`
  Product of all values. Vectors are summed pairwise.
  ```lua
  assert(collection:product{2, 3, 4} == 24)
  assert(collection:product{vec(1, 2), vec(3, 4), vec(5, 6)} == vec(15, 48))
  ```
* `collection:quotient(vals: [a]) → a if a ∈ number | Vector2 | Vector3 | Vector4`
  Quotient of all values, equivalent to negative product. Vectors are summed pairwise.
  ```lua
  assert(collection:quotient{1, 2, 4} == 0.125)
  assert(collection:quotient{vec(2, 1), vec(4, 2), vec(1, 4)} == vec(0.125, 0.125))
  ```
