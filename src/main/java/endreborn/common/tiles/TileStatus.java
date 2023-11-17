package endreborn.common.tiles;

public enum TileStatus {

    Idle("tile.status.gui.idle"),
    OutFull("tile.status.gui.output_full"),
    Failed("tile.status.gui.failed"),
    Invalid("tile.status.gui.invalid_recipe"),

    Running("tile.status.gui.running");

    private final String langKey;

    TileStatus(String langKey) {
        this.langKey = langKey;
    }

    public String getLangKey() {
        return langKey;
    }
}
