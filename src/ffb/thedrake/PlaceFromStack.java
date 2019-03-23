package ffb.thedrake;

public class PlaceFromStack extends Move {

	public PlaceFromStack(BoardPos target) {
		super(target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.placeFromStack(target());
	}
	
}
