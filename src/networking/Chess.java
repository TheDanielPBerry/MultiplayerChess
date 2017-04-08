package networking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Chess extends JPanel implements Runnable, MouseListener {
	
	
	
	private static final long serialVersionUID = -8316910605042033620L;
	private JFrame frame;
	private BufferedImage buffer;
	private Graphics g3;
	private final Dimension DIM;
	private byte refresh = 0;
	private final byte refreshRate = 40;
	private Cell board[][];
	private boolean availableMoves[][] = new boolean[8][8];
	private Point selectedCell = new Point(8,8);
	private boolean whitePeopleFirst = true;
	
	public static void main(String[] args) {
		new Chess(args).frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Chess(String[] args) {
		frame = new JFrame("Chess");
		DIM = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)(DIM.height/1.1),(int)(DIM.height/1.1)+10);
		frame.add(this);
		addMouseListener(this);
		//frame.setUndecorated(true);
		//frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true);
		requestFocus();
		buffer = new BufferedImage((int)(DIM.height/1.1),(int)(DIM.height/1.1), BufferedImage.TYPE_INT_ARGB);
		g3 = buffer.getGraphics();
		resetBoard();
		Thread t = new Thread(this);
		t.start();
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
				}else if(x%2==0 && y%2==0 || x%2==1 && y%2==1) {
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
					board[x][y] = temp;
					selectedCell = new Point(8,8);
				}
			}
		}
		availableMoves = new boolean[8][8];
	}
	
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==1) {
			byte x = (byte) (((e.getX()+0.0)/frame.getWidth())*8), y = (byte) (((e.getY()+0.0)/frame.getWidth())*8);
			if(selectedCell.x==8 && board[x][y].whitePiece==whitePeopleFirst) {
				selectedCell = new Point(x,y);
				availableMoves = board[x][y].possibleMoves(selectedCell, board);
			}
			else movePiece(x,y);
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
	
}

