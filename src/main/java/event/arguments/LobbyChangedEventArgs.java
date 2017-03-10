package teamproject.event.arguments;

import teamproject.gamelogic.core.LobbyPlayerInfo;

public abstract class LobbyChangedEventArgs {
	public static class LobbyPlayerJoinedEventArgs extends LobbyChangedEventArgs {
		private int playerID;
		private LobbyPlayerInfo playerInfo;
		
		public int getPlayerID() {
			return playerID;
		}
		
		public LobbyPlayerInfo getPlayerInfo() {
			return playerInfo;
		}
		
		public LobbyPlayerJoinedEventArgs(int id, LobbyPlayerInfo info) {
			this.playerID = id;
			this.playerInfo = info;
		}
	}

	public static class LobbyPlayerLeftEventArgs extends LobbyChangedEventArgs {
		private int playerID;
		
		public int getPlayerID() {
			return playerID;
		}
		
		public LobbyPlayerLeftEventArgs(int id) {
			this.playerID = id;
		}
	}

	public static class LobbyRulesChangedEventArgs extends LobbyChangedEventArgs {
		private String[] newRules;
		
		public String[] getNewRules() {
			return newRules;
		}
		
		public LobbyRulesChangedEventArgs(String[] newRules) {
			this.newRules = newRules;
		}
	}
}
