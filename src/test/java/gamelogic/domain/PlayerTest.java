package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.World;
import test.java.gamelogic.random.Randoms;

public class PlayerTest {
	@Test
	public void shouldConstruct() {
		// Given
		final String name = Randoms.randomString();
		final double angle = 1.0;
		final Position pos = new Position(0, 0);
		final World world = Randoms.randomWorld();

		// When
		final LocalPlayer player = new LocalPlayer(name);
		((Player) player).setID(-1);
		((Player) player).setPosition(pos);
		((Player) player).setAngle(angle);
		((Player) player).setDeathReason("Killed by laser");
		((Player) player).setLaserFired(true);
		// ((Player)player).setShield(10);
		((Player) player).setWorld(world);

		// Then
		assertTrue(((Player) player).getAngle() == angle);
		assertTrue(((Player) player).getDeathReason().equals("Killed by laser"));
		assertTrue(((Player) player).getID() == -1);
		assertTrue(((Player) player).getLaserFired() == true);
		assertTrue(((Player) player).getPosition().equals(pos));
		// assertTrue(((Player) player).getShield() == 10);
		assertThat(((Player) player).getWorld(), Is.is(world));
		assertTrue(((Player) player).getDotsEaten() == 0);

	}
}
