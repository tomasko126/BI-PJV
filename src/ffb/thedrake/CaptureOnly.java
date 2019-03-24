package ffb.thedrake;

public class CaptureOnly extends BoardMove {

	public CaptureOnly(BoardPos origin, BoardPos target) {
		super(origin, target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.captureOnly(origin(), target());
	}
	
}
