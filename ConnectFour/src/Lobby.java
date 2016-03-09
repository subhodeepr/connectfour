import java.io.BufferedReader;
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
	
	public void lobbyInputLoop(){
		
		
		
	}
	
	public void spectateGame(String gameRoom){
		
	}
	public void challengePlayer(String username){
		
	}
	public void disconnect(){
		
	}
	public String[] returnPlayers(){
		return playerList;
	}
}
