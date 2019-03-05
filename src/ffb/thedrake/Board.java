package ffb.thedrake;



public class Board {
	private final int dimension;
	private BoardTile[][] boardArray;
	private PositionFactory positionFactory;
	public Board(int dimension) {
		this.positionFactory = new PositionFactory(dimension);
		this.dimension = dimension;
		boardArray = new BoardTile[dimension][];
		for(int i = 0; i < dimension; ++i){
			boardArray[i] = new BoardTile[dimension];
			for(int j = 0; j < dimension; ++j){
				boardArray[i][j] = BoardTile.EMPTY;
			}
		}
	}
		
	public int dimension() {
		return dimension;
	} 
	
	public BoardTile at(BoardPos pos){
		return boardArray[pos.i()][pos.j()];
	}
		
	public Board withTiles(TileAt ...ats) {
		Board newBoard = new Board(dimension);
		for(int i = 0; i < dimension; ++i){
			for(int j = 0; j < dimension; ++j){
				newBoard.boardArray[i][j] = this.boardArray[i][j];
			}
		}
		for(TileAt tmp : ats){
			newBoard.boardArray[tmp.pos.i()][tmp.pos.j()] = tmp.tile;
		}
		return newBoard;
	}
	
	public PositionFactory positionFactory() {
		return positionFactory;
	}
	
	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

