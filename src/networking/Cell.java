package networking;

//package chess;

import java.awt.Point;

public class Cell {

	public char id;
	public final boolean whitePiece;
	public boolean firstMove = true;

	public Cell(char p) {
		boolean whiteStatus = true;
		id = p;
		if(id==0x265A || id==0x265B || id==0x265C || id==0x265D || id==0x265E || id==0x265F) {
			whiteStatus = false;
		}
		whitePiece = whiteStatus;
	}









	public boolean[][] possibleMoves(Point selectedCell, Cell[][] grid) {
		int x = selectedCell.x;
		int y = selectedCell.y;

		boolean[][] moves = {
			new boolean[8], 
			new boolean[8], 
			new boolean[8], 
			new boolean[8], 
			new boolean[8], 
			new boolean[8], 
			new boolean[8], 
			new boolean[8] 
		};

		switch(id) {
			case 0x265F:
			case 0x2659:
				byte whiteStatus = 1;
				if(whitePiece) {
					whiteStatus*=-1;
				}
				if(grid[x][y+whiteStatus].id == ' ') {
					moves[x][y+whiteStatus] = true;
					if(firstMove) {
						if(grid[x][y+whiteStatus*2].id == ' ') {
							moves[x][y+whiteStatus*2] = true;
						}
					}
				}
				break;
		}

		return moves;
	}
}