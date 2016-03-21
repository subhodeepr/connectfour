import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Authentication {
    
    private String username;
    private String password;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private BufferedReader bufferedReader;
    private Socket socket;
    private String line = null;

    
    public Authentication(BufferedReader is, PrintWriter os, BufferedReader br, Socket s)
    {
	inputStream = is;
	outputStream = os;
	bufferedReader = br;
	socket = s;
	username = null;
	password = null;
    }
    
    public boolean authenticate(){
	String response = null;

	try {
	    String welcomeMessage = "Welcome to ConnectFour. Please login";
	    System.out.println(welcomeMessage);

	    line = bufferedReader.readLine();
	    while (!line.equalsIgnoreCase("logout")) {
		outputStream.println(line);
		outputStream.flush();
		response = inputStream.readLine();
		System.out.println(response);
		line = bufferedReader.readLine();

	    }

	} catch (IOException e) {
	    e.printStackTrace();
	    System.out.println("Server read error");
	} 
	return false;
    }
    

}
