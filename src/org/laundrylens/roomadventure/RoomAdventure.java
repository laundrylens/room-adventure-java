import room.HiddenRoom;
import room.Room;

import java.util.Scanner;

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = { null, null, null, null, null }; // Player inventory slots
    private static String status; // Message to display after each action

    // constants
    final private static String DEFAULT_STATUS = "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', and 'take'."; // Default

    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                currentRoom = exitDestinations[i]; // Change current room
                status = "Changed Room"; // Update status

            }
        }
    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    public static void handleUse(String noun) {
        status = "You don't have that item in your inventory.";
        String[] items = currentRoom.getItems();
        byte i = -1; // index of item, if found
        boolean found = false;
        for (String item : inventory) { // checks if you have the item in your inventory
            if (item.equals(noun)) {
                i++;
                found = true;
                break;
            }
        }

        if (found) {
            switch (noun) {
                case "Key": // use Key
                    // reveal secret room 1
                    // HACK: workaroud. loop through the exits of the room until we find a
                    // hidden room, then show it.
                    for (Room exitDestination : currentRoom.getExitDestinations()) {
                        if (exitDestination.getClass() == HiddenRoom.class) {
                            ((HiddenRoom) exitDestination).setVisible(true);
                        }
                    }
                    break;
                case "Trophy": // use Trophy, requires a room with a table
                    boolean foundDesk = false;
                    for (String item : items) { // Check that theres a table in the room
                        if (item.equals("Table")) {
                            foundDesk = true;
                        }
                    }
                    if (foundDesk) {
                        inventory[i] = null;
                        status = "Something changed!";
                        // create secret room 2

                    }
                    break;
                case "Pokeball": // use Pokeball
                    boolean foundMirror = false;
                    for (String item : items) { //
                        if (item.equals("Mirror")) {
                            foundMirror = true;
                        }
                    }
                    if (foundMirror) { // mirror, secret ending
                        status = "You threw the pokeball at the mirror and caught yourself!";
                        inventory[i] = null;
                        // secret ending
                    } else { // no mirror, add pokeball to room grabbables
                        inventory[i] = null;
                        status = "You threw the pokeball at nothing!";
                    }

                    break;
                case "Knife":
                    status = "You committed Seppuku!";
                    // win
                    break;
                default:
                    status = "You can't use that item. here...";
                    break;
            }
        }
    }

    private static void setupGame() { // Initializes game world
        Room room1 = new Room("Room 1"); // Create Room 1
        Room room2 = new Room("Room 2"); // Create Room 2
        Room room3 = new Room("Room 3");
        Room room4 = new Room("Room 4");

        HiddenRoom secretRoom1 = new HiddenRoom("Hidden Room 1");
        HiddenRoom secretRoom2 = new HiddenRoom("Hidden Room 2");

        // Exits for secret room #1
        String[] secretRoom1ExitDirections = { "north" };
        Room[] secretRoom1ExitDestinations = { room3 };
        secretRoom1.setExitDirections(secretRoom1ExitDirections);
        secretRoom1.setExitDestinations(secretRoom1ExitDestinations);

        // Exits for secret room #2
        String[] secretRoom2ExitDirections = { "north" };
        Room[] secretRoom2ExitDestinations = { room2 };
        secretRoom2.setExitDirections(secretRoom2ExitDirections);
        secretRoom2.setExitDestinations(secretRoom2ExitDestinations);

        String[] room1ExitDirections = { "east", "south" }; // Room 1 exits
        Room[] room1ExitDestinations = { room2, room3 }; // Destination rooms for Room 1
        String[] room1Items = { "chair", "table" }; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
                "It is a chair",
                "It's a desk, there is a key on it."
        };
        String[] room1Grabbables = { "key" }; // Items you can take in Room 1

        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items

        String[] room2ExitDirections = { "west", "south" }; // Room 2 exits
        Room[] room2ExitDestinations = { room1, room4 }; // Destination rooms for Room 2
        String[] room2Items = { "lamp", "bed", "table" };
        String[] room2ItemDescriptions = {
                "Doesn't that look a little flat?",
                "Looks comfy. No time to rest.",
                "If only there was something I could place on top."
        };

        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions

        String[] room3ExitDirections = { "north", "east", "south" };
        Room[] room3ExitDestinations = { room1, room4, secretRoom1 };
        String[] room3Items = { "pizza-slice", "frog", "key" };
        String[] room3ItemDescriptions = {
                "Rotten",
                "Noisy",
                "This looks like it will come in handy"
        };
        String[] room3Grabbables = { "key" };

        room3.setExitDirections(room3ExitDirections); // Set exits
        room3.setExitDestinations(room3ExitDestinations); // Set exit destinations
        room3.setItems(room3Items); // Set visible items
        room3.setItemDescriptions(room3ItemDescriptions); // Set item descriptions
        room3.setGrabbables(room3Grabbables);

        String[] room4ExitDirections = { "north", "west" };
        Room[] room4ExitDestinations = { room2, room3 };
        String[] room4Items = { "hand", "pokeball" };
        String[] room4ItemDescriptions = {
                "Yes",
                "Is there something I can catch with this?"
        };
        String[] room4Grabbables = { "pokeball" };

        room4.setExitDirections(room4ExitDirections); // Set exits
        room4.setExitDestinations(room4ExitDestinations); // Set exit destinations
        room4.setItems(room4Items); // Set visible items
        room4.setItemDescriptions(room4ItemDescriptions); // Set item descriptions
        room4.setGrabbables(room4Grabbables);

        currentRoom = room1; // Start game in Room 1
    }

    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program

        setupGame(); // Initialize rooms, items, and starting room

        Scanner s = new Scanner(System.in);

        while (true) { // Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the action verb
            String noun = words[1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // If verb is 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                    break;
                case "use":
                    handleUse(noun);
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message
        }
    }

}
