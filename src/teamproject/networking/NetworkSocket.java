package teamproject.networking;

import teamproject.event.Event;

public interface NetworkSocket {
	public void send(byte[] data);
	public Event<NetworkSocketListener, byte[]> getReceiveEvent();
}
