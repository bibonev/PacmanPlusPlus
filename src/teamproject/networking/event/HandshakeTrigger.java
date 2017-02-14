package teamproject.networking.event;

import teamproject.networking.data.Packet;

public class HandshakeTrigger implements ClientTrigger {
	private HandshakeListener listener;
	private boolean alreadyDoneHandshake;

	public HandshakeTrigger(HandshakeListener listener) {
		this.listener = listener;
		this.alreadyDoneHandshake = false;
	}
	
	@Override
	public void trigger(Packet p) {
		if(p.getPacketName().equals("server-handshake")) {
			if(!alreadyDoneHandshake) {
				int clientID = p.getInteger("client-id");
				
				listener.onServerHandshake(clientID);
				this.alreadyDoneHandshake = true;
			} else {
				throw new RuntimeException("Already received handshake packet from server.");
			}
		}
	}
}
