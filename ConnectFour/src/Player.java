import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private String username;
	private int wins;
	private int losses;
	private Boolean gameStatus;
	public List <Player> banList;
	private Boolean connected;
	private char token;
	private Socket socket;
	
	public Player(String name, Socket s){
		username = name; 	
		socket = s; 
		banList = new ArrayList<Player> ();
	}
	public Socket getSocket(){
		
		return socket; 
	}
	public void setSocket(Socket s){
		
		socket = s; 
	}
	public String getUserName(){
		return username;
	}
	public int getLoses(){
		return losses;
	}
	public int getWins(){
		return wins;
	}
	public Boolean getStatus(){
		return gameStatus;
	}
	public Boolean acceptChallenge(){
		return true;
	}
	
	public void setPlayerToken(char player_token){
		token = player_token;
	}
	public char returnPlayerToken(){
		return token;
	}
	
}
