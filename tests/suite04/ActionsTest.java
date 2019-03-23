package suite04;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ffb.thedrake.Army;
import ffb.thedrake.Board;
import ffb.thedrake.BoardTroops;
import ffb.thedrake.CaptureOnly;
import ffb.thedrake.GameState;
import ffb.thedrake.Move;
import ffb.thedrake.PlayingSide;
import ffb.thedrake.PositionFactory;
import ffb.thedrake.StandardDrakeSetup;
import ffb.thedrake.StepAndCapture;
import ffb.thedrake.StepOnly;

public class ActionsTest {

	private Set<Move> makeSet(Move ...moves) {
		Set<Move> result = new HashSet<Move>();
		for(Move m : moves) {
			result.add(m);
		}
		
		return result;
	}
	
	@Test
	public void test()  {
		Board board = new Board(4);
		PositionFactory pf = board.positionFactory();
		StandardDrakeSetup setup = new StandardDrakeSetup();
		
		BoardTroops blueTroops = new BoardTroops(PlayingSide.BLUE);
		blueTroops = blueTroops
				.placeTroop(setup.DRAKE, pf.pos("a1"))
				.placeTroop(setup.CLUBMAN, pf.pos("a2"))
				.placeTroop(setup.SPEARMAN, pf.pos("b2"));
		Army blueArmy = new Army(blueTroops, Collections.emptyList(), Collections.emptyList());
		
		BoardTroops orangeTroops = new BoardTroops(PlayingSide.ORANGE);
		orangeTroops = orangeTroops
				.placeTroop(setup.DRAKE, pf.pos("c4"))
				.placeTroop(setup.MONK, pf.pos("c3")) 
				.placeTroop(setup.CLUBMAN, pf.pos("b3"));
		Army orangeArmy = new Army(orangeTroops, Collections.emptyList(), Collections.emptyList());
		
		GameState state = new GameState(board, blueArmy, orangeArmy);
		
		assertEquals(
			makeSet(
				new StepOnly(pf.pos("a1"), pf.pos("b1")),
				new StepOnly(pf.pos("a1"), pf.pos("c1")),
				new StepOnly(pf.pos("a1"), pf.pos("d1"))
			),
			new HashSet<Move>(
				state.tileAt(pf.pos("a1")).movesFrom(pf.pos("a1"), state)
			)
		);	
		
		assertEquals(
				makeSet(
					new StepOnly(pf.pos("a2"), pf.pos("a3"))
				),
				new HashSet<Move>(
					state.tileAt(pf.pos("a2")).movesFrom(pf.pos("a2"), state)
				)
			);
		
		assertEquals(
				makeSet(
					new StepAndCapture(pf.pos("b2"), pf.pos("b3")),
					new CaptureOnly(pf.pos("b2"), pf.pos("c4"))
				),
				new HashSet<Move>(
					state.tileAt(pf.pos("b2")).movesFrom(pf.pos("b2"), state)
				)
			);
	
	}
}


