package room;
import item.Item;

public class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private Item[] items; // Items visible in the room
    // private String[] itemDescriptions; // Descriptions for those items
    // private String[] grabbables; // Items you can take

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(Item[] items) { // Setter for items
        this.items = items;
    }

    public Item[] getItems() { // Getter for items
        return items;
    }

    // public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
    //     this.itemDescriptions = itemDescriptions;
    // }

    // public String[] getItemDescriptions() { // Getter for descriptions
    //     return itemDescriptions;
    // }

    // public void setGrabbables(String[] grabbables) { // Setter for grabbable items
    //     this.grabbables = grabbables;
    // }

    // public String[] getGrabbables() { // Getter for grabbable items
    //     return grabbables;
    // }
    public void removeItem(Item item){
        Item[] newItems = new Item[items.length - 1];
        int newItemIndex = 0;
        for (int i = 0; i<items.length;i++){ // loop through items to find item to remove
            if (this.items[i] != item){
                newItems[newItemIndex++] = this.items[i]; // add to new array if not the item to remove
            }
        }
        this.items = newItems; // correct array
    }

    public void addItem(Item item){
        Item[] newItems = new Item[items.length + 1];
        int newItemIndex = 0;
        for (int i = 0; i<newItems.length;i++){ // loop through items to find item to remove
            newItems[newItemIndex++] = this.items[i]; // add to new array if not the item to remove
        }
        this.items = newItems; // correct array
    }

    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
        for (Item item : items) { // Loop items
            result += item.getName() + " "; // Append each item
        }
        result += "\nExits: "; // List exits
        // for (String direction : exitDirections) { // Loop exits
        // result += direction + " "; // Append each direction
        // }
        for (int i = 0; i < exitDestinations.length; i++) {
            if (exitDestinations[i].getClass() == HiddenRoom.class) {
                HiddenRoom hr = (HiddenRoom) exitDestinations[i];
                if (hr.isVisible())
                    result += exitDirections[i] + "* ";
                else
                    continue;
            } else {
                result += exitDirections[i] + " ";
            }
        }
        return result + "\n"; // Return full description
    }

    public String getName() {
        return name;
    }
}
