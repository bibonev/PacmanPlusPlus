package test.java.gamelogic.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import main.java.gamelogic.domain.GameSettings;
import test.java.gamelogic.random.Randoms;

public class GameSettingsTest {

	@Test
	public void shouldConstruct() {
		
		//Given
		GameSettings settings = Randoms.randomGameSettings();
		
		//When
		settings.setInitialPlayerLives(3);
		settings.setAIPlayer(true);
		
		//Then
		assertTrue(settings.getInitialPlayerLives()==3);
		assertTrue(settings.getAIPlayer()==true);
	}

}
