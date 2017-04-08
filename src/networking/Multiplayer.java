package networking;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Multiplayer {
	
	public static void main(String[] args) {
		new Multiplayer("Username");
	}
	
	public Multiplayer(String username) {
		JFrame f = new JFrame("Chess");
		
		Container list = new Container();
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		f.setSize(300,500);
		f.setVisible(true);
	}
	
}
