import java.io.*;
import java.net.*;

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
			Lobby lobby = new Lobby(bufferedReader,inputStream, outputStream, socket);
			line = bufferedReader.readLine();
			while (line != null)
			{
				if(line.equalsIgnoreCase("logout")){
					lobby.logout();
				}
				else{
					lobby.invalidCommand();

				}
				line = bufferedReader.readLine();


			}

		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IO Exception");
		}

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
