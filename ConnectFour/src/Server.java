import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

	public static void main(String args[]) {

		Map<Player, GameRoom> playerList = new HashMap<Player, GameRoom>();
		Map<String, String> userCredentials = new HashMap<String, String>();
		Map<String, Integer> leaderBoards = new HashMap<String, Integer>();
		List<GameRoom> gameRooms = new ArrayList<GameRoom>();
		userCredentials.put("John", "johndoe");
		userCredentials.put("Jane", "janedoe");
		userCredentials.put("Jack", "jackdoe");

		leaderBoards.put("John", 3);
		leaderBoards.put("Jane", 4);
		leaderBoards.put("Jack", 2);

		if (args.length != 0) {
			System.out.println("Usage: Server");
			System.exit(1);
		}

		Socket s = null;
		ServerSocket ss2 = null;
		System.out.println("ConnectFour server started");
		try {
			ss2 = new ServerSocket(9999);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server error");

		}

		while (true) {
			try {
				s = ss2.accept();
				System.out.println("Connection established from " + s.getLocalAddress());
				ServerThread st = new ServerThread(s, playerList, userCredentials, leaderBoards, gameRooms);
				st.start();

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}

	}
}

class ServerThread extends Thread {

	BufferedReader inputStream = null;
	PrintWriter outputStream = null;
	Socket s = null;
	String line;
	Map<Player, GameRoom> pl;
	Map<String, String> uc;
	Map<String, Integer> lb;
	List<GameRoom> gr;

	Player player = null;
	Lobby lobby = null;

	public ServerThread(Socket s, Map<Player, GameRoom> pl, Map<String, String> uc, Map<String, Integer> lb,
			List<GameRoom> gr) {
		this.s = s;
		this.pl = pl;
		this.uc = uc;
		this.lb = lb;
		this.gr = gr;

	}

	public void run() {
		try {
			inputStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			outputStream = new PrintWriter(s.getOutputStream());

		} catch (IOException e) {
			System.out.println("IO error in server thread");
		}

		try {
			ServerAuthenticationHandler authHandler = new ServerAuthenticationHandler(inputStream, outputStream, s, pl,
					uc);
			player = authHandler.authenticate();
			lobby = new Lobby(inputStream, outputStream, s, pl, player, gr);
			line = inputStream.readLine();
			while (line != null) {
				if(line.substring(0, 2).equals("c1")){
					
					lobby.publicChat(line);
					line = inputStream.readLine();
				}
				if(line.substring(0, 2).equals("c2")){
					
					lobby.privateChat(line);
					line = inputStream.readLine();
				}
				else if (line.substring(3, line.length()).equalsIgnoreCase("logout")) {
					lobby.logout();
					line = inputStream.readLine();

				} else if (line.substring(3, line.length()).equalsIgnoreCase("list")) {
					lobby.list();
					line = inputStream.readLine();

				} else if (line.substring(3, line.length()).equalsIgnoreCase("leaderboard")) {
					lobby.leaderboard(lb);
					line = inputStream.readLine();
				}

				else if (line.substring(3, 6).equalsIgnoreCase("ban")) {
					lobby.banUser(line);
					line = inputStream.readLine();
				}

				else if (line.substring(3, 8).equals("unban")) {
					lobby.unbanUser(line);
					line = inputStream.readLine();
				}
				
				else if(line.substring(3, 7).equals("join")){
					
					lobby.joinGameroom(line);
					line = inputStream.readLine();
				}


				else if (line.substring(3, 6).equalsIgnoreCase("new")) {
					boolean exists = false;
					String[] commands = line.split("\\s+");
					for (GameRoom room : gr) {
						if (room.getGameRoomName().equals(commands[2])) {
							outputStream.println("A gameroom with the name " + commands[2] + " already exists.");
							outputStream.flush();
							exists = true;
							break;
						}
					}
					if (!exists) {
						if (commands.length - 1 == 2) {
							String gameroomName = commands[2];
							Player p1;
							for (Map.Entry<Player, GameRoom> entry : pl.entrySet()) {
								if (entry.getKey().getSocket() == this.s) {
									p1 = entry.getKey();
									lobby.createGameroom(p1, gameroomName);
								}
							}
						} else if (commands.length - 1 == 3) {
							String gameroomName = commands[2];
							Player p1 = null;
							Player p2 = null;
							for (Map.Entry<Player, GameRoom> entry : pl.entrySet()) {
								if (entry.getKey().getSocket() == this.s) {
									p1 = entry.getKey();
								} else if (entry.getKey().getUserName().equalsIgnoreCase(commands[3])) {
									p2 = entry.getKey();

								}
							}
							if (p2 == null) {

								outputStream.println("Player " + commands[3] + " does not exist or is not online.");
								outputStream.flush();
							} else {
								boolean isBanned = false;
								List<Player> banlist = p2.banList;
								for (Player p : banlist) {
									if (p.getUserName().equals(p1.getUserName())) {
										outputStream.println("Player " + commands[3] + " is unavailable.");
										outputStream.flush();
										isBanned = true;
									}

								}
								if (!isBanned) {
										p1.setPlayerToken('X');
										p2.setPlayerToken('Y');
										lobby.createGameroomWithUser(p1, p2, gameroomName);
									
								}
							}
						}
					}
					line = inputStream.readLine();
				}

			}

		} catch (NullPointerException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {

				System.out.println("Connection Closing..");
				if (inputStream != null) {
					inputStream.close();
					System.out.println(" Socket Input Stream Closed");
				}

				if (outputStream != null) {
					outputStream.close();
					System.out.println("Socket Out Closed");
				}
				if (s != null) {
					s.close();
					System.out.println("Socket Closed");
				}
				pl.remove(player);

			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		} // end finally
	}
}

class ServerAuthenticationHandler {
	BufferedReader inputStream;
	PrintWriter outputStream;
	Socket socket;
	String line = null;
	Map<String, String> userCredentials;
	Map<Player, GameRoom> playerlist;

