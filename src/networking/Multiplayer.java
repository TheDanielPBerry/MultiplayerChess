package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;

public class Multiplayer implements Runnable {
	
	private User user;
	private User opponent;
	
	public Multiplayer(User u) {
		user = u;
		
		Thread t = new Thread(this);
		t.start();
		
		String onlinePlayers = Server.SendMessage("GET_LIST||");
		String[] players = onlinePlayers.split("\\|\\|");
		String opponent = (String) JOptionPane.showInputDialog(null, "Select an Opponent to Challenge", user.Username, JOptionPane.QUESTION_MESSAGE, null, players, null);
		if(opponent==null) {
			logout();
		} else {
			challenge(opponent);
		}
	}
	
	
	
	public void logout() {
		
	}
	
	public void challenge(String opponentName) {
		String response[] = Server.SendMessage("GET_USER||" + opponentName + "||").split("\\|\\|");
		opponent = new User(response[0],Integer.parseInt(response[1]),response[2]);
		if(SendMessage("CHALLENGE||"+user.Username+"||"+opponentName+"||")!=null) {
			new Chess(user, opponent, true);
		}
	}
	
	
	public String SendMessage(String data) {
		if(opponent!=null) {
			try {
				DatagramSocket socket = new DatagramSocket();
		        while (true) {
		            InetAddress destAddress = opponent.Ip;
		            byte outBuffer[] = data.getBytes();
		            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, destAddress, opponent.Port);
		            socket.send(outPacket);
		            
		            
		            byte inBuffer[] = new byte[512];
		            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
		            socket.receive(inPacket);
		            String back = new String(inPacket.getData());
		            
		            socket.close();
		            return back;
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
	}
	
	
	public String parseCommand(String command, DatagramPacket packet) {
		String data[] = command.split("\\|\\|");
		switch(data[0]) {
		case "CHALLENGE":
			int n = JOptionPane.showConfirmDialog(
                    null, "Challenge", data[1] + " wants to challenge you.",
                    JOptionPane.YES_NO_OPTION);
			if(n==0) {
				opponent = new User(data[1], packet.getPort(), packet.getAddress().toString().replace("/",""));
				return "OK";
			}
		}
		return null;
	}
	
	
	@Override
	public void run() {
		listen();
	}
	public void listen() {
		try {
			DatagramSocket socket = new DatagramSocket(user.Port);
            byte[] inBuffer = new byte[512];
            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(inPacket);
            inBuffer = inPacket.getData();
            
            InetAddress returnAddress = inPacket.getAddress();
            String inputData = new String(inBuffer);
            String outputData = parseCommand(inputData, inPacket);
            
            byte[] outBuffer = outputData.getBytes();
            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, returnAddress, inPacket.getPort());
            socket.send(outPacket);
            socket.close();
            
            new Chess(user, opponent, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
