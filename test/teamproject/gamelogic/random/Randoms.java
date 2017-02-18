package teamproject.gamelogic.random;

import java.util.HashMap;
import java.util.Random;

import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.ControlledPlayer;
import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.LocalGhost;
import teamproject.gamelogic.domain.LocalPlayer;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;
import teamproject.gamelogic.domain.RemoteGhost;
import teamproject.gamelogic.domain.RemotePlayer;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.Scoreboard;
import teamproject.gamelogic.domain.World;
import teamproject.gamelogic.domain.stubs.CellStub;
import teamproject.gamelogic.domain.stubs.MapStub;

public class Randoms {

	private static Random random = new Random();
	private static char[] CHARS = "1234567890qwertyuiopasdfghjklmnbvcxz".toCharArray();

	public static boolean randomBoolean() {
		return random.nextBoolean();
	}

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
		return new World(randomRuleEnforcer(), randomMap());
	}

	public static RemoteGhost randomRemoteGhost() {
		return new RemoteGhost(randomInteger());
	}

	public static LocalGhost randomLocalGhost() {
		return new LocalGhost();
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

	public static Inventory randomInventory() {
		final java.util.Map<Item, Integer> items = new HashMap<Item, Integer>();
		items.put(randomItem(), randomInteger());

		return new Inventory(items);
	}

	public static CellType randomCellType() {
		return randomEnum(CellType.class);
	}

	public static CellState randomCellState() {
		return randomEnum(CellState.class);
	}

	public static Position randomPosition() {
		return new Position(randomInteger(), randomInteger());
	}

	public static Position randomPositionInRange(final int range) {
		return new Position(randomInteger(range), randomInteger(range));
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
				cells[i][j] = randomCell(new Position(i, j));
			}
		}

		return cells;
	}

	public static Scoreboard randomScoreboard() {
		final Scoreboard scoreboard = new Scoreboard();

		for (int i = 0; i < randomInteger(); i++) {
			scoreboard.setScoreForPlayerId(randomInteger(), randomInteger());
		}

		return scoreboard;
	}

	public static LocalPlayer randomLocalPlayer() {
		return new LocalPlayer(Randoms.randomString());
	}

	public static RemotePlayer randomRemotePlayer() {
		return new RemotePlayer(Randoms.randomInteger(), Randoms.randomString());
	}

	public static ControlledPlayer randomControlledPlayer() {
		return new ControlledPlayer(Randoms.randomInteger(), Randoms.randomString());
	}

}
