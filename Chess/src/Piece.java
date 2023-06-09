import java.util.ArrayList;

public abstract class Piece {
	
	protected Team team;
	protected int value;
	
	public Piece(Team team, int value) {
		this.value = value;
		this.team = team;
	}
	
	public abstract Piece copy();
	
	public abstract ArrayList<int[]> getMoves(ChessBoard board, int i, int j);
	
	public Team getTeam() {
		return team;
	}
	
	public final boolean check(ChessBoard board, int i, int j) {
		ArrayList<int[]> possMoves = getMoves(board, i, j);
		
		for (int[] move : possMoves) {
			Piece p = board.getPiece(move[0], move[1]);
			if (p != null && p.team != this.team && p.getClass() == King.class) 
				return true;
		}
		return false;
	}
}
