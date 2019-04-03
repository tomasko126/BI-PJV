package ffb.thedrake;

import java.util.*;

public class BoardTroops {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) {
		this.playingSide = playingSide;
		this.troopMap = Collections.emptyMap();
		this.leaderPosition = TilePos.OFF_BOARD;
		this.guards = 0;
	}
	
	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		if (this.troopMap.containsKey(pos)) {
			Optional<TroopTile> troopTile = Optional.ofNullable(this.troopMap.get(pos));
			return troopTile;
		}

		return Optional.empty();
	}
	
	public PlayingSide playingSide() {
		return this.playingSide;
	}
	
	public TilePos leaderPosition() {
		return this.leaderPosition;
	}

	public Map<BoardPos, TroopTile> troopMap() {
		return this.troopMap;
	}

	public int guards() {
		return this.guards;
	}
	
	public boolean isLeaderPlaced() {
		if (this.leaderPosition() == TilePos.OFF_BOARD) {
			return false;
		}

		return true;
	}
	
	public boolean isPlacingGuards() {
		if (this.isLeaderPlaced() && this.guards() < 2) {
			return true;
		}

		return false;
	}	
	
	public Set<BoardPos> troopPositions() {
		Set<BoardPos> boardPosSet = new HashSet<BoardPos>();

		this.troopMap.forEach((key, value) -> {
			if (value.hasTroop()) {
				boardPosSet.add(key);
			}
		});

		return boardPosSet;
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if (this.troopMap.containsKey(target)) {
			throw new IllegalStateException("Cannot place troop on a tile, because there is already one!");
		}

		// Create new Troop tile with a given troop
		TroopTile newTroopTile = new TroopTile(troop, this.playingSide(), TroopFace.AVERS);

		// Create new Troop Map
		Map<BoardPos, TroopTile> updatedTroopMap = new HashMap<>(troopMap);

		// Update the Troop Map with the newly created Troop tile
		updatedTroopMap.put(target, newTroopTile);

		if (!this.isLeaderPlaced()) {
			return new BoardTroops(this.playingSide(), updatedTroopMap, target, this.guards());
		} else if (this.isPlacingGuards()){
			return new BoardTroops(this.playingSide(), updatedTroopMap, this.leaderPosition(), this.guards() + 1);
		} else {
			return new BoardTroops(this.playingSide(), updatedTroopMap, this.leaderPosition(), this.guards());
		}
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if (!this.isLeaderPlaced()) {
			throw new IllegalStateException("Cannot move troops before the leader is placed.");
		}

		if (this.isPlacingGuards()) {
			throw new IllegalStateException("Cannot move troops before guards are placed.");
		}

		if (!at(origin).isPresent()) {
			throw new IllegalArgumentException();
		}

		// Create new Troop Map
		Map<BoardPos, TroopTile> updatedTroopMap = new HashMap<>(troopMap);

		if (this.at(target).isPresent()) {
			throw new IllegalArgumentException("Cannot move troop to the target position, because it is not empty!");
		}

		// Create new BoardTroops
		BoardTroops updatedBoardTroops;

		// We are moving with the leader
		if (this.leaderPosition().equals(origin)) {
			updatedBoardTroops = new BoardTroops(this.playingSide(), updatedTroopMap, TilePos.OFF_BOARD, this.guards());
		} else {
			updatedBoardTroops = new BoardTroops(this.playingSide(), updatedTroopMap, this.leaderPosition(), this.guards());
		}

		updatedBoardTroops = updatedBoardTroops.placeTroop(updatedBoardTroops.troopMap().get(origin).troop(), target);

		// If origin's and target's faces are equal, flip the target's face
		if (updatedBoardTroops.troopMap().get(origin).face() == updatedBoardTroops.troopMap().get(target).face()) {
			updatedBoardTroops = updatedBoardTroops.troopFlip(target);
		}

		updatedBoardTroops = updatedBoardTroops.removeTroop(origin);

		return updatedBoardTroops;
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if (!isLeaderPlaced()) {
			throw new IllegalStateException("Cannot move troops before the leader is placed.");
		}
		
		if (isPlacingGuards()) {
			throw new IllegalStateException("Cannot move troops before guards are placed.");
		}
		
		if (!at(origin).isPresent()) {
			throw new IllegalArgumentException();
		}

		// Create new Troop Map
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if (!this.isLeaderPlaced() || this.isPlacingGuards()) {
			throw new IllegalStateException("Cannot move troops before guards are placed.");
		}

		if (!at(target).isPresent()) {
			throw new IllegalArgumentException("Target argument is not present!");
		}

		// Create new Troop Map
		Map<BoardPos, TroopTile> updatedTroopMap = new HashMap<>(troopMap);

		// Get the leader position
		TilePos newLeaderPosition;

		// We are removing leader
		if (this.leaderPosition().equals(target)) {
			newLeaderPosition = TilePos.OFF_BOARD;
		} else {
			newLeaderPosition = this.leaderPosition();
		}

		// Remove troop from map
		updatedTroopMap.remove(target);

		return new BoardTroops(this.playingSide(), updatedTroopMap, newLeaderPosition, this.guards());
	}	
}
