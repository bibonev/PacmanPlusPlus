package test.java.gamelogic.domain;

import main.java.gamelogic.domain.PacLaser;
import main.java.gamelogic.domain.PacShield;
import org.hamcrest.core.Is;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by simeonkostadinov on 27/03/2017.
 */
public class PacShieldTest {


        public static final int MAX_SHIELD = 4;

        @Test
        public void shouldConstruct() {
            // When
            final PacShield pacShield = new PacShield();
            int shieldValue = MAX_SHIELD;


            pacShield.activate();

            pacShield.setCD(40);
            int coolDown = pacShield.getCD();

            if(coolDown == 40){
                pacShield.activate();
                shieldValue = pacShield.getShieldValue();
            }

            // Then
            assertThat(pacShield.getName(), Is.is("PacShield"));
            assertThat(shieldValue, Is.is(MAX_SHIELD));
        }
}
