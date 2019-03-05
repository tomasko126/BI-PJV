package suite02;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import ffb.thedrake.Offset2D;
import ffb.thedrake.PlayingSide;
import ffb.thedrake.Troop;
import ffb.thedrake.TroopFace;
import ffb.thedrake.TroopTile;

public class TroopTileTest {

	@Test
	public void classStructure() {
		// All attributes private and final
		for(Field f : TroopTile.class.getFields()) {
			assertTrue(Modifier.isPrivate(f.getModifiers()));
			assertTrue(Modifier.isFinal(f.getModifiers()));
		}
	}
	
	@Test
	public void behaviour() {
		Troop monk = new Troop("Monk", new Offset2D(1, 1));
		Troop drake = new Troop("Drake");
		
		TroopTile tile1 = new TroopTile(monk, PlayingSide.BLUE, TroopFace.AVERS);
		TroopTile tile2 = new TroopTile(drake, PlayingSide.ORANGE, TroopFace.REVERS);
		
		assertFalse(tile1.canStepOn());
		assertFalse(tile2.canStepOn());
		
		assertTrue(tile1.hasTroop());
		assertTrue(tile2.hasTroop());
			
		assertSame(monk, tile1.troop());
		assertSame(drake, tile2.troop());
		
		assertSame(PlayingSide.BLUE, tile1.side());
		assertSame(PlayingSide.ORANGE, tile2.side());
		
		assertSame(TroopFace.AVERS, tile1.face());
		assertSame(TroopFace.REVERS, tile2.face());
		
		assertSame(TroopFace.REVERS, tile1.flipped().face());
		assertSame(TroopFace.AVERS, tile2.flipped().face());
		
		assertFalse(tile1.flipped() == tile1);
	}
}
