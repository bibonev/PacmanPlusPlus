package main.java.event.arguments;

import main.java.gamelogic.core.LobbyPlayerInfo;

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

		public LobbyPlayerJoinedEventArgs(final int id, final LobbyPlayerInfo info) {
			playerID = id;
			playerInfo = info;
		}
	}

	public static class LobbyPlayerLeftEventArgs extends LobbyChangedEventArgs {
		private int playerID;

		public int getPlayerID() {
			return playerID;
		}

		public LobbyPlayerLeftEventArgs(final int id) {
			playerID = id;
		}
	}

	public static class LobbyRulesChangedEventArgs extends LobbyChangedEventArgs {
		private String[] newRules;

		public String[] getNewRules() {
			return newRules;
		}

		public LobbyRulesChangedEventArgs(final String[] newRules) {
			this.newRules = newRules;
		}
	}
}
