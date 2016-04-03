import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;



class UserInputThread implements Runnable {
	Socket socket = null;
	String line = null;
	BufferedReader bufferedReader = null;
	BufferedReader inputStream = null;
	PrintWriter outputStream = null;
	Client client;

	public UserInputThread(Socket s, BufferedReader br, BufferedReader is, PrintWriter os, Client cl) {
		socket = s;
		bufferedReader = br;
		inputStream = is;
		outputStream = os;
		client = cl;
	}

	public void run() {

		try {
			while (true) {
				if (bufferedReader.ready()) {
					line = bufferedReader.readLine();
					String[] commands = line.split("\\s+");
					if (line.length() > 3) {
						if (line.substring(0, 1).equalsIgnoreCase("y")) {
							client.publicChat(line);
						} else if (line.substring(0, 1).equalsIgnoreCase("p")) {
							client.privateChat(line);
						}
						else if (line.equalsIgnoreCase("logout")) {
							client.logout();
						} else if (line.equalsIgnoreCase("list")) {
							client.list();
						} else if (line.equalsIgnoreCase("leaderboard")) {
							client.leaderboard();
						} else if (line.substring(0, 3).equalsIgnoreCase("new") && commands.length == 2) {
							client.newGameroom(line);
						} else if (line.substring(0, 3).equalsIgnoreCase("new") && commands.length == 3) {
							client.newGameroomWithPlayer(line);
						} else if (line.substring(0, 3).equalsIgnoreCase("ban") && commands.length == 2) {
							client.banUser(line);
						} else if (line.contains("unban") && commands.length == 2) {
							client.unbanUser(line);
						} else if (line.contains("join") && commands.length == 2) {
							client.joinGame(line);
						} 

						else {
							client.invalidCommand();

						}
					} else {
						client.invalidCommand();

					}
				}

				if (inputStream.ready()) {
					line = inputStream.readLine();
					System.out.println(line);

				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

public class Client {

	static Socket socket = null;
	static String line = null;
	static BufferedReader bufferedReader = null;
	static BufferedReader inputStream = null;
	static PrintWriter outputStream = null;

	public static void main(String args[]) throws Exception {
		if (args.length != 1) {
			System.out.println("Usage: Client <Server IP>");
			System.exit(1);
		}

		String address = args[0];

		try {
			socket = new Socket(address, 9999);
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintWriter(socket.getOutputStream());
			ClientAuthenticationHandler authHandler = new ClientAuthenticationHandler(bufferedReader, inputStream,
					outputStream, socket);
			authHandler.authenticate();
			Client cl = new Client();
			// IncomingMessageThread icmThread = new
			// IncomingMessageThread(socket, bufferedReader, inputStream,
			// outputStream, cl);
			// icmThread.run();
			UserInputThread uiThread = new UserInputThread(socket, bufferedReader, inputStream, outputStream, cl);
			uiThread.run();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}
	}

	public void publicChat(String line2) {

		line = "c1 " + line2;
		outputStream.println(line);
		outputStream.flush();
	}
	
	public void privateChat(String line2) {

		line = "c2 " + line2;
		outputStream.println(line);
		outputStream.flush();
	}

	public void joinGame(String command) {
		try {
			line = "s2 " + command;
			outputStream.println(line);
			outputStream.flush();
			String response = inputStream.readLine();
			System.out.println(response);
			if (response.contains("No gameroom") || response.contains("is full")) {
				return;
			} else {
				gameLoop();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}

	}

	public void unbanUser(String command) {
		try {
			line = "s2 " + command;
			outputStream.println(line);
			outputStream.flush();
			String response = inputStream.readLine();
			System.out.println(response);

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}
	}

	public void banUser(String command) {
		try {
			line = "s2 " + command;
			outputStream.println(line);
			outputStream.flush();
			String response = inputStream.readLine();
			System.out.println(response);

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}
	}

	public void newGameroomWithPlayer(String command) {
		try {
			line = "s2 " + command;
			outputStream.println(line);
			outputStream.flush();
			String response = inputStream.readLine();
			if (response.contains("does not exist") || response.contains("is unavailable")) {
				System.out.println(response);
			} else {
				System.out.println(response);
				response = inputStream.readLine();
				System.out.println(response);
				response = inputStream.readLine();
				System.out.println(response);

				gameLoop();

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}
	}

	public void newGameroom(String command) {
		try {
			line = "s3 " + command;
			outputStream.println(line);
			outputStream.flush();
			String response;
			response = inputStream.readLine();
			System.out.println(response);
			if (!response.contains("already exists")) {

				response = inputStream.readLine();
				System.out.println(response);
				response = inputStream.readLine();
				System.out.println(response);
				gameLoop();
			}

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void gameLoop() {
		try {
			line = bufferedReader.readLine();
			while (line != null) {
				if (line.equals("quit")) {
					line = "s1 " + line;
					outputStream.println(line);
					outputStream.flush();
					String response = inputStream.readLine();
					System.out.println(response);
					line = bufferedReader.readLine();
					if (line.equals("yes")) {
						outputStream.println(line);
						outputStream.flush();
						System.out.println("You have quit the game");
						System.out.println("You are in lobby 1");
						return;
					}

				}
				line = bufferedReader.readLine();
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void leaderboard() {
		try {
			line = "s1 leaderboard";
			outputStream.println(line);
			outputStream.flush();
			String userNames;
			String gameRooms = null;
			userNames = inputStream.readLine();
			gameRooms = inputStream.readLine();
			String[] userNameArray = userNames.split(",");
			String[] scoresArray = gameRooms.split(",");
			Map<String, String> leaderboard = new HashMap<String, String>();
			for (int i = 0; i < userNameArray.length; i++) {

				leaderboard.put(userNameArray[i], scoresArray[i]);
			}
			for (Map.Entry<String, String> entry : leaderboard.entrySet()) {

				System.out.println("Player: " + entry.getKey() + " | Score: " + entry.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client read error");
		}

	}

	public void list() {
		line = "s1 list";
		outputStream.println(line);
		outputStream.flush();

	}

	public void logout() {

		try {
			line = "s1 logout";
			outputStream.println(line);
			outputStream.flush();

			String response = null;
			response = inputStream.readLine();

			if (response.substring(0, 2).equals("n3")) {
				System.out.print(response.substring(3, response.length()));
				line = bufferedReader.readLine();
				outputStream.println(line);
				outputStream.flush();
				response = inputStream.readLine();
			}
			if (response.equals("1")) {
				inputStream.close();
				outputStream.close();
				bufferedReader.close();
				socket.close();
				System.out.println("Logged out from server");
				System.exit(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client read error");
		}

	}

	public void invalidCommand() {
		System.out.println("Invalid command. Please enter a valid command");

	}

}

class ClientAuthenticationHandler {

	Socket socket;
	String line;
	BufferedReader inputStream;
	BufferedReader bufferedReader;
	PrintWriter outputStream;

	public ClientAuthenticationHandler(BufferedReader br, BufferedReader is, PrintWriter os, Socket s) {
		bufferedReader = br;
		inputStream = is;
		outputStream = os;
		socket = s;

	}

	void authenticate() {

		try {
			String response = null;
			response = inputStream.readLine();
			System.out.println(response.substring(3, response.length()));
			response = inputStream.readLine();
			while (response.substring(0, 2).equals("n3") || response.substring(0, 2).equals("n4")) {
				System.out.print(response.substring(3, response.length()));
				line = bufferedReader.readLine();
				outputStream.println(line);
				outputStream.flush();
				response = inputStream.readLine();
			}
			if (response.substring(0, 2).equals("n1")) {
				System.out.println(response.substring(3, response.length()));
				response = inputStream.readLine();
				System.out.println(response.substring(3, response.length()));
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client read error");
		}

	}

}
