# room-adventure-java

## Assignment for CSC132

A short room-adventure style console game written in Java
[GitHub Repository Link](https://github.com/laundrylens/room-adventure-java)

## Feature Contributions

### Jesse Llewellyn (jcllewellyn)

- `handleUse()`: Allows for usage of select items
- Made `Inventory` an `ArrayList`: Allows the inventory to function more like
  a Python list and simplifies code
- Added SecretWin and Win: Uses `handleUse` function to implement a
  way to win and an easter egg
- Fixed `Room` Class: Integrated `Item` Class, and fixed `toString` to be
  compatible w/ pokeballRoom

### Will Langley (luaemu or "keda@tanuki.codes")

- File structure: Moved classes to separate files
  and used `package` declarations for `include`s,
  improving the organization of code.
- Directory structure follows reverse domain name standard.
- `Item` class: Replaces the system of `items`, `grabbables`, and `itemDescriptions`.
- `Item.toString`: formats the item as a item in a list automatically
- `HiddenRoom`: Subclass of `Room` that allows for secret rooms.
