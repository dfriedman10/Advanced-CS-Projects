import java.lang.reflect.InvocationTargetException;

public class ChessBoard {
	
	final int MAXDEPTH = 3;

	Piece[][] board = new Piece[8][8];
	Team turn = Team.WHITE;
	
	public ChessBoard(ChessBoard orig) {
		turn = orig.turn;
		for (int i = 0; i < 8; i ++) { 
			for (int j = 0; j < 8; j++) {
				if (orig.getPiece(i, j) != null) 
					board[i][j] = orig.getPiece(i, j).copy();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ChessBoard() {
		Class[] col1 = {Rook.class,Knight.class,Bishop.class, King.class, Queen.class,Bishop.class,Knight.class,Rook.class},
				 col2 = {Rook.class,Knight.class,Bishop.class, Queen.class,King.class,Bishop.class,Knight.class,Rook.class};
		try {
			for (int i = 0; i < 8; i++) 
				for (int j = 0; j < 8; j++) {
					Piece p;
					if (j == 0) {
						p = (Piece)col1[i].getConstructor(Team.class).newInstance(new Object[] {Team.WHITE});
					}
					else if (j == 7) {
						p = (Piece)col2[i].getConstructor(Team.class).newInstance(new Object[] {Team.BLACK });
					}
					else if (j == 1 ) 
						p = new Pawn(Team.WHITE);
					
					else if (j == 6)
						p = new Pawn(Team.BLACK);
		
					else {
						p = null;
					}
					board[i][j] = p;
				}
		}catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public Piece getPiece(int i, int j) {
		return board[i][j];
	}
	
	public void setPiece(int i, int j, Piece p) {
		board[i][j] = p;
	}
	

	public String toString() {
		String output = "";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					output += "_ ";
				}
				else
					output += (board[i][j].getClass() == King.class ?
							"*" : board[i][j].getClass().toString().charAt(6)) + " ";
			}
			output += "\n";
		}
		return output;
	}
	
	public Team move(int i, int j, int newI, int newJ) {
		Team win = null;
		if (getPiece(i,j) != null) {
			if (getPiece(newI, newJ)!= null && getPiece(newI, newJ).getClass() == King.class) {
				win = getPiece(i,j).team;
			}
			else if (getPiece(i, j).getClass() == Pawn.class) {
				((Pawn)getPiece(i,j)).moved = true;
			}

			setPiece(newI,newJ, getPiece(i,j));
			setPiece(i,j,null);
		}
		
		turn = turn == Team.BLACK ? Team.WHITE : Team.BLACK;
		
		return win;
	}
	
	public int[] moveAI() {
		
		int[] move = DecisionMaker.bestMove(this, turn);
		Team winner = move(move[0],move[1],move[2],move[3]);
		if (winner != null) {
			return new int[] {move[0], move[1], move[2], move[3], winner == Team.WHITE ? 1 : 0};
		}
		return move;
	}
	
	public ChessBoard copy() {
		return new ChessBoard(this);
	}
	
	public boolean check() {
		for (int i = 0; i< 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getPiece(i,j) != null && getPiece(i, j).check(this, i, j)) {
			        return true;
				}
			}
		}
		return false;
	}
}
