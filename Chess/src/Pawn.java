import java.util.ArrayList;

public class Pawn extends Piece{
	
	public Pawn(Team team) {
		super(team, 100);
	}
	
	boolean moved = false;

	@Override
	public ArrayList<int[]> getMoves(ChessBoard board, int i, int j) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int dir = team == Team.WHITE ? 1 : -1;
		
		if (j+dir < 8 && j + dir >= 0 && board.getPiece(i,j+dir) == null) {
			int[] move = {i, j+dir};
			moves.add(move);
		}
		if (!moved && board.getPiece(i, j+dir) == null && board.getPiece(i, j+dir*2) == null) {
			int[] move = {i,j+dir*2};
			moves.add(move);
		}
		if (i+1 < 8 && j+dir < 8 && j+dir >= 0)
			if (board.getPiece(i+1,j+dir) != null && board.getPiece(i+1,j+dir).team != this.team) {
				int[] move = {i+1,j+dir};
				moves.add(move);
			}
		if (i-1 >=0 && j+dir < 8 && j+dir >= 0)
			if (board.getPiece(i-1,j+dir) != null && board.getPiece(i-1,j+dir).team != this.team) {
				int[] move = {i-1,j+dir};
				moves.add(move);
			}
		
		
		return moves;
	}

	@Override
	public Piece copy() {
		return new Pawn(this.team);
	}

}
