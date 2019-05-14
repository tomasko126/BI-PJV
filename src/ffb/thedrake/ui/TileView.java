package ffb.thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ffb.thedrake.BoardPos;
import ffb.thedrake.Move;
import ffb.thedrake.Tile;

public class TileView extends Pane {

    private BoardPos boardPos;

    private Tile tile;

    private TileBackgrounds backgrounds = new TileBackgrounds();

    private Border selectBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private Move move;

    private final ImageView moveImage;

    private TileViewContext tileViewContext;

    public TileView(BoardPos boardPos, Tile tile, TileViewContext tileViewContext) {
        this.boardPos = boardPos;
        this.tile = tile;

        setPrefSize(100, 100);

        setOnMouseClicked(e -> onClick());
        update();

        moveImage = new ImageView(getClass().getResource("assets/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);

        this.tileViewContext = tileViewContext;
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    public void select() {
        setBorder(selectBorder);
        tileViewContext.tileViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }

    public void onClick() {
        if (move != null) {
            tileViewContext.executeMove(move);
        } else if (tile.hasTroop()) {
            select();
        }
    }

    public BoardPos position() {
        return boardPos;
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);

    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

}
