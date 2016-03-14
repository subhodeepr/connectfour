import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

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
			ClientAuthenticationHandler authHandler = new ClientAuthenticationHandler(bufferedReader,inputStream, outputStream, socket);
			authHandler.authenticate();
			Client client = new Client();
			line = bufferedReader.readLine();
			while (line != null)
			{
				if(line.equalsIgnoreCase("logout")){
					client.logout();
				}
				else if (line.equalsIgnoreCase("list")){
					client.list();
				}
				else{
					client.invalidCommand();

				}
				line = bufferedReader.readLine();


			}

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}

	}
	
	public void list(){
		try {
			line = "s1 list";
			outputStream.println(line);
			outputStream.flush();
			String userNames;
			String gameRooms = null; 
			userNames = inputStream.readLine();
			gameRooms = inputStream.readLine();
		    String[] userNameArray = userNames.split(",");
		    String[] gameRoomsArray = gameRooms.split(",");
		    Map<String, String> users = new HashMap <String, String>();
			for (int i = 0; i < userNameArray.length; i++){
				
				users.put(userNameArray[i], gameRoomsArray[i]);
			}
			for (Map.Entry<String, String> entry : users.entrySet()){
				
				System.out.println(entry.getKey() + " | " + entry.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client read error");
		}
		
		
	}
	
	public void logout(){
		
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
			if (response.equals("1")){
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

	public void authenticate() {

		try {
			String response = null;
			response = inputStream.readLine();
			System.out.println(response.substring(3, response.length()));
			response = inputStream.readLine();
			while (response.substring(0, 2).equals("n3")) {
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
