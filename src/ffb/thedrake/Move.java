package ffb.thedrake;

public abstract class Move {
	private final BoardPos target;
	
	protected Move(BoardPos target) {
		this.target = target;
	}
	
	public BoardPos target() {
		return target;
	}
	
	public abstract GameState execute(GameState originState);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
}
