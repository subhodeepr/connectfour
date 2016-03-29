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
	List<GameRoom> gamerooms;
	Player player;

	public Lobby(BufferedReader is, PrintWriter os, Socket s, Map<Player, GameRoom> plist, Player pl,
			List<GameRoom> gr) {
		inputStream = is;
		outputStream = os;
		socket = s;
		playerList = plist;
		player = pl;
		gamerooms = gr;

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
	
	private void gameLoop(Player p, String gameroomName, GameRoom gameroom){
		try {
			line = inputStream.readLine();
			while (line != null) {
				if (line.contains("quit")) {
					String msg = "Are you sure you want to quit the game?";
					outputStream.println(msg);
					outputStream.flush();
					line = inputStream.readLine();
					if (line.equals("yes")) {
						for (Entry<Player, GameRoom> entry : playerList.entrySet()) {
							if (entry.getKey() == p) {
								entry.setValue(null);
								if(gameroom.getPlayer1() != null){
									if (gameroom.getPlayer1() == p)
										gameroom.setPlayer1(null);
								}
								if(gameroom.getPlayer2() !=null){
									if (gameroom.getPlayer2() == p)
										gameroom.setPlayer2(null);
								}
								if (gameroom.getPlayer1() == null && gameroom.getPlayer2() == null){
									gamerooms.remove(gameroom);
								}
							}
						}
						return;
					}
				}
			}
			line = inputStream.readLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	public void createGameroom(Player p1, String gameroomName) {
		GameRoom gameroom = new GameRoom(p1, gameroomName);
		gamerooms.add(gameroom);
		for (Entry<Player, GameRoom> entry : playerList.entrySet()) {
			if (entry.getKey() == p1) {
				entry.setValue(gameroom);
			}
		}
		String msg = gameroomName + " created" + "\n" + "You are in " + gameroomName + "\n"
				+ "Waiting on another player to join...";
		outputStream.println(msg);
		outputStream.flush();
		gameLoop(p1, gameroomName, gameroom);

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

	public void joinGameroom(String line) {
		String[] commands = line.split("\\s+");
		boolean roomFound = false;
		for (GameRoom room : gamerooms) {
			if (room.getGameRoomName().equals(commands[2])) {
				roomFound = true;
				if (room.getPlayer1() != null && room.getPlayer2() !=null)
				{
					outputStream.println("Instance is full.");
					outputStream.flush();
					
				}
				else{
					for (Entry<Player, GameRoom> entry : playerList.entrySet()) {
						if (entry.getKey() == player) {
							entry.setValue(room);
						}
					}
					room.setPlayer2(player);
					outputStream.println("You have joined " + room.getGameRoomName());
					outputStream.flush();
					gameLoop(player, commands[2], room);
				}

			}
		}
		if (!roomFound){
			outputStream.println("No gameroom with name " + commands[2] + " exists.");
			outputStream.flush();
		}
		
	}

}
