package suite02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import ffb.thedrake.Offset2D;
import ffb.thedrake.Troop;
import ffb.thedrake.TroopFace;

public class TroopTest {

	@Test
	public void classStructure() {
		// All attributes private and final
		for(Field f : Troop.class.getFields()) {
			assertTrue(Modifier.isPrivate(f.getModifiers()));
			assertTrue(Modifier.isFinal(f.getModifiers()));
		}
	}
	
	@Test
	public void behaviour() {
		Troop archer = new Troop("Archer", new Offset2D(1, 1), new Offset2D(0, 1));
		assertEquals("Archer", archer.name());
		assertTrue(archer.pivot(TroopFace.AVERS).equalsTo(1, 1));
		assertTrue(archer.pivot(TroopFace.REVERS).equalsTo(0, 1));
		
		Troop monk = new Troop("Monk", new Offset2D(1, 1));
		assertEquals("Monk", monk.name());
		assertTrue(monk.pivot(TroopFace.AVERS).equalsTo(1, 1));
		assertTrue(monk.pivot(TroopFace.REVERS).equalsTo(1, 1));
		
		Troop drake = new Troop("Drake");
		assertEquals("Drake", drake.name());
		assertTrue(drake.pivot(TroopFace.AVERS).equalsTo(1, 1));
		assertTrue(drake.pivot(TroopFace.REVERS).equalsTo(1, 1));
	}
}
