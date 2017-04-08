package networking;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Multiplayer {
	
	public Multiplayer(User user) {
		/*JFrame f = new JFrame("Chess");
		
		Container list = new Container();
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		f.setSize(300,500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel UserList = new JPanel();
		UserList.setLayout(new BoxLayout(UserList, BoxLayout.Y_AXIS));
		JScrollPane pane = new JScrollPane(UserList);*/
		
		ArrayList<String> players = new ArrayList<String>();
		for(User u : User.ReadUsers("res\\User.csv")){
			players.add(u.Username);
		}
		JOptionPane.showInputDialog(null, "Select an Opponent to Challenge", user.Username, JOptionPane.QUESTION_MESSAGE, null, players.toArray(), null);
	}
	
}
