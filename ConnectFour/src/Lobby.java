import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Lobby {
	int maxCapactity;
	int connectedUsers;
	int gamesInProgress;
	String ranking[];

	Socket socket;
	String line;
	BufferedReader inputStream;
	PrintWriter outputStream;
	Map<Player, GameRoom> playerList;
	Map<String, String> userCredentials;

	Player player;

	public Lobby(BufferedReader is, PrintWriter os, Socket s, Map<Player, GameRoom> plist, Player pl) {
		inputStream = is;
		outputStream = os;
		socket = s;
		playerList = plist;
		player = pl;

	}

	public void spectateGame(String gameRoom) {

	}

	public void challengePlayer(String username) {

	}

	public void list() {
		try {
			String players = "";
			String gameRooms = "";

			for (Map.Entry<Player, GameRoom> entry : playerList.entrySet()) {
				players += entry.getKey().getUserName() + ",";
				try {
					gameRooms += entry.getValue().getGameRoomName() + ",";
				} catch (NullPointerException e) {

					gameRooms += "Lobby" + ",";

				}

			}
			outputStream.println(players);
			outputStream.flush();
			outputStream.println(gameRooms);
			outputStream.flush();
		} catch (NullPointerException e) {
			e.printStackTrace();

		}
	}

	public void logout() {
		try {
			String logoutConf = "n3 Are you sure you want to logout? ";
			outputStream.println(logoutConf);
			outputStream.flush();
			line = inputStream.readLine();
			if (line.equalsIgnoreCase("yes")) {
				outputStream.println("1");
				outputStream.flush();
				playerList.remove(player);
			} else {

				outputStream.println("0");
				outputStream.flush();

			}
		} catch (NullPointerException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void invalidCommand() {
		System.out.println("Invalid command. Please enter a valid command");

	}

	public void leaderboard(Map<String, Integer> lb) {
		try {
			String players = "";
			String scores = "";

			for (Map.Entry<String, Integer> entry : lb.entrySet()) {
				players += entry.getKey() + ",";
				scores += entry.getValue() + ",";

			}
			outputStream.println(players);
			outputStream.flush();
			outputStream.println(scores);
			outputStream.flush();
		} catch (NullPointerException e) {
			e.printStackTrace();

		}

	}

	public void createGameroom(Player p1, String gameroomName) {
		GameRoom gameroom = new GameRoom(p1, gameroomName);
		for (Entry<Player, GameRoom> entry : playerList.entrySet()) {
			if (entry.getKey() == p1) {
				entry.setValue(gameroom);

			}
		}

	}

	public void createGameroomWithUser(Player p1, Player p2, String gameroomName) {
		GameRoom gameroom = new GameRoom(p1, p2, gameroomName);

	}

	public void banUser(String command) {
		String username = command.substring(7, command.length());
		Player p1 = null;
		Player p2 = null;
		for (Map.Entry<Player, GameRoom> entry : playerList.entrySet()) {
			if (entry.getKey().getSocket() == this.socket) {
				p1 = entry.getKey();
			} else if (entry.getKey().getUserName().equalsIgnoreCase(username)) {
				p2 = entry.getKey();

			}
		}
		if (p2 == null) {

			outputStream.println("Player " + username + " does not exist or is not online.");
			outputStream.flush();
		}

		
		else {
			p1.banList.add(p2);
			outputStream.println("Player " + username + " is banned. ");
			outputStream.flush();
		}
	}

	public void unbanUser(String command) {

		String username = command.substring(9, command.length());
		Player p1 = null;
		Player p2 = null;
		for (Map.Entry<Player, GameRoom> entry : playerList.entrySet()) {
			if (entry.getKey().getSocket() == this.socket) {
				p1 = entry.getKey();
			} else if (entry.getKey().getUserName().equalsIgnoreCase(username)) {
				p2 = entry.getKey();
			}
		}
		if (p2 == null) {

			outputStream.println("Player " + username + " does not exist or is not online.");
			outputStream.flush();
		}

		
		else {
			p1.banList.remove(p2);
			outputStream.println("Player " + username + " is unbanned.");
			outputStream.flush();
		}
	}
}
