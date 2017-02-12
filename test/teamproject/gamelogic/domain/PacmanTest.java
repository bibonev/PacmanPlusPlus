package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.domain.stubs.PacmanStub;
import teamproject.gamelogic.random.Randoms;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public class PacmanTest {
    @Test
    public void shouldConstruct() {
        // Given
        final String name = Randoms.randomString();
        final Behaviour behaviour = Randoms.randomBehaviour();

        // When
        final Pacman pacman = new PacmanStub(behaviour, name);

        // Then
        assertThat(pacman.getBehaviour(), Is.is(behaviour));
        assertThat(pacman.getName(), Is.is(name));
    }
}
