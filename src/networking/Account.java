package networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Account extends JFrame{
		public static void main(String args[]){
			JFrame frame = new Account();
		}
		
		public Account(){
			setVisible(true);
			setTitle("Account login");
			setResizable(false);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			
			JTabbedPane pane = new JTabbedPane();
			
			Container UserPass = new Container();
			SpringLayout layout = new SpringLayout();
			UserPass.setLayout(layout);
			
			JLabel UserLabel = new JLabel("Username: ");
			UserLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
			UserPass.add(UserLabel);
			layout.putConstraint(SpringLayout.WEST, UserLabel, 60, SpringLayout.WEST, UserPass);
			layout.putConstraint(SpringLayout.NORTH, UserLabel, 35, SpringLayout.NORTH, UserPass);
			JTextField Username = new JTextField(15);
			UserPass.add(Username);
			layout.putConstraint(SpringLayout.WEST, Username, 10, SpringLayout.EAST, UserLabel);
			layout.putConstraint(SpringLayout.NORTH, Username, 40, SpringLayout.NORTH, UserPass);
			JLabel PWLabel = new JLabel("Password: ");
			PWLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
			UserPass.add(PWLabel);
			layout.putConstraint(SpringLayout.WEST, PWLabel, 60, SpringLayout.WEST, UserPass);
			layout.putConstraint(SpringLayout.NORTH, PWLabel, 55, SpringLayout.NORTH, UserLabel);
			JPasswordField Password = new JPasswordField(15);
			UserPass.add(Password);
			layout.putConstraint(SpringLayout.WEST, Password, 10, SpringLayout.EAST, PWLabel);
			layout.putConstraint(SpringLayout.NORTH, Password, 55, SpringLayout.NORTH, Username);
			JButton SignIn = new JButton("Sign In");
			UserPass.add(SignIn);
			layout.putConstraint(SpringLayout.WEST, SignIn, 150, SpringLayout.WEST, UserPass);
			layout.putConstraint(SpringLayout.NORTH, SignIn, 45, SpringLayout.NORTH, Password);
			pane.add("Login",UserPass);
			
			Container Register = new Container();
			Register.setLayout(layout);
			
			JLabel UserLabel2 = new JLabel("Username: ");
			UserLabel2.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
			Register.add(UserLabel2);
			layout.putConstraint(SpringLayout.WEST, UserLabel2, 60, SpringLayout.WEST, Register);
			layout.putConstraint(SpringLayout.NORTH, UserLabel2, 35, SpringLayout.NORTH, Register);
			JTextField Username2 = new JTextField(15);
			Register.add(Username2);
			layout.putConstraint(SpringLayout.WEST, Username2, 10, SpringLayout.EAST, UserLabel2);
			layout.putConstraint(SpringLayout.NORTH, Username2, 40, SpringLayout.NORTH, Register);
			JLabel PWLabel2 = new JLabel("Password: ");
			PWLabel2.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
			Register.add(PWLabel2);
			layout.putConstraint(SpringLayout.WEST, PWLabel2, 60, SpringLayout.WEST, Register);
			layout.putConstraint(SpringLayout.NORTH, PWLabel2, 55, SpringLayout.NORTH, UserLabel2);
			JPasswordField Password2 = new JPasswordField(15);
			Register.add(Password2);
			layout.putConstraint(SpringLayout.WEST, Password2, 10, SpringLayout.EAST, PWLabel2);
			layout.putConstraint(SpringLayout.NORTH, Password2, 55, SpringLayout.NORTH, Username2);
			JButton SignUp = new JButton("Register");
			Register.add(SignUp);
			layout.putConstraint(SpringLayout.WEST, SignUp, 150, SpringLayout.WEST, Register);
			layout.putConstraint(SpringLayout.NORTH, SignUp, 45, SpringLayout.NORTH, Password2);
			pane.add("Register",Register);
			
			
			add(pane,BorderLayout.CENTER);
			setSize(400,250);
		}
}
