package main.java.event.listener;

import main.java.event.arguments.PlayerLaserActivatedEventArgs;
import main.java.event.arguments.PlayerShieldActivatedEventArgs;

/**
 * Created by Simeon Kostadinov on 22/03/2017.
 */
public interface PlayerShieldActivatedListener {
    public void onPlayerShieldActivated(PlayerShieldActivatedEventArgs args);
}
