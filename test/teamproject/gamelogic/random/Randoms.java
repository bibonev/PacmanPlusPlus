package teamproject.gamelogic.random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import teamproject.gamelogic.domain.GameSettings;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Item;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.RuleEnforcer;
import teamproject.gamelogic.domain.World;

public class Randoms {

	private static Random random = new Random();
	private static char[] CHARS = "1234567890qwertyuiopasdfghjklmnbvcxz".toCharArray();

	public static int randomInteger(final int upperBound) {
		return random.nextInt(upperBound);
	}

	public static int randomInteger() {
		return randomInteger(100);
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
		return new Player(randomString());
	}

	public static Ghost randomGhost() {
		return new Ghost();
	}

	public static RuleEnforcer randomRuleEnforcer() {
		return new RuleEnforcer();
	}

	public static Map randomMap() {
		return new Map();
	}

	public static GameSettings randomGameSettings() {
		return new GameSettings();
	}

	public static Item randomItem() {
		return new Item(randomString(), randomString());
	}

}
