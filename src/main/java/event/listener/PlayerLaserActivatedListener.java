package main.java.event.listener;

import main.java.event.arguments.PlayerCooldownChangedEventArgs;
import main.java.event.arguments.PlayerLaserActivatedEventArgs;

/**
 * Created by simeonkostadinov on 22/03/2017.
 */
public interface PlayerLaserActivatedListener {
    public void onPlayerLaserActivated(PlayerLaserActivatedEventArgs args);

}
