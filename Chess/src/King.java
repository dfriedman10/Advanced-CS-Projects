import java.util.ArrayList;

public class King extends Piece{
	
	public King(Team team) {
		super(team, -1);
	}

	@Override
	public ArrayList<int[]> getMoves(ChessBoard board, int i, int j) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int[][] directions = {{1,0},{0,1},{-1,0},{0,-1},{1,1},{-1,1},{-1,-1},{1,-1}};
		
		for (int[] dir : directions) {
			if (i + dir[0] < 8 && i + dir[0] >= 0 && j + dir[1] < 8 && j + dir[1] >= 0) {
				Piece currSpace = board.getPiece(i+dir[0], j+dir[1]);
				if (currSpace ==null || currSpace.team != this.team)
					moves.add(new int[] {i+dir[0], j+dir[1]});

			}
		}
		return moves;
	}

	@Override
	public Piece copy() {
		return this;
	}

}
