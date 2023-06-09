import java.util.ArrayList;

public class Bishop extends Piece{
	
	public Bishop(Team team) {
		super(team, 340);
	}
	
	@Override
	public Piece copy() {
		return this;
	}

	@Override
	public ArrayList<int[]> getMoves(ChessBoard board, int i, int j) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int[][] multipliers = {{1,1},{-1,1},{-1,-1},{1,-1}};
		
		for (int[] mult : multipliers)
			for (int dist = 1; dist < 8; dist ++) {
				if (i + dist*mult[0] > 7 || i + dist*mult[0] < 0 || j + dist*mult[1] < 0 || j + dist*mult[1] > 7)
					break;
				Piece currSpace = board.getPiece(i+dist*mult[0], j+dist*mult[1]);
				if (currSpace !=null && currSpace.team == this.team)
					break;
				moves.add(new int[] {i+dist*mult[0], j+dist*mult[1]});
				if (currSpace != null && currSpace.team != this.team)
					break;
			}
		return moves;
	}

}
