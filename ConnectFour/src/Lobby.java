import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Lobby {
	int maxCapactity;
	int connectedUsers;
	int gamesInProgress;
	private String playerList[];
	String ranking[];
	
	Socket socket;
	String line;
	BufferedReader inputStream;
	BufferedReader bufferedReader;
	PrintWriter outputStream;
	
	
	public Lobby(BufferedReader br, BufferedReader is, PrintWriter os, Socket s)
	{
		bufferedReader = br;
		inputStream = is;
		outputStream = os;
		socket = s;
		
	}
	

	public void spectateGame(String gameRoom){
		
	}
	public void challengePlayer(String username){
		
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
	public String[] returnPlayers(){
		return playerList;
	}


	public void invalidCommand() {
		System.out.println("Invalid command. Please enter a valid command");

	}
}
