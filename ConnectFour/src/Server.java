import java.io.*;
import java.net.*;

public class Server {

	public static void main(String args[]) {

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
				ServerThread st = new ServerThread(s);
				st.start();

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}

	}
}

class ServerAuthenticationHandler
{
	BufferedReader inputStream;
	PrintWriter outputStream;
	Socket socket;
	String line = null;

	public ServerAuthenticationHandler(BufferedReader is, PrintWriter os, Socket s)
	{
		inputStream = is;
		outputStream = os;
		socket = s;
		
	}
	
	public void authenticate(){
		
		try {
			String username = "n1 Connected to IP Address: " + socket.getLocalAddress() + " Port: " + socket.getPort() + "\n" + "n3 Enter username: ";
			outputStream.println(username);
			outputStream.flush();
			line = inputStream.readLine();
			while (line == null || line.isEmpty())
			{
				outputStream.println("n3 Please enter a valid username: ");
				outputStream.flush();
				line = inputStream.readLine();

			}
			String password = "n3 Enter password: ";
			outputStream.println(password);
			outputStream.flush();
			line = inputStream.readLine();
			while (line == null || line.isEmpty())
			{
				outputStream.println("n3 Please enter a valid password: ");
				outputStream.flush();
				line = inputStream.readLine();

			}
			String loggedIn = "n1 You are logged in." + "\n" + "n1 You are in lobby 1.";
			outputStream.println(loggedIn);
			outputStream.flush();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}



class ServerThread extends Thread {

	BufferedReader inputStream = null;
	PrintWriter outputStream = null;
	Socket s = null;

	public ServerThread(Socket s) {
		this.s = s;
	}

	
	public void run() {
		try {
			inputStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			outputStream = new PrintWriter(s.getOutputStream());

		} catch (IOException e) {
			System.out.println("IO error in server thread");
		}

		try {
			ServerAuthenticationHandler authHandler = new ServerAuthenticationHandler(inputStream, outputStream, s);
			authHandler.authenticate();

			
		} catch (NullPointerException e) {
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

			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		} // end finally
	}
}