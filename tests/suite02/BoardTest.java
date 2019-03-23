package suite02;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import ffb.thedrake.Board;
import ffb.thedrake.BoardTile;
import ffb.thedrake.PositionFactory;

public class BoardTest {

	@Test
	public void classStructure() {
		// All attributes private and final
		for(Field f : Board.class.getFields()) {
			assertTrue(Modifier.isPrivate(f.getModifiers()));
			assertTrue(Modifier.isFinal(f.getModifiers()));
		}
	}
	
	@Test
	public void behaviour() {
		Board emptyBoard = new Board(3);
		assertSame(3, emptyBoard.dimension());
		
		PositionFactory pf = emptyBoard.positionFactory();
		
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a3")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b3")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c3")));
		
		Board board = emptyBoard.withTiles(
			new Board.TileAt(pf.pos(0, 0), BoardTile.MOUNTAIN),
			new Board.TileAt(pf.pos(1, 2), BoardTile.MOUNTAIN),
			new Board.TileAt(pf.pos(2, 1), BoardTile.MOUNTAIN)
		);
		assertSame(3, board.dimension());
		
		pf = board.positionFactory();
		assertSame(BoardTile.MOUNTAIN, board.at(pf.pos("a1")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("b1")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("c1")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("a2")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("b2")));
		assertSame(BoardTile.MOUNTAIN, board.at(pf.pos("c2")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("a3")));
		assertSame(BoardTile.MOUNTAIN, board.at(pf.pos("b3")));
		assertSame(BoardTile.EMPTY, board.at(pf.pos("c3")));
		
		Board board2 = board.withTiles(
			new Board.TileAt(pf.pos(0, 1), BoardTile.MOUNTAIN),
			new Board.TileAt(pf.pos(0, 2), BoardTile.MOUNTAIN),
			new Board.TileAt(pf.pos(2, 1), BoardTile.EMPTY)
		);
			
		assertSame(3, board2.dimension());
		
		pf = board2.positionFactory();
		assertSame(BoardTile.MOUNTAIN, board2.at(pf.pos("a1")));
		assertSame(BoardTile.EMPTY, board2.at(pf.pos("b1")));
		assertSame(BoardTile.EMPTY, board2.at(pf.pos("c1")));
		assertSame(BoardTile.MOUNTAIN, board2.at(pf.pos("a2")));
		assertSame(BoardTile.EMPTY, board2.at(pf.pos("b2")));
		assertSame(BoardTile.EMPTY, board2.at(pf.pos("c2")));
		assertSame(BoardTile.MOUNTAIN, board2.at(pf.pos("a3")));
		assertSame(BoardTile.MOUNTAIN, board2.at(pf.pos("b3")));
		assertSame(BoardTile.EMPTY, board2.at(pf.pos("c3")));
			
		//Test that the emptyBoard stays empty end independent 
		//of the new board.
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c1")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c2")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("a3")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("b3")));
		assertSame(BoardTile.EMPTY, emptyBoard.at(pf.pos("c3")));
	}
}
