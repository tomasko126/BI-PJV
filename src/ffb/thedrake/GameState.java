package ffb.thedrake;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class GameState implements JSONSerializable {
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
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(BoardPos pos) {

        if (!blueArmy.boardTroops().at(pos).equals(Optional.empty())) {
            return blueArmy.boardTroops().at(pos).get();
        } else if (!orangeArmy.boardTroops().at(pos).equals(Optional.empty())) {
            return orangeArmy.boardTroops().at(pos).get();
        } else if (!board.at(pos).hasTroop())
            return board.at(pos);
        return null;
    }

    private boolean canStepFrom(TilePos origin) {
        if (!origin.equals(TilePos.OFF_BOARD)
                && result.equals(GameResult.IN_PLAY)
                && armyOnTurn().boardTroops().at(board.positionFactory().pos(origin.i(), origin.j())).isPresent()
                && !blueArmy.boardTroops().isPlacingGuards()
                && !orangeArmy.boardTroops().isPlacingGuards()) {
            return true;
        }
        return false;
    }

    private boolean canStepTo(TilePos target) {
        return !target.equals(TilePos.OFF_BOARD) && result.equals(GameResult.IN_PLAY) && !armyNotOnTurn().boardTroops().at(board.positionFactory().pos(target.i(), target.j())).isPresent() && armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(), target.j())).equals(Optional.empty()) && board.at(board.positionFactory().pos(target.i(), target.j())).canStepOn();
    }

    private boolean canCaptureOn(TilePos target) {
        if (target.equals(TilePos.OFF_BOARD) || !result.equals(GameResult.IN_PLAY) || !armyNotOnTurn().boardTroops().at(target).isPresent() || armyOnTurn().boardTroops().at(target).isPresent()) {
            return false;
        }

        return true;
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    /*
    Rozlisuje 3 pripady:
        A) pokladame leadra
        B) pokladame straze
        C) hra
     */
    public boolean canPlaceFromStack(TilePos target) {
        if (target.equals(TilePos.OFF_BOARD) || !result.equals(GameResult.IN_PLAY) || armyOnTurn().stack().isEmpty()) {
            return false;
        }

        // A
        if (!this.armyOnTurn().boardTroops().isLeaderPlaced()) {

            if (sideOnTurn.equals(PlayingSide.BLUE)) {
                return target.row() == 1;
            }

            if (target.column() == armyNotOnTurn().boardTroops().leaderPosition().column()) {
                if (target.row() - 2 > armyNotOnTurn().boardTroops().leaderPosition().row()) {
                    return true;
                }
                return false;
            } else {
                return target.row() == this.board.dimension();
            }
        }
        // B
        else if (armyOnTurn().boardTroops().isPlacingGuards() && armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(), target.j())).equals(Optional.empty())) {
            for (BoardPos it : board.positionFactory().pos(target.i(), target.j()).neighbours()) {
                if (!armyOnTurn().boardTroops().leaderPosition().equalsTo(it.i(), it.j()))
                    return true;
            }
        }
        // C
        else if (this.armyNotOnTurn().boardTroops().at(board.positionFactory().pos(target.i(), target.j())).equals(Optional.empty())
                && this.armyOnTurn().boardTroops().at(board.positionFactory().pos(target.i(), target.j())).equals(Optional.empty())) {
            for (BoardPos it : board.positionFactory().pos(target.i(), target.j()).neighbours()) {
                if (this.canStepTo(target) && armyOnTurn().boardTroops().troopPositions().contains(it))
                    return true;
            }
        }

        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            GameState newGameState = createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);

            return newGameState;
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
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
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    public void toJSON(PrintWriter writer) {
        Board board = this.board();

        String json = "{";

        json += "\"result\":";
        json += "\"" + this.result() + "\"";

        json += ",";

        json += "\"board\":";
        json += "{";

        json += "\"dimension\":";
        json += board.dimension();
        json += ",";

        json += "\"tiles\":";
        json += "[";

        for (int i = 0; i < board.dimension(); ++i) {
            for (int j = 0; j < board.dimension(); ++j) {
                if (board.at(board.positionFactory().pos(j, i)) == BoardTile.EMPTY) {
                    json += "\"empty\",";
                } else if (board.at(board.positionFactory().pos(j, i)) == BoardTile.MOUNTAIN) {
                    json += "\"mountain\",";
                }
            }
        }

        json = json.substring(0, json.length() - 1);

        json += "]";
        json += "},";

        json += "\"blueArmy\":";
        json += "{";

        json += "\"boardTroops\":";
        json += "{";

        json += "\"side\":";
        json += "\"BLUE\"";
        json += ",";

        json += "\"leaderPosition\":";
        json += "\"" + blueArmy.boardTroops().leaderPosition() + "\"";
        json += ",";

        json += "\"guards\":";
        json += blueArmy.boardTroops().guards();
        json += ",";

        json += "\"troopMap\":";
        json += "{";

        //Map<BoardPos, TroopTile> blueArmyPos = blueArmy.boardTroops().troopMap();
        Map<BoardPos, TroopTile> blueArmyPos = new TreeMap<>(blueArmy.boardTroops().troopMap());

        String[] jsonBlueArmyTroops = new String[1];
        jsonBlueArmyTroops[0] = "";

        blueArmyPos.forEach((pos, tile) -> {
            jsonBlueArmyTroops[0] += "\"" + pos.toString() + "\":";
            jsonBlueArmyTroops[0] += "{";

            jsonBlueArmyTroops[0] += "\"troop\":";
            jsonBlueArmyTroops[0] += "\"" + tile.troop().name() + "\"";
            jsonBlueArmyTroops[0] += ",";

            jsonBlueArmyTroops[0] += "\"side\":";
            jsonBlueArmyTroops[0] += "\"" + tile.side() + "\"";
            jsonBlueArmyTroops[0] += ",";

            jsonBlueArmyTroops[0] += "\"face\":";
            jsonBlueArmyTroops[0] += "\"" + tile.face() + "\"";
            jsonBlueArmyTroops[0] += "}";
            jsonBlueArmyTroops[0] += ",";
        });

        if (!blueArmyPos.isEmpty()) {
            jsonBlueArmyTroops[0] = jsonBlueArmyTroops[0].substring(0, jsonBlueArmyTroops[0].length() - 1);
            json += jsonBlueArmyTroops[0];
        }

        json += "}";
        json += "}";
        json += ",";

        json += "\"stack\":";
        json += "[";

        String[] jsonBlueArmyStack = new String[1];
        jsonBlueArmyStack[0] = "";

        blueArmy.stack().stream().forEach(troop -> {
            jsonBlueArmyStack[0] += "\"" + troop.name() + "\"";
            jsonBlueArmyStack[0] += ",";
        });

        if (jsonBlueArmyStack[0] != "") {
            // remove last comma
            jsonBlueArmyStack[0] = jsonBlueArmyStack[0].substring(0, jsonBlueArmyStack[0].length() - 1);

            // join stack string to the main json string
            json += jsonBlueArmyStack[0];
        }

        json += "]";
        json += ",";

        json += "\"captured\":";
        json += "[";

        String[] jsonBlueArmyCaptured = new String[1];
        jsonBlueArmyCaptured[0] = "";

        blueArmy.captured().stream().forEach(troop -> {
            jsonBlueArmyCaptured[0] += "\"" + troop.name() + "\"";
            jsonBlueArmyCaptured[0] += ",";
        });

        if (jsonBlueArmyCaptured[0] != "") {
            // remove last comma
            jsonBlueArmyCaptured[0] = jsonBlueArmyCaptured[0].substring(0, jsonBlueArmyCaptured[0].length() - 1);

            // join captured string to the main json string
            json += jsonBlueArmyCaptured[0];
        }

        json += "]";
        json += "}";

        json += ",";

        json += "\"orangeArmy\":";
        json += "{";

        json += "\"boardTroops\":";
        json += "{";

        json += "\"side\":";
        json += "\"ORANGE\"";
        json += ",";

        json += "\"leaderPosition\":";
        json += "\"" + orangeArmy.boardTroops().leaderPosition() + "\"";
        json += ",";

        json += "\"guards\":";
        json += orangeArmy.boardTroops().guards();
        json += ",";

        json += "\"troopMap\":";
        json += "{";

        Map<BoardPos, TroopTile> orangeArmyPos = new TreeMap<>(orangeArmy.boardTroops().troopMap());

        String[] jsonOrangeArmyTroops = new String[1];
        jsonOrangeArmyTroops[0] = "";

        orangeArmyPos.forEach((pos, tile) -> {
            jsonOrangeArmyTroops[0] += "\"" + pos.toString() + "\":";
            jsonOrangeArmyTroops[0] += "{";

            jsonOrangeArmyTroops[0] += "\"troop\":";
            jsonOrangeArmyTroops[0] += "\"" + tile.troop().name() + "\"";
            jsonOrangeArmyTroops[0] += ",";

            jsonOrangeArmyTroops[0] += "\"side\":";
            jsonOrangeArmyTroops[0] += "\"" + tile.side() + "\"";
            jsonOrangeArmyTroops[0] += ",";

            jsonOrangeArmyTroops[0] += "\"face\":";
            jsonOrangeArmyTroops[0] += "\"" + tile.face() + "\"";
            jsonOrangeArmyTroops[0] += "}";
            jsonOrangeArmyTroops[0] += ",";
        });

        if (!orangeArmyPos.isEmpty()) {
            jsonOrangeArmyTroops[0] = jsonOrangeArmyTroops[0].substring(0, jsonOrangeArmyTroops[0].length() - 1);
            json += jsonOrangeArmyTroops[0];
        }

        json += "}";
        json += "}";
        json += ",";

        json += "\"stack\":";
        json += "[";

        String[] jsonOrangeArmyStack = new String[1];
        jsonOrangeArmyStack[0] = "";

        orangeArmy.stack().stream().forEach(troop -> {
            jsonOrangeArmyStack[0] += "\"" + troop.name() + "\"";
            jsonOrangeArmyStack[0] += ",";
        });

        if (jsonOrangeArmyStack[0] != "") {
            // remove last comma
            jsonOrangeArmyStack[0] = jsonOrangeArmyStack[0].substring(0, jsonOrangeArmyStack[0].length() - 1);

            // join stack string to the main json string
            json += jsonOrangeArmyStack[0];
        }

        json += "]";
        json += ",";

        json += "\"captured\":";
        json += "[";

        String[] jsonOrangeArmyCaptured = new String[1];
        jsonOrangeArmyCaptured[0] = "";

        orangeArmy.captured().stream().forEach(troop -> {
            jsonOrangeArmyCaptured[0] += "\"" + troop.name() + "\"";
            jsonOrangeArmyCaptured[0] += ",";
        });

        if (jsonOrangeArmyCaptured[0] != "") {
            // remove last comma
            jsonOrangeArmyCaptured[0] = jsonOrangeArmyCaptured[0].substring(0, jsonOrangeArmyCaptured[0].length() - 1);

            // join captured string to the main json string
            json += jsonOrangeArmyCaptured[0];
        }

        json += "]";
        json += "}";

        json += "}";
        writer.printf(json);
    }
}
