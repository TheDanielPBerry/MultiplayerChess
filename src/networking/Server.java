package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;


public class Server implements Runnable {
	
	public static final int PORT = 9001;
	/**A collection of all the players online*/
	private static ArrayList<User> users = new ArrayList<User>();
	
	/**An instance of a connection between a client.*/
	private Socket socket;
	private BufferedReader input;
	private DataOutputStream output;
	
	public static void main(String[] args) {
        ServerSocket server;
		try {
			server = new ServerSocket(PORT);
			while(true) {
				Thread t = new Thread(new Server(server.accept()));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Server(Socket s) {
		socket = s;
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			String command = "";
			while(!command.contains("LOGOUT")) {
					command = input.readLine();
		            System.out.println("> " + command);
					String response = Server.ParseCommand(command, socket.getRemoteSocketAddress().toString(), socket.getPort());
		            System.out.println("< " + response);
					output.writeBytes(response);
			}
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String ParseCommand(String command, String address, int port) {
		String data[] = command.split("\\|\\|");
		switch(data[0]) {
		case "REGISTER":
			User user = new User(data[1],PasswordHash.Hash(data[2]), new Timestamp(System.currentTimeMillis()));
			if(User.GetUser(user.Username)==null) {
				User.AddUser(user);
				return "OK";
			}
			return "Username Already Exists";
		case "LOGIN":
			user = User.GetUser(data[1]);
			if(user!=null) {
				if(PasswordHash.Compare(user.PasswordHash, data[2])) {
					users.add(user);
					return "OK||" + port + "||";
				} else {
					return "Password Incorrect";
				}
			}
			return "Username Does Not Exist";
		case "GET_LIST":
			String onlinePlayers = "";
			for(User player : users) {
				if(!player.Username.equals(data[1])) {
					onlinePlayers += player.Username + "||";
				}
			}
			onlinePlayers += "Refresh||";
			return onlinePlayers;
		case "GET_USER":
			user = null;
			for(User player : users) {
				if(player.Username.equals(data[1])) {
					user = player;
				}
			}
			if(user!=null) {
				return user.Username + "||" + 
						user.socket.getPort() + "||" + 
						user.socket.getRemoteSocketAddress().toString() + "||";
			}
			return "No Valid User";
		case "LOGOUT":
			user = null;
			for(User player : users) {
				if(player.Username.equals(data[1])) {
					user = player;
				}
			}
			if(user!=null) {
				users.remove(user);
				return "OK";
			}
			return "Logout Failure";
		case "VERIFY":
			return "CONNECTION_PROCEED||";
		}
		return "Unknown Command Error";
	}
	
	public static String SendMessage(BufferedReader input, DataOutputStream output, String data) {
		try {
			output.writeBytes(data);
			String response = input.readLine();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static String bufferToString(byte[] buffer) {
		short i=0;
		String result = "";
		while(buffer[i]!=0) {
			result += (char)buffer[i];
			i++;
		}
		return new String(result);
	}
	
}
