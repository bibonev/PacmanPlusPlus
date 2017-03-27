package test.java.event.arguments;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.event.arguments.MultiplayerGameStartingEventArgs;
import main.java.gamelogic.domain.GameSettings;
import main.java.gamelogic.domain.Map;
import test.java.gamelogic.random.Randoms;

public class MultiplayerGameStartingEventArgsTest {

	@Test
	public void shouldConstruct_serverSide() {
		// Given
		final GameSettings settings = Randoms.randomGameSettings();

		// When
		final MultiplayerGameStartingEventArgs event = new MultiplayerGameStartingEventArgs(settings);

		// Then
		assertThat(event.getSettings(), Is.is(settings));
		assertThat(event.isServer(), Is.is(true));
	}

	@Test
	public void shouldConstruct_clientSide() {
		// Given
		final GameSettings settings = Randoms.randomGameSettings();
		final int localPlayerID = Randoms.randomInteger();
		final String localPlayerUsername = Randoms.randomString();

		// When
		final MultiplayerGameStartingEventArgs event = new MultiplayerGameStartingEventArgs(settings, localPlayerID,
				localPlayerUsername, Map.generateMap());

		// Then
		assertThat(event.getSettings(), Is.is(settings));
		assertThat(event.isServer(), Is.is(false));
		assertThat(event.getLocalPlayerID(), Is.is(localPlayerID));
		assertThat(event.getLocalUsername(), Is.is(localPlayerUsername));

	}

	@Test
	public void shouldNotGetLocalPlayerInfo_serverSide() {
		// Given
		final GameSettings settings = Randoms.randomGameSettings();
		final MultiplayerGameStartingEventArgs event = new MultiplayerGameStartingEventArgs(settings);
		Exception exception1 = null;
		Exception exception2 = null;

		// When
		try {
			event.getLocalPlayerID();
		} catch (final IllegalStateException expected) {
			exception1 = expected;
		}

		try {
			event.getLocalUsername();
		} catch (final IllegalStateException expected) {
			exception2 = expected;
		}

		assertNotNull(exception1);
		assertNotNull(exception2);
		assertThat(exception1.getMessage(),
				Is.is("Can't get local player ID when generating multiplayer game for server."));
		assertThat(exception2.getMessage(),
				Is.is("Can't get local player username when generating multiplayer game for server."));
	}

}
