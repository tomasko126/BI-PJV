package ffb.thedrake;

public class Board {
	private final int dimension;
	private PositionFactory positionFactory;
	private BoardTile[][] boardArray;

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(int dimension) {
		this.positionFactory = new PositionFactory(dimension);
		this.dimension = dimension;
		this.boardArray = new BoardTile[dimension][];

		for (int i = 0; i < dimension; ++i) {
			this.boardArray[i] = new BoardTile[dimension];

			for (int j = 0; j < dimension; ++j) {
				this.boardArray[i][j] = BoardTile.EMPTY;
			}
		}
	}

	// Rozměr hrací desky
	public int dimension() {
		return dimension;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(BoardPos pos){
		return boardArray[pos.i()][pos.j()];
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt ...ats) {
		Board newBoard = new Board(dimension);

		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < dimension; ++j) {
				newBoard.boardArray[i][j] = this.boardArray[i][j];
			}
		}

		for (TileAt tmp : ats) {
			newBoard.boardArray[tmp.pos.i()][tmp.pos.j()] = tmp.tile;
		}

		return newBoard;
	}

	// Vrací instanci PositionFactory pro výrobu pozic na tomto hracím plánu
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

