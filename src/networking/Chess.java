package networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Chess extends JPanel implements Runnable, MouseListener {
	
	
	
	private static final long serialVersionUID = -8316910605042033620L;
	private JFrame frame;
	private JFrame grave;
	private JPanel WGY;
	private JPanel BGY;
	private BufferedImage buffer;
	private Graphics g3;
	private final Dimension DIM;
	private byte refresh = 0;
	private final byte refreshRate = 40;
	private Cell board[][];
	private boolean availableMoves[][] = new boolean[8][8];
	private Point selectedCell = new Point(8,8);
	private boolean whitePeopleFirst = true;
	private boolean whitePrivilege;
	private User player1;
	private User player2;
	private ArrayList<Cell> whiteGY = new ArrayList<Cell>();
	private ArrayList<Cell> blackGY = new ArrayList<Cell>();
	
	public static void main(String[] args) {
		new Chess(new User("Jack",50003,"localhost"), new User("Ass",50002,"localhost"), true).frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Chess(new User("Ass",50002,"localhost"), new User("Jack",50003,"localhost"), false).frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Chess(User p1, User p2, boolean wp) {
		frame = new JFrame("Chess");
		DIM = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)(DIM.height/1.5),(int)(DIM.height/1.5));
		frame.add(this);
		addMouseListener(this);
		//frame.setUndecorated(true);
		//frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true);
		requestFocus();
		buffer = new BufferedImage((int)(DIM.height/1.5),(int)(DIM.height/1.5), BufferedImage.TYPE_INT_ARGB);
		g3 = buffer.getGraphics();
		player1 = p1;
		player2 = p2;
		whitePrivilege = wp;
		resetBoard();
		Graveyard();
		Thread t = new Thread(this);
		t.start();
		
		if(!wp) {
			new Thread(new Move(null)).start();
		}
	}
	
	public void Graveyard(){
		grave = new JFrame("Graveyards");
		grave.setSize(300,(int)(DIM.height/1.5));
		grave.setLocation((int) (frame.getX()+10+(DIM.height/1.5)),frame.getY());
		grave.setVisible(true);
		grave.setLayout(new GridLayout(2,1));
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
		JLabel WGYLabel = new JLabel(player1.Username + "'s Graveyard");
		WGYLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
		top.add(WGYLabel);
		WGYLabel.setAlignmentX(CENTER_ALIGNMENT);
		WGY = new JPanel(new GridLayout(4,4));
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.Y_AXIS));
		JLabel BGYLabel = new JLabel(player2.Username + "'s Graveyard");
		BGYLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
		bottom.add(BGYLabel);
		BGYLabel.setAlignmentX(CENTER_ALIGNMENT);
		BGY = new JPanel(new GridLayout(4,4));
		if(whitePrivilege) {
			top.add(WGY);
			bottom.add(BGY);
		} else {
			bottom.add(WGY);
			top.add(BGY);
		}
		grave.add(top);
		grave.add(bottom);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(buffer,0,0,null);
	}
	
	private void make() {
		g3.setFont(new Font(Font.MONOSPACED, Font.BOLD, getHeight()/14));
		g3.setColor(Color.ORANGE);
		g3.fillRect(0, 0, getWidth(), getHeight());
		for(byte y=0; y<8; y++) {
			for(byte x=0; x<8; x++) {
				if(availableMoves[x][y] || x==selectedCell.x && y==selectedCell.y) {
					g3.setColor(Color.CYAN);
					g3.fillRect(x*(getWidth()/8),y*(getWidth()/8),(getWidth()/8),(getWidth()/8));
					g3.setColor(Color.BLUE);
					g3.drawRect(x*(getWidth()/8),y*(getWidth()/8),(getWidth()/8),(getWidth()/8));
				} else if(x%2==0 && y%2==0 || x%2==1 && y%2==1) {
					g3.setColor(new Color(163,64,29));
					g3.fillRect(x*(getWidth()/8),y*(getWidth()/8),(getWidth()/8),(getWidth()/8));
				}
				if(board[x][y].whitePiece) g3.setColor(Color.WHITE);
				else g3.setColor(Color.BLACK);
				g3.drawString(""+board[x][y].id,(int)((x*(getWidth()/8))+(getWidth()/32)),(int)((y*(getHeight()/7.8))+(getHeight()/10)));
			}
		}
		repaint();
	}
	
	public void run() {
		while(frame.isVisible()) {
			if(refresh>refreshRate) {
				make();
				refresh=0;
			}refresh++;
			
			
			try {
				Thread.sleep(1);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void movePiece(byte x, byte y) {
		Cell temp = board[selectedCell.x][selectedCell.y];
		if((x==selectedCell.x && y==selectedCell.y) || !availableMoves[x][y]) {
			selectedCell = new Point(8,8);
		} else {
			temp.firstMove=false;
			if(temp.id!=' ') {
				if((temp.whitePiece!=board[x][y].whitePiece) || board[x][y].id==' ') {
					board[selectedCell.x][selectedCell.y] = new Cell(' ');
					if(board[x][y].id!=' '){
						if(board[x][y].whitePiece){
							JLabel tempLabel = new JLabel(""+board[x][y].id);
							tempLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
							WGY.add(tempLabel);
							whiteGY.add(board[x][y]);
						}
						else{
							JLabel tempLabel = new JLabel(""+board[x][y].id);
							tempLabel.setFont(new Font(getFont().getFontName(),Font.PLAIN,20));
							BGY.add(tempLabel);
							blackGY.add(board[x][y]);
						}
						grave.revalidate();
						grave.repaint();
					}
					board[x][y] = temp;
					if(whitePrivilege==whitePeopleFirst) {
						new Thread(new Move(selectedCell.x + "||" + selectedCell.y + "||" + x + "||" + y + "||")).start();
					}
					selectedCell = new Point(8,8);
				}
			}
		}
		availableMoves = new boolean[8][8];
	}
	
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==1 && whitePrivilege==whitePeopleFirst) {
			byte x = (byte) (((e.getX()+0.0)/frame.getWidth())*8), y = (byte) (((e.getY()+0.0)/frame.getWidth())*8);
			if(selectedCell.x==8) {
				if(board[x][y].whitePiece==whitePeopleFirst) {
					selectedCell = new Point(x,y);
					availableMoves = board[x][y].possibleMoves(selectedCell, board);
				}
			}
			else if(board[selectedCell.x][selectedCell.y].whitePiece==whitePeopleFirst) movePiece(x,y);
		}
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void resetBoard() {
		board = new Cell[8][8];
		final short pieces[][] = {
				{0x265c,0x265E,0x265D,0x265A,0x265B,0x265D,0x265E,0x265c},
				{0x265F,0x265F,0x265F,0x265F,0x265F,0x265F,0x265F,0x265F},
				{0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20},
				{0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20},
				{0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20},
				{0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20},
				{0x2659,0x2659,0x2659,0x2659,0x2659,0x2659,0x2659,0x2659},
				{0x2656,0x2658,0x2657,0x2654,0x2655,0x2657,0x2658,0x2656}
		};
		for(byte y=0; y<8; y++) {
			for(byte x=0; x<8; x++) {
				board[x][y] = new Cell((char) pieces[y][x]);
			}
		}
	}
	class Move implements Runnable {
		
		private String data; 
		
		public Move(String d) {
			data = d;
		}
		
		public void run() {
			if(data==null) {
				WaitMove();
			}else {
				MoveSend(data);
			}
		}
		
		public void MoveSend(String data) {
			try {
				DatagramSocket socket = new DatagramSocket();
	            InetAddress destAddress = player2.Ip;
	            byte outBuffer[] = data.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, destAddress, player2.Port);
	            socket.send(outPacket);
				whitePeopleFirst = !whitePeopleFirst;
				make();
				frame.revalidate();
				socket.close();
				WaitMove();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void WaitMove(DatagramSocket socket) {
			try {
	            byte[] inBuffer = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            inBuffer = inPacket.getData();
	            
	            String inputData[] = new String(inBuffer).split("\\|\\|");
	            selectedCell = new Point(Integer.parseInt(inputData[0]),Integer.parseInt(inputData[1]));
				availableMoves = board[selectedCell.x][selectedCell.y].possibleMoves(selectedCell, board);
	            movePiece(Byte.parseByte(inputData[2]), Byte.parseByte(inputData[3]));
	            
	            socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			whitePeopleFirst = !whitePeopleFirst;
			make();
			frame.revalidate();
		}
		
		
		public void WaitMove() {
			try {
				DatagramSocket socket = new DatagramSocket(player1.Port);
	            byte[] inBuffer = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            inBuffer = inPacket.getData();
	            
	            String inputData[] = new String(inBuffer).split("\\|\\|");
	            selectedCell = new Point(Integer.parseInt(inputData[0]),Integer.parseInt(inputData[1]));
				availableMoves = board[selectedCell.x][selectedCell.y].possibleMoves(selectedCell, board);
	            movePiece(Byte.parseByte(inputData[2]), Byte.parseByte(inputData[3]));
	            
	            socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			whitePeopleFirst = !whitePeopleFirst;
			make();
			frame.revalidate();
		}
	}
	
}

