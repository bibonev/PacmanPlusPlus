package test.java.gamelogic.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import main.java.ai.AIPlayer;
import main.java.gamelogic.domain.Entity;
import main.java.gamelogic.domain.PacBomb;
import main.java.gamelogic.domain.PacLaser;
import main.java.gamelogic.domain.PacShield;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.random.Randoms;

public class SkillsTest {

	@Test
	public void shouldConstructAndActivate() {

		// When
		final PacShield pacShield = new PacShield();
		final PacLaser pacLaser = new PacLaser();
		World world = Randoms.randomWorld();
		AIPlayer player = new AIPlayer();
		((Entity) player).setPosition(new Position(0,0));
		player.setWorld(world);
		player.setID(-1);
		pacShield.setOwner(player);
		pacLaser.setOwner(player);
		pacShield.activate();
		pacLaser.activate();
		// Then
		assertTrue(pacShield.getCD() == 0 );
		assertTrue(pacLaser.getCD() == 0 );
	}

}
