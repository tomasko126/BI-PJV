package suite04;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;

import org.junit.Test;

import ffb.thedrake.Army;
import ffb.thedrake.Board;
import ffb.thedrake.BoardTile;
import ffb.thedrake.GameState;
import ffb.thedrake.PlayingSide;
import ffb.thedrake.PositionFactory;
import ffb.thedrake.StandardDrakeSetup;
import ffb.thedrake.TilePos;

public class GameStateTest {

	private GameState createTestState()  {
		Board board = new Board(3);
		PositionFactory pf = board.positionFactory();
		board = board.withTiles(new Board.TileAt(pf.pos("a3"), BoardTile.MOUNTAIN));
		return new StandardDrakeSetup().startState(board);
	}
	
	@Test
	public void classStructure() {
		// All attributes private and final
		for(Field f : GameState.class.getFields()) {
			assertTrue(Modifier.isPrivate(f.getModifiers()));
			assertTrue(Modifier.isFinal(f.getModifiers()));
		}
	}
	
	@Test
	public void introGame() {
		GameState state = createTestState();
		PositionFactory pf = state.board().positionFactory();
		
		assertFalse(state.canPlaceFromStack(TilePos.OFF_BOARD));
		
		// Placing the blue leader
		assertTrue(state.canPlaceFromStack(pf.pos("a1")));
		assertTrue(state.canPlaceFromStack(pf.pos("b1")));
		assertTrue(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
		
		state = state.placeFromStack(pf.pos("a1"));
		
		// Placing the orange leader
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("b1")));
		assertFalse(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertTrue(state.canPlaceFromStack(pf.pos("b3")));
		assertTrue(state.canPlaceFromStack(pf.pos("c3")));
		
		state = state.placeFromStack(pf.pos("c3"));
		
		// Placing first blue guard
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertTrue(state.canPlaceFromStack(pf.pos("b1")));
		assertFalse(state.canPlaceFromStack(pf.pos("c1")));
		assertTrue(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
		
		// No steps or capturing before guards are placed
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("a2")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("c3")));
		
		state = state.placeFromStack(pf.pos("a2"));
		
		// Placing first orange guard
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("b1")));
		assertFalse(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertTrue(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertTrue(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
		
		// No steps or capturing before guards are placed
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("c2")));
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("a1")));
		
		state = state.placeFromStack(pf.pos("b3"));
		
		// Placing second blue guard
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertTrue(state.canPlaceFromStack(pf.pos("b1")));
		assertFalse(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
			
		state = state.placeFromStack(pf.pos("b1"));
		
		// Placing second orange guard
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("b1")));
		assertFalse(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertTrue(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
			
		state = state.placeFromStack(pf.pos("c2"));
	}
	
	@Test
	public void middleGameBlue() {
		GameState state = createTestState();
		PositionFactory pf = state.board().positionFactory();

		state = state
				.placeFromStack(pf.pos("a1"))		
				.placeFromStack(pf.pos("c3"))
				.placeFromStack(pf.pos("a2"))
				.placeFromStack(pf.pos("b3"))
				.placeFromStack(pf.pos("b1"))
				.placeFromStack(pf.pos("c2"));
		
		// Placing blue troop
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("b1")));
		assertTrue(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertTrue(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
		
		// Stepping with blue troop
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("a3")));
		assertTrue(state.canStep(pf.pos("a1"), pf.pos("b2")));
		assertTrue(state.canStep(pf.pos("a1"), pf.pos("c1")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("a1")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("a2")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("b1")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("b3")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("c2")));
		assertFalse(state.canStep(pf.pos("a1"), pf.pos("c3")));
		
		// Capturing with blue troop
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a3")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("b2")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("c1")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a1")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a2")));
		assertFalse(state.canCapture(pf.pos("a1"), pf.pos("b1")));
		assertTrue(state.canCapture(pf.pos("a1"), pf.pos("b3")));
		assertTrue(state.canCapture(pf.pos("a1"), pf.pos("c2")));
		assertTrue(state.canCapture(pf.pos("a1"), pf.pos("c3")));
		
		// Boundaries
		assertFalse(state.canStep(TilePos.OFF_BOARD, pf.pos("b2")));
		assertFalse(state.canStep(pf.pos("a1"), TilePos.OFF_BOARD));
		
		assertFalse(state.canCapture(TilePos.OFF_BOARD, pf.pos("c3")));
		assertFalse(state.canCapture(pf.pos("a1"), TilePos.OFF_BOARD));
	}
	
	@Test
	public void middleGameOrange() {
		GameState state = createTestState();
		PositionFactory pf = state.board().positionFactory();

		state = state
				.placeFromStack(pf.pos("a1"))		
				.placeFromStack(pf.pos("c3"))
				.placeFromStack(pf.pos("a2"))
				.placeFromStack(pf.pos("b3"))
				.placeFromStack(pf.pos("b1"))
				.placeFromStack(pf.pos("c2"))
				.placeFromStack(pf.pos("b2"));
		
		// Placing orange troop
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("b1")));
		assertTrue(state.canPlaceFromStack(pf.pos("c1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a2")));
		assertFalse(state.canPlaceFromStack(pf.pos("b2")));
		assertFalse(state.canPlaceFromStack(pf.pos("c2")));
		assertFalse(state.canPlaceFromStack(pf.pos("a3")));
		assertFalse(state.canPlaceFromStack(pf.pos("b3")));
		assertFalse(state.canPlaceFromStack(pf.pos("c3")));
		
		// Stepping with orange troop
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("a3")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("b2")));
		assertTrue(state.canStep(pf.pos("c3"), pf.pos("c1")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("a1")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("a2")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("b1")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("b3")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("c2")));
		assertFalse(state.canStep(pf.pos("c3"), pf.pos("c3")));
		
		// Capturing with orange troop
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("a3")));
		assertTrue(state.canCapture(pf.pos("c3"), pf.pos("b2")));
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c1")));
		assertTrue(state.canCapture(pf.pos("c3"), pf.pos("a1")));
		assertTrue(state.canCapture(pf.pos("c3"), pf.pos("a2")));
		assertTrue(state.canCapture(pf.pos("c3"), pf.pos("b1")));
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("b3")));
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c2")));
		assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c3")));
	}
	
	@Test
	public void emptyStack() {
		Board board = new Board(3);
		PositionFactory pf = board.positionFactory();

		GameState state = new GameState(
			board,
			new Army(PlayingSide.BLUE, Collections.emptyList()),
			new Army(PlayingSide.ORANGE, Collections.emptyList())
		);
		
		//No placing from an empty stack
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
		assertFalse(state.canPlaceFromStack(pf.pos("a1")));
	}
}



