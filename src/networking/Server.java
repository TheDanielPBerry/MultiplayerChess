package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;


public class Server {
	
	public static final int PORT = 9001;
	/**A collection of all the players online*/
	private static ArrayList<User> users = new ArrayList<User>();
	
	public static void main(String[] args) {
		try {
	        while (true) {
				DatagramSocket socket = new DatagramSocket(PORT);
	            byte[] inBuffer = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            inBuffer = inPacket.getData();
	            
	            InetAddress returnAddress = inPacket.getAddress();
	            String inputData = Server.bufferToString(inBuffer);
	            System.out.println("> " + inputData);
	            String outputData = parseCommand(inputData, inPacket);
	            System.out.println("< " + outputData);
	            
	            byte[] outBuffer = outputData.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, returnAddress, inPacket.getPort());
	            socket.send(outPacket);
	            socket.close();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static String parseCommand(String command, DatagramPacket packet) {
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
					user.Port = packet.getPort();
					user.Ip = packet.getAddress();
					users.add(user);
					return "OK||" + user.Port + "||";
				} else {
					return "Password Incorrect";
				}
			}
			return "Username Does Not Exist";
		case "GET_LIST":
			String onlinePlayers = "";
			for(User player : users) {
				onlinePlayers += player.Username + "||";
			}
			return onlinePlayers;
		case "GET_USER":
			user = null;
			for(User player : users) {
				if(player.Username.equals(data[1])) {
					user = player;
				}
			}
			if(user!=null) {
				return user.Username + "||" + user.Port + "||" + user.Ip.toString().replace("/","") + "||";
			}
			return "No Valid User";
		case "LOGOUT":
			
			return "DOPE";
		}
		return "Unknown Command Error";
	}
	
	
	public static String SendMessage(String data) {
		try {
			DatagramSocket socket = new DatagramSocket();
	        while (true) {
	            InetAddress destAddress = InetAddress.getByName(Account.Ip);
	            byte outBuffer[] = data.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, destAddress, PORT);
	            socket.send(outPacket);
	            
	            
	            byte inBuffer[] = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            String back = bufferToString(inPacket.getData());
	            
	            socket.close();
	            return back;
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private static String bufferToString(byte[] buffer) {
		short i=0;
		String result = "";
		while(buffer[i]!=0) {
			result += (char)buffer[i];
			i++;
		}
		return new String(result);
	}
	
}
