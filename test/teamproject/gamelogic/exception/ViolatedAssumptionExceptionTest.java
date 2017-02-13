package teamproject.gamelogic.exception;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class ViolatedAssumptionExceptionTest {

	@Test
	public void shouldConstruct() {
		// Given
		final String message = Randoms.randomString();

		// When
		final ViolatedAssumptionException exception = new ViolatedAssumptionException(message);

		// Then
		assertThat(exception.getMessage(), Is.is("Assumption was violated: " + message));
	}
}
