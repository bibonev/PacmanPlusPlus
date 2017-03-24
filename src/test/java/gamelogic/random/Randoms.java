package test.java.gamelogic.random;

import java.util.Random;

import main.java.constants.CellState;
import main.java.constants.GameType;
import main.java.event.Event;
import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.arguments.PlayerLaserActivatedEventArgs;
import main.java.event.arguments.PlayerShieldActivatedEventArgs;
import main.java.event.arguments.PlayerShieldRemovedEventArgs;
import main.java.event.listener.PlayerCooldownChangedListener;
import main.java.event.listener.PlayerLaserActivatedListener;
import main.java.event.listener.PlayerShieldActivatedListener;
import main.java.event.listener.PlayerShieldRemovedListener;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.gamelogic.domain.Ability;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.ControlledPlayer;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.LocalGhost;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.LocalSkillSet;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.RemoteGhost;
import main.java.gamelogic.domain.RemotePlayer;
import main.java.gamelogic.domain.RuleChecker;
import main.java.gamelogic.domain.Scoreboard;
import main.java.gamelogic.domain.SkillSet;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.domain.stubs.AbilityStub;
import test.java.gamelogic.domain.stubs.CellStub;
import test.java.gamelogic.domain.stubs.MapStub;

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
		return new World(randomRuleEnforcer(), randomMap(), false);
	}

	public static RemoteGhost randomRemoteGhost() {
		return new RemoteGhost(randomInteger());
	}

	public static LocalGhost randomLocalGhost() {
		final LocalGhost localGhost = new LocalGhost();
		localGhost.setPosition(randomPositionInRange(15));
		return localGhost;
	}

	public static RuleChecker randomRuleEnforcer() {
		return new RuleChecker();
	}

	public static Map randomMap() {
		return new MapStub(randomCells(randomInteger(15)));
	}

	public static GameSettings randomGameSettings() {
		return new GameSettings();
	}

	public static Ability randomAbility() {
		return new AbilityStub(randomString());
	}

	public static LocalSkillSet randomLocalSkillSet() {
		return new LocalSkillSet();
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
		return new CellStub(randomCellState(), randomPosition());
	}

	public static Cell randomCell(final Position position) {
		return new CellStub(randomCellState(), position);
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

	public static Game randomGame() {
		return new Game(randomWorld(), randomGameSettings(), GameType.SINGLEPLAYER);
	}

	public static LobbyPlayerInfo randomLobbyPlayerInfo() {
		return new LobbyPlayerInfo(Randoms.randomInteger(), Randoms.randomString());
	}

}
