package teamproject.gamelogic.domain.repository;

import java.util.ArrayList;
import java.util.List;

import teamproject.gamelogic.domain.Game;
import teamproject.gamelogic.domain.Player;

/**
 * For now this is a placeholder for a real database
 *
 * @author aml
 *
 */

public class Repository {
	private static List<Player> humanPlayers = new ArrayList<Player>();
	private static List<Game> games = new ArrayList<Game>();
	private static long userCounter = 0;
	private static long gameCounter = 0;

	// For now it uses names to fetch the player - should use IDs in future
	public static Player getHumanPlayer(final String humanPlayerName) {
		return humanPlayers.stream().filter(humanPlayer -> humanPlayer.getName().equals(humanPlayerName)).findFirst()
				.get();
	}

	public static void addHumanPlayer(final Player humanPlayer) {
		humanPlayers.add(humanPlayer);
	}

	public static void addGame(final Game game) {
		games.add(game);
	}

	public static long nextHumanPlayerId() {
		return userCounter++;
	}

	public static long nextGameId() {
		return gameCounter++;
	}

}
