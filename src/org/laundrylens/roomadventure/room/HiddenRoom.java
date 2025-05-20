package room;

public class HiddenRoom extends Room {
    private boolean visible;

    public HiddenRoom(String name) {
        super(name);
        this.visible = false;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
