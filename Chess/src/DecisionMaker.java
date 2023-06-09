
public class DecisionMaker {
	
	static final int MAXDEPTH = 3;

	public static int[] bestMove(ChessBoard board, Team aiTeam) {
		
		int[] bestMove = bestMove(board, 1, aiTeam);
		
		System.out.println(bestMove[0]);
		return new int[] {bestMove[1], bestMove[2], bestMove[3], bestMove[4]};
	}
	
	public static int[] bestMove(ChessBoard board, int depth, Team aiTeam) {
		boolean maxMode = depth%2==1;

		int[] bestMove = new int[] {maxMode? Integer.MIN_VALUE : Integer.MAX_VALUE};
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece p = board.getPiece(i, j);
				if (p != null && p.team == board.turn) {
					for (int[] possMove : p.getMoves(board, i, j)) {
						
						ChessBoard copy = board.copy();
						copy.move(i, j,possMove[0],possMove[1]);
						
						int score = (int)evaluate(copy, aiTeam);

						if (depth == MAXDEPTH) {

							if (maxMode && score >= bestMove[0])
								bestMove = new int[] {score, i, j, possMove[0],possMove[1]};
							else if (!maxMode && score <= bestMove[0])
								bestMove = new int[] {score, i, j, possMove[0],possMove[1]};
						}
						else {
							int[] possChoice = bestMove(copy, depth+1, aiTeam);
												
							if (maxMode && possChoice[0] >= bestMove[0]) {
								bestMove = new int[] {possChoice[0], i, j,possMove[0],possMove[1]};
							}
							else if (!maxMode && possChoice[0] <= bestMove[0]) {
								bestMove = new int[] {possChoice[0], i, j,possMove[0],possMove[1]};
							}
						}
					}
				}
			}
		}
		return bestMove;
	}
	
	public static double evaluate(ChessBoard board, Team turn) {
		double score = 0;
		boolean[] kings = {false, false};
		
		
		// sum the values of the pieces 
		for (int i = 0; i < 8; i ++)
			for (int j = 0; j < 8; j ++) {
				Piece piece = board.getPiece(i, j);
				if (piece== null) continue;
				
				Team team = piece.team;
				int val = piece.value;
				int teamScale = team == turn ? 1 : -1;
					// keep track of the kings 
				
				if (piece.getClass() == King.class)
					kings[team == turn ? 0 : 1] = true;
				
				// add values of the pieces
				else
					score += val*teamScale;
				
				// knights favor middle of board
				if (piece.getClass() == Knight.class) {
					double dist = Math.sqrt((3.5-i)*(3.5-i) + (3.5-j)*(3.5-j));
					score += (2-dist)*10;
				}
				
				// add value for pawn support
//				if (val == 1) {
//					if (team == Team.WHITE && i < 7 && j < 7 && board[i+1][j+1].getTeam() == team)
//						score += 5*scale;
//					if (team == 0 && i > 0 && j < 7 && board[i-1][j+1].getTeam() == team)
//						score += 5*scale;
//					if (team == 1 && i < 7 && j >0 && board[i+1][j-1].getTeam() == team)
//						score += 5*scale;
//					if (team == 1 && i > 0 && j >0 && board[i-1][j-1].getTeam() == team)
//						score += 5*scale;
//				}
//				
//				// add value for center control
//				if (i >= 2 && i <= 5 && j >= 2 && j <= 5)
//					score += 10*scale;
				
			}
		
		
		
		if (!kings[0])
			return Integer.MIN_VALUE;
		else if (!kings[1])
			return Integer.MAX_VALUE;
		return score;
	}
}
