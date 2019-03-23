package ffb.thedrake;

public class StepAndCapture extends BoardMove {

	public StepAndCapture(BoardPos origin, BoardPos target) {
		super(origin, target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.stepAndCapture(origin(), target());
	}
	
}
