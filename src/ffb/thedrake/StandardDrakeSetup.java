package ffb.thedrake;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDrakeSetup {
	
	private final Map<String, Troop> infoMap;
	
	public StandardDrakeSetup() {
		infoMap = new HashMap<>();
		infoMap.put(DRAKE.name(), DRAKE);
		infoMap.put(CLUBMAN.name(), CLUBMAN);
		infoMap.put(MONK.name(), MONK);
		infoMap.put(SPEARMAN.name(), SPEARMAN);
		infoMap.put(SWORDSMAN.name(), SWORDSMAN);
		infoMap.put(ARCHER.name(), ARCHER);
	}
	
	public GameState startState(Board board) {
		List<Troop> stack = Arrays.asList(
				DRAKE, CLUBMAN, CLUBMAN, MONK, SPEARMAN,
				SWORDSMAN,ARCHER
			); 
		
		Army blueArmy = new Army(PlayingSide.BLUE, stack);
		Army orangeArmy = new Army(PlayingSide.ORANGE, stack);
		
		return new GameState(board, blueArmy, orangeArmy);
	}
	
	public List<Troop> troops() {
		return Arrays.asList(DRAKE, CLUBMAN, MONK, SPEARMAN, SWORDSMAN, ARCHER);
	}	
	
	public Troop infoByName(String name) {
		if(infoMap.containsKey(name))
			return infoMap.get(name);
		
		throw new IllegalArgumentException();
	}
	
	public final Troop DRAKE = new Troop(
			"Drake",
			Arrays.asList(
					new SlideAction(1, 0),    
					new SlideAction(-1, 0)), 		      
			Arrays.asList(
					new SlideAction(0, 1),
  				new SlideAction(0, -1)));
	
	public final Troop CLUBMAN = new Troop(
			"Clubman",
			Arrays.asList(
		      new ShiftAction(1, 0),    
		      new ShiftAction(0, 1),
		      new ShiftAction(-1, 0),
		      new ShiftAction(0, -1)), 		      
			Arrays.asList(
		  		new ShiftAction(1, 1),    
		      new ShiftAction(-1, 1),
		      new ShiftAction(1, -1),
		      new ShiftAction(-1, -1))); 
	
	public final Troop MONK = new Troop(
			"Monk",
			Arrays.asList(
					new SlideAction(1, 1),    
		      new SlideAction(-1, 1),
		      new SlideAction(1, -1),
		      new SlideAction(-1, -1)), 		      
			Arrays.asList(
					new ShiftAction(1, 0),    
		      new ShiftAction(0, 1),
		      new ShiftAction(-1, 0),
		      new ShiftAction(0, -1)));
	
	public final Troop SPEARMAN = new Troop(
			"Spearman",
			Arrays.asList(
					new ShiftAction(0, 1),
					new StrikeAction(1, 2),    
		      new StrikeAction(-1, 2)), 		      
			Arrays.asList(
					new ShiftAction(1, 1),    
		      new ShiftAction(-1, 1),
		      new ShiftAction(0, -1)));
	
	public final Troop SWORDSMAN = new Troop(
			"Swordsman",
			Arrays.asList(
					new StrikeAction(1, 0),    
		      new StrikeAction(0, 1),
		      new StrikeAction(-1, 0),
		      new StrikeAction(0, -1)), 		      
			Arrays.asList(
					new ShiftAction(1, 0),    
		      new ShiftAction(0, 1),
		      new ShiftAction(-1, 0),
		      new ShiftAction(0, -1)));
	
	public final Troop ARCHER = new Troop(
			"Archer",
			new Offset2D(1, 1), 
			new Offset2D(1, 0),
			Arrays.asList(
					new ShiftAction(1, 0),    
		      new ShiftAction(-1, 0),
		      new ShiftAction(0, -1)), 		      
			Arrays.asList(    
		      new ShiftAction(0, 1),
		      new StrikeAction(-1, 1),
		      new StrikeAction(1, 1),
		      new StrikeAction(0, 2)));
}
