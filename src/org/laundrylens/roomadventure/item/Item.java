package item;

public class Item {
    private String name;
    private String description;
    private boolean grabbable;

    public Item(String name, String description, boolean grabbable) {
        this.name = name;
        this.description = description;
        this.grabbable = grabbable;
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.grabbable = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGrabbable() {
        return grabbable;
    }

    @Override
    public String toString() {
        return String.format("- %s:\n\t%s\n", name, description);
    }

}
