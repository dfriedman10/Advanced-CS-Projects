import java.util.ArrayList;

public class Knight extends Piece {

	public Knight(Team team) {
		super(team, 325);
	}
	
	
	@Override
	public ArrayList<int[]> getMoves(ChessBoard board, int i, int j) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int[][] jumps = {{1,2},{1,-2},{-1,2},{-1,-2},{2,-1},{2,1},{-2,-1},{-2,1}};
		
		for (int[] jump : jumps) {
			if (i + jump[0] < 8 && i + jump[0] >= 0 && j + jump[1] < 8 && j + jump[1] >= 0) {
				Piece currSpace = board.getPiece(i+jump[0], j+jump[1]);
				if (currSpace ==null || currSpace.team != this.team) {
					moves.add(new int[] {i+jump[0], j+jump[1]});
				}

			}
		}
		return moves;
	}


	@Override
	public Piece copy() {
		return this;
	}
}
