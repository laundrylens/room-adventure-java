import item.Item;
import java.util.ArrayList;
import java.util.Scanner;
import room.HiddenRoom;
import room.Room;

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static ArrayList<Item> inventory = new ArrayList<>(); // Player inventory slots
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
        Item[] items = currentRoom.getItems(); // Visible items in current room
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i].getName())) { // If user-noun matches an item
                status = items[i].getDescription(); // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        Item[] items = currentRoom.getItems(); // Items that can be taken
        ArrayList<Item> grabbables = new ArrayList<>();
        for (int i = 0; i < items.length;i++) {
            if (items[i].isGrabbable() == true) grabbables.add(items[i]);
        }
        status = "I can't grab that."; // Default if not grabbable
        for (Item item : grabbables) { // Loop through grabbable items
            if (noun.equals(item.getName())) { // If user-noun matches grabbable
                inventory.add(item);
                currentRoom.removeItem(item);
                status = "Added it to the inventory"; // Update status
                break; // Exit inventory loop
            }
            
        }
    }

    public static void handleUse(String noun) {
        status = "You don't have that item in your inventory.";
        byte i = -1; // index of item, if found
        boolean found = false;
        for (byte j = 0; j < inventory.size(); j++) {
            if (inventory.get(j).getName().equals(noun)) {
                i = j;
                found = true;
                break;
            }
        }

        if (found) {
            switch (noun) {
                case "key": // use Key
                    // reveal secret room 1
                    for (Room exitDestination : currentRoom.getExitDestinations()) {
                        if (exitDestination.getName().equals("Hidden Room 1")) {
                            status = "Something changed!";
                            inventory.remove(i);
                            ((HiddenRoom) exitDestination).setVisible(true);
                        } else {
                            status = "Nothing happened...";
                        }
                    }
                    // remove the item from player's inventory after use
                    break;
                case "trophy": // use Trophy, requires a room with a table
                    boolean foundDesk = false;
                    for (Item item : currentRoom.getItems()) { // Check that theres a table in the room
                        if (item.getName().equals("table")) {
                            foundDesk = true;
                        }
                    }
                    if (foundDesk) {
                        // remove the item from player's inventory after use
                        inventory.remove(i);
                        status = "Something changed!";
                        // create secret room 2
                        // HACK: workaroud. loop through the exits of the room until we find a
                        // hidden room, then show it.
                        for (Room exitDestination : currentRoom.getExitDestinations()) {
                            if (exitDestination.getClass() == HiddenRoom.class) {
                                ((HiddenRoom) exitDestination).setVisible(true);
                            }
                        }
                    }
                    break;
                case "pokeball": // use Pokeball
                    boolean foundMirror = false;
                    for (Item item : currentRoom.getItems()) { //
                        if (item.getName().equals("mirror")) {
                            foundMirror = true;
                        }
                    }
                    if (foundMirror) { // mirror, secret ending
                        status = "You threw the pokeball at the mirror and caught yourself!";
                        inventory.remove(i);
                        // secret ending
                        handleGo("south");
                    } else { // no mirror, add pokeball to room grabbables
                        status = "You threw the pokeball at nothing!";
                        currentRoom.addItem(inventory.get(i));
                        inventory.remove(i);
                    }

                    break;
                case "knife":
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
        Room room3 = new Room("Room 3"); // Create Room 3
        Room room4 = new Room("Room 4"); // Create Room 4

        HiddenRoom secretRoom1 = new HiddenRoom("Hidden Room 1"); // Create Secret Room 1
        HiddenRoom secretRoom2 = new HiddenRoom("Hidden Room 2"); // Create Secret Room 2
        HiddenRoom pokeballRoom = new HiddenRoom("Pokeball");

        //Create Items for Rooms 1 - 4
        Item chair = new Item("chair", "It is a chair");
        Item table = new Item("table","If only there was something I could place on top");
        Item lamp = new Item("lamp", "Doesn't that look a little flat?");
        Item bed = new Item("bed", "Looks comfy. No time to rest.");
        Item pizza = new Item("pizza-slice", "Rotten.");
        Item frog = new Item("frog", "Noisy.");
        Item key = new Item("key", "This might come in handy", true);
        Item hand = new Item("hand", "Yes.");
        Item pokeball = new Item("pokeball", "Is there something I could catch with this?", true);
        Item knife = new Item("knife", "Looks sharp.", true);

        //Create Items for secret Rooms
        Item mirror = new Item("mirror", "A mirror. You look at yourself and wonder how you got here.");
        Item mistletoe = new Item("mistletoe","A mistletoe is hanging from the ceiling. You stand under it and hope for a kiss; you remain sad and lonely.");
        Item kendama = new Item("kendama","A traditional Japanese kendama toy, made from wood. You pick it up and try to perform a trick on it, but you fail. Better luck next time, perhaps.");
        Item book = new Item("book", "A book. You pick it up and open it, eager to gain knowledge; you then remember you don't know how to read.");

        
        // Items for pokeball room, no exits
        Item[] pokeballRoomItems = {knife};
        pokeballRoom.setItems(pokeballRoomItems);

        // Exits for secret room #1
        String[] secretRoom1ExitDirections = { "north", "south" };
        Room[] secretRoom1ExitDestinations = { room3 , pokeballRoom};
        secretRoom1.setExitDirections(secretRoom1ExitDirections);
        secretRoom1.setExitDestinations(secretRoom1ExitDestinations);

        // Items for secret room #1
        Item[] secretRoom1Items = { mirror, mistletoe };

        secretRoom1.setItems(secretRoom1Items);
    

        // Exits for secret room #2
        String[] secretRoom2ExitDirections = { "north" };
        Room[] secretRoom2ExitDestinations = { room2 };

        secretRoom2.setExitDirections(secretRoom2ExitDirections);
        secretRoom2.setExitDestinations(secretRoom2ExitDestinations);

        // Items for secret room #2
        Item[] secretRoom2Items = { kendama, book };

        secretRoom2.setItems(secretRoom2Items);

        String[] room1ExitDirections = { "east", "south" }; // Room 1 exits
        Room[] room1ExitDestinations = { room2, room3 }; // Destination rooms for Room 1
        Item[] room1Items = { chair, table }; // Items in Room 1

        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items

        String[] room2ExitDirections = { "west", "south" }; // Room 2 exits
        Room[] room2ExitDestinations = { room1, room4 }; // Destination rooms for Room 2
        Item[] room2Items = { lamp, bed, table };

        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items

        String[] room3ExitDirections = { "north", "east", "south" };
        Room[] room3ExitDestinations = { room1, room4, secretRoom1 };
        Item[] room3Items = { pizza, frog, key };

        room3.setExitDirections(room3ExitDirections); // Set exits
        room3.setExitDestinations(room3ExitDestinations); // Set exit destinations
        room3.setItems(room3Items); // Set visible items

        String[] room4ExitDirections = { "north", "west" };
        Room[] room4ExitDestinations = { room2, room3 };
        Item[] room4Items = { hand, pokeball };

        room4.setExitDirections(room4ExitDirections); // Set exits
        room4.setExitDestinations(room4ExitDestinations); // Set exit destinations
        room4.setItems(room4Items); // Set visible items

        currentRoom = room1; // Start game in Room 1
    }

    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program

        setupGame(); // Initialize rooms, items, and starting room

        try (Scanner s = new Scanner(System.in)) {
            while (true) { // Game loop, runs until program is terminated
                System.out.print(currentRoom.toString()); // Display current room description
                System.out.print("Inventory: "); // Prompt for inventory display

                for (int i = 0; i < inventory.size(); i++) { // Loop through inventory slots
                    System.out.print(inventory.get(i).getName() + " "); // Print each inventory item
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
}
