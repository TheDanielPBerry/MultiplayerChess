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
		
		byte whiteStatus = 1;
		if(whitePiece) {
			whiteStatus*=-1;
		}
		switch(id) {
		case 0x265F:
		case 0x2659:
			if(y>0 && y<7 && grid[x][y+whiteStatus].id == ' ') {
				moves[x][y+whiteStatus] = true;
				if(firstMove) {
					if(grid[x][y+whiteStatus*2].id == ' ') {
						moves[x][y+whiteStatus*2] = true;
					}
				}
			}
			if(x<7 && grid[x+1][y+whiteStatus].id != ' ' && grid[x+1][y+whiteStatus].whitePiece != whitePiece) {
				moves[x+1][y+whiteStatus] = true;
			}
			if(x>0 && grid[x-1][y+whiteStatus].id != ' ' && grid[x-1][y+whiteStatus].whitePiece != whitePiece) {
				moves[x-1][y+whiteStatus] = true;
			}
			break;
		case 0x265c:
		case 0x2654:
			byte d = (byte) x;
			while(d<=7) {
				if(grid[d][y].id == ' ') {
					moves[d][y] = true;
				}
				if(grid[d][y].id != ' ' && grid[d][y].whitePiece!=whitePiece) {
					moves[d][y] = true;
					d=8;
				}
				d++;
			}
			d = (byte) x;
			while(d>=0) {
				if(grid[d][y].id == ' ') {
					moves[d][y] = true;
				}
				if(grid[d][y].id != ' ' && grid[d][y].whitePiece!=whitePiece) {
					moves[d][y] = true;
					d=8;
				}
				d--;
			}
			d = (byte) y;
			while(d>=0) {
				if(grid[x][d].id == ' ') {
					moves[x][d] = true;
				}
				if(grid[x][d].id != ' ' && grid[x][d].whitePiece!=whitePiece) {
					moves[x][d] = true;
					d=8;
				}
				d--;
			}
			d = (byte) y;
			while(d<=7) {
				if(grid[x][d].id == ' ') {
					moves[x][d] = true;
				}
				if(grid[x][d].id != ' ' && grid[x][d].whitePiece!=whitePiece) {
					moves[x][d] = true;
					d=8;
				}
				d++;
			}
			break;
		}
		return moves;
	}
}