package networking;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Multiplayer {
	
	public static void main(String[] args) {
		new Multiplayer("Username");
	}
	
	public Multiplayer(String username) {
		/*JFrame f = new JFrame("Chess");
		
		Container list = new Container();
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		f.setSize(300,500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel UserList = new JPanel();
		UserList.setLayout(new BoxLayout(UserList, BoxLayout.Y_AXIS));
		JScrollPane pane = new JScrollPane(UserList);*/
		
		ArrayList players = new ArrayList();
		for(int i=0;i<500;i++){
			players.add("Opponent "+(i+1));
		}
		JOptionPane.showInputDialog(null, "Select Opponent", "Challenge", JOptionPane.QUESTION_MESSAGE, null, players.toArray(), null);
	}
	
}