	public ServerAuthenticationHandler(BufferedReader is, PrintWriter os, Socket s, Map<Player, GameRoom> pl,
			Map<String, String> uc) {

		inputStream = is;
		outputStream = os;
		socket = s;
		playerlist = pl;
		userCredentials = uc;

	}

	public Player authenticate() {

		String unValue;
		String pwValue;
		Player player = null;
		boolean foundUser = false;
		boolean validUser = false;
		boolean newUser = false;
		try {
			String username = "n1 Connected to IP Address: " + socket.getLocalAddress() + " Port: " + socket.getPort()
					+ "\n" + "n3 Enter username: ";
			outputStream.println(username);
			outputStream.flush();
			line = inputStream.readLine();
			while ((line == null || line.isEmpty())) {
				outputStream.println("n3 Please enter a valid username: ");
				outputStream.flush();
				line = inputStream.readLine();
			}
			for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
				if (entry.getKey().equals(line)) {

					foundUser = true;
				}

			}

			unValue = line;

			while (!foundUser) {
				outputStream.println("n4 Unable to find username. Do you wish to create a new account? ");
				outputStream.flush();
				line = inputStream.readLine();
				if (line.equals("yes")) {

					userCredentials.put(unValue, null);
					foundUser = true;
					newUser = true;
				} else {
					while ((line == null || line.isEmpty() || line.equals("no"))) {
						outputStream.println("n3 Enter username: ");
						outputStream.flush();
						line = inputStream.readLine();
					}
					for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
						if (entry.getKey().equals(line)) {

							foundUser = true;
							unValue = line;

						}
					}
				}
			}

			String password = "n3 Enter password: ";
			outputStream.println(password);
			outputStream.flush();
			line = inputStream.readLine();
			while ((line == null || line.isEmpty())) {
				outputStream.println("n3 Please enter a valid password: ");
				outputStream.flush();
				line = inputStream.readLine();
			}
			if (!newUser) {
				for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
					if (entry.getKey().equals(unValue) && entry.getValue().equals(line)) {

						validUser = true;
					}

				}
				while (!validUser) {
					outputStream.println("n4 Invalid password. Please enter a valid password: ");
					outputStream.flush();
					line = inputStream.readLine();
					for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
						if (entry.getKey().equals(unValue) && entry.getValue().equals(line)) {

							validUser = true;
						}
					}
				}
			}

			pwValue = line;

			if (newUser) {

				for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
					if (entry.getKey().equals(unValue)) {

						entry.setValue(pwValue);
					}

				}
			}

			player = new Player(unValue, socket);
			playerlist.put(player, null);
			String loggedIn = "n1 You are logged in " + player.getUserName() + "\n" + "n1 You are in lobby 1";
			outputStream.println(loggedIn);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return player;
	}

}