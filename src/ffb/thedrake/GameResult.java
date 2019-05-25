package ffb.thedrake;

public enum GameResult {
	VICTORY, DRAW, IN_PLAY, MAIN_MENU;

	public static boolean stateChanged = false;
	public static GameResult state = null;

	public static void changeStateTo(GameResult newState) {
		if (state == newState) {
			return;
		}
		state = newState;
		changeStateChangedTo(true);
	}

	public static void changeStateChangedTo(boolean newStatus) {
		stateChanged = newStatus;
	}

	public static GameResult getState() {
		return state;
	}

	public static boolean getStateChanged() {
		return stateChanged;
	}
}
