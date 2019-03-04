package ffb.thedrake;

public interface BoardTile extends Tile {
	public static BoardTile EMPTY = new BoardTile() {

		@Override
		public boolean canStepOn() {
			return true;
		}

		@Override
		public boolean hasTroop() {
			return false;
		}
	};
	
	public static final BoardTile MOUNTAIN = new BoardTile() {
		@Override
		public boolean canStepOn() {
			return false;
		}
		
		@Override
		public boolean hasTroop() {
			return false;
		}
	};
}
