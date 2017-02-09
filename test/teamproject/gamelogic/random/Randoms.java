package teamproject.gamelogic.random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Cell;
import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.Scoreboard;
import teamproject.gamelogic.domain.World;
import teamproject.gamelogic.domain.stubs.CellStub;
import teamproject.gamelogic.domain.stubs.MapStub;
import teamproject.gamelogic.domain.stubs.PositionStub;

public class Randoms {

	private static Random random = new Random();
	private static char[] CHARS = "1234567890qwertyuiopasdfghjklmnbvcxz".toCharArray();

	public static int randomInteger(final int upperBound) {
		if (upperBound < 0) {
			return random.nextInt(-upperBound);
		}
		return random.nextInt(upperBound);
	}

	public static int randomInteger() {
		return randomInteger(100);
	}

	public static long randomLong() {
		return random.nextLong();
	}

	public static <T extends Enum<?>> T randomEnum(final Class<T> enumClass) {
		final int i = randomInteger(enumClass.getEnumConstants().length);
		return enumClass.getEnumConstants()[i];
	}

	public static String randomString(final int length) {
		final StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < length; i++) {
			stringBuilder.append(CHARS[randomInteger(CHARS.length)]);
		}

		return stringBuilder.toString();
	}

	public static String randomString() {
		return randomString(20);
	}

	public static World randomWorld() {
		final Collection<Player> players = new ArrayList<Player>();
		players.add(randomPlayer());

		final Collection<Ghost> ghosts = new ArrayList<Ghost>();
		ghosts.add(randomGhost());

		return new World(players, randomRuleEnforcer(), ghosts, randomMap());
	}

	public static Player randomPlayer() {
		return new Player(randomLong(), randomString());
	}

	public static Ghost randomGhost() {
		return new Ghost(randomBehaviour(), randomString());
	}

	public static RuleEnforcer randomRuleEnforcer() {
		return new RuleEnforcer();
	}

	public static Map randomMap() {
		return new MapStub(randomCells(randomInteger()));
	}

	public static GameSettings randomGameSettings() {
		return new GameSettings();
	}

	public static Item randomItem() {
		return new Item(randomString(), randomString());
	}

	public static CellType randomCellType() {
		return randomEnum(CellType.class);
	}

	public static CellState randomCellState() {
		return randomEnum(CellState.class);
	}

	public static Position randomPosition() {
		return new PositionStub(randomInteger(), randomInteger());
	}

	public static Position randomPositionInRange(final int range) {
		return new PositionStub(randomInteger(range), randomInteger(range));
	}

	public static Cell randomCell() {
		return new CellStub(randomCellType(), randomCellState(), randomPosition());
	}

	public static Cell randomCell(final Position position) {
		return new CellStub(randomCellType(), randomCellState(), position);
	}

	public static Cell[][] randomCells(final int numberOfCells) {
		final Cell[][] cells = new Cell[numberOfCells][numberOfCells];

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = randomCell(new PositionStub(i, j));
			}
		}

		return cells;
	}

	public static Cell randomCell(final Position position) {
		return new Cell(randomCellType(), randomCellState(), position);
	}

	public static Scoreboard randomScoreboard() {
		final Scoreboard scoreboard = new Scoreboard();

		for (int i = 0; i < randomInteger(); i++) {
			scoreboard.setScoreForPlayerId(randomLong(), randomInteger());
		}

		return scoreboard;
	}

	private static Behaviour randomBehaviour() {
		return new BehaviourStub(randomEnum(Behaviour.Type.class));
	}

}
