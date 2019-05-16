package ffb.thedrake.ui;

import ffb.thedrake.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;

public class TileBackgrounds {

    public static final Background EMPTY_BG = new Background(
            new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null));
    private final Background mountainBg;

    public TileBackgrounds() {
        Image img = new Image(getClass().getResourceAsStream("assets/mountain.png"));
        this.mountainBg = new Background(
                new BackgroundImage(img, null, null, null, null));
    }

    public Background get(Tile tile) {
        if (tile == null) {
            return EMPTY_BG;
        }

        if (tile.hasTroop()) {
            TroopTile armyTile = ((TroopTile) tile);
            return getTroop(armyTile.troop(), armyTile.side(), armyTile.face());
        }

        if (tile == BoardTile.MOUNTAIN) {
            return mountainBg;
        }

        return EMPTY_BG;
    }

    public Background getTroop(Troop info, PlayingSide side, TroopFace face) {
        TroopImageSet images = new TroopImageSet(info.name());
        BackgroundImage bgImage = new BackgroundImage(
                images.get(side, face), null, null, null, null);

        return new Background(bgImage);
    }
}
