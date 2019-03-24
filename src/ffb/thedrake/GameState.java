package ffb.thedrake;

import java.util.Optional;

public class GameState{
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	private boolean isInited;
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy, 
			PlayingSide sideOnTurn, 
			GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
		this.isInited = false;
	}
	
	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		
		return orangeArmy;
	}
	
	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}
	
	public Tile tileAt(BoardPos pos) {
		// Místo pro váš kód

        if(!blueArmy.boardTroops().at(pos).equals(Optional.empty())){
        	return blueArmy.boardTroops().at(pos).get();
		}else if(!orangeArmy.boardTroops().at(pos).equals(Optional.empty())){
			return orangeArmy.boardTroops().at(pos).get();
		}else if(!board.at(pos).hasTroop())
			return board.at(pos);
        return null;
	}

	private boolean canStepFrom(TilePos origin) {
        if(     !origin.equals(TilePos.OFF_BOARD)
        		&& result.equals(GameResult.IN_PLAY)
                && armyOnTurn().boardTroops().at(board.positionFactory().pos(origin.i(),origin.j())).isPresent()
                && !blueArmy.boardTroops().isPlacingGuards()
                && !orangeArmy.boardTroops().isPlacingGuards()){
            return true;
        }
        return false;
	}

	private boolean canStepTo(TilePos target) {
        return !target.equals(TilePos.OFF_BOARD)&&result.equals(GameResult.IN_PLAY)&&!armyNotOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).isPresent()&&armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).equals(Optional.empty())&&board.at(board.positionFactory().pos(target.i(),target.j())).canStepOn();
	}
	
	private boolean canCaptureOn(TilePos target) {
        return !target.equals(TilePos.OFF_BOARD)&&result.equals(GameResult.IN_PLAY)&&armyNotOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).isPresent();
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	/*
	Rozlisuje 3 pripady:
		A) pokladame leadra
		B) pokladame straze
		C) hra
	 */
	public boolean canPlaceFromStack(TilePos target) {
        if(!target.equals(TilePos.OFF_BOARD)&&result.equals(GameResult.IN_PLAY)&&!armyOnTurn().stack().isEmpty()){
        	// A
            if(!this.armyOnTurn().boardTroops().isLeaderPlaced()){
                if(sideOnTurn.equals(PlayingSide.BLUE)){
                    return target.row()==1;
                }
                else if(target.column()!=armyNotOnTurn().boardTroops().leaderPosition().column()){
                    return target.row()==this.board.dimension();
                }
            }
            // B
            else if(armyOnTurn().boardTroops().isPlacingGuards()&&armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).equals(Optional.empty())){
            	for(BoardPos it : board.positionFactory().pos(target.i(),target.j()).neighbours()){
            		if(armyOnTurn().boardTroops().leaderPosition().equalsTo(it.i(),it.j()))
            			return true;
				}
			}
            // C
            else if(this.armyNotOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).equals(Optional.empty())
					&& this.armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(),target.j())).equals(Optional.empty())){
            	for(BoardPos it : board.positionFactory().pos(target.i(),target.j()).neighbours()){
            		if(this.canStepTo(target)&&armyOnTurn().boardTroops().troopPositions().contains(it))
            			return true;
				}
            }
        }
        return false;
	}
	
	public GameState stepOnly(BoardPos origin, BoardPos target) {		
		if(canStep(origin, target))		 
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(), 
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}
}
