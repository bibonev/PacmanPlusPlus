package test.java.gamelogic.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.core.Lobby;
import main.java.gamelogic.core.LobbyPlayerInfo;
import test.java.gamelogic.random.Randoms;

public class LobbyTest {

	@Test
	public void shouldConstruct() {
		// When
		final Lobby lobby = new Lobby();

		// Then
		assertThat(lobby.getPlayerCount(), Is.is(0));
		assertThat(lobby.getPlayerIDs().size(), Is.is(0));
		assertThat(lobby.getSettingsString(), Is.is(new String[0]));
		assertNotNull(lobby.getLobbyStateChangedEvent());
		assertTrue(lobby.allReady());
	}

	@Test
	public void shouldHandlePlayersCorrectly() {
		// Given
		final Lobby lobby = new Lobby();
		final LobbyPlayerInfo playerInfo = Randoms.randomLobbyPlayerInfo();

		// When
		lobby.addPlayer(0, playerInfo);

		// Then
		assertThat(lobby.getPlayer(0), Is.is(playerInfo));
		assertTrue(lobby.containsPlayer(0));

		// When
		lobby.removePlayer(0);

		// Then
		assertThat(lobby.getPlayerCount(), Is.is(0));
		assertFalse(lobby.containsPlayer(0));
	}

	@Test
	public void shouldResetReady() {
		// Given
		final Lobby lobby = new Lobby();

		// When
		lobby.addPlayer(0, Randoms.randomLobbyPlayerInfo());
		lobby.resetReady();

		// Then
		assertFalse(lobby.allReady());
	}

}
