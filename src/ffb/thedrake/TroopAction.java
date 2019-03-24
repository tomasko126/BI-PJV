package ffb.thedrake;

import java.util.List;

public abstract class TroopAction {
	private final Offset2D offset;
	
	protected TroopAction(int offsetX, int offsetY) {
    this(new Offset2D(offsetX, offsetY));
  }
	
	public TroopAction(Offset2D offset) {
    this.offset = offset;
  }
	
	public Offset2D offset() {
		return offset;
	}
	
  public abstract List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state);
}
