package test.java.gamelogic.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.core.LobbyPlayerInfo;
import test.java.gamelogic.random.Randoms;

public class LobbyPlayerInfoTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();
		final int id = Randoms.randomInteger();

		// When
		final LobbyPlayerInfo lobbyPlayerInfo = new LobbyPlayerInfo(id, name);
		lobbyPlayerInfo.setRemainingLives(1);

		// Then
		assertThat(lobbyPlayerInfo.getID(), Is.is(id));
		assertThat(lobbyPlayerInfo.getName(), Is.is(name));
		assertFalse(lobbyPlayerInfo.isInGame());
		assertFalse(lobbyPlayerInfo.isReady());
		assertThat(lobbyPlayerInfo.getRemainingLives(), Is.is(1));
	}

}
