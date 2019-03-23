package ffb.thedrake;

public class StepOnly extends BoardMove {

	public StepOnly(BoardPos origin, BoardPos target) {
		super(origin, target);
	}

	@Override
	public GameState execute(GameState originState) {
		return originState.stepOnly(origin(), target());
	}
	
}
