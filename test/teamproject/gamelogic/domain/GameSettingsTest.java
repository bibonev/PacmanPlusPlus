package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class GameSettingsTest {

	@Test
	public void shouldConstruct() {
		// Given
		final boolean musicOn = Randoms.randomBoolean();
		final boolean soundsOn = Randoms.randomBoolean();

		// When
		final GameSettings gameSettings = new GameSettings(musicOn, soundsOn);

		// Then
		assertThat(gameSettings.isMusicOn(), Is.is(musicOn));
		assertThat(gameSettings.isSoundsOn(), Is.is(soundsOn));
	}

}
