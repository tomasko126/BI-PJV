package ffb.thedrake.ui;

import ffb.thedrake.Tile;
import ffb.thedrake.TroopFace;
import ffb.thedrake.TroopTile;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StackView extends Pane {

    private Tile tile;

    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final Border selectBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private TileViewContext tileViewContext;

    public StackView(TileViewContext context) {
        this.setPrefSize(100, 100);

        setOnMouseClicked(e -> onClick());

        this.tileViewContext = context;

        update();
    }

    public void update() {
        if (!tileViewContext.getGameState().armyOnTurn().stack().isEmpty()) {
            setTile(new TroopTile(tileViewContext.getGameState().armyOnTurn().stack().get(0), tileViewContext.getGameState().sideOnTurn(), TroopFace.AVERS));
        } else {
            setTile(null);
        }

        this.setBackground(backgrounds.get(tile));
    }

    public void select() {
        this.setBorder(selectBorder);
        tileViewContext.stackViewSelected(this);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void unselect() {
        this.setBorder(null);
    }

    public void onClick() {
        select();
    }
}