import java.util.*;

class GoodPlayer implements OthelloPlayer{

	private int dim = 8;
	private OthelloSide mySide;
	private OthelloBoard board;
	//private OthelloDisplay display;

	/**
	 * Initialize the AI.
	 * @param side Set to either OthelloSide.BLACK or
	 * OthelloSide.WHITE.
	 **/

	public void init(OthelloSide side){

		board = new OthelloBoard();

		mySide = side;
	}

	/**
	 * Compute the next move given the opponent's last move.  Each AI is
	 * expected to keep track of the board on its own.  If this is the first
	 * move, or the opponent passed on the last move, then
	 * <code>opponentsMove</code> will be <code>null</code>.  If there are no
	 * valid moves for your side, {@link #doMove} must return <code>null</code>.
	 * <p>
	 * <strong>Important:</strong> doMove must take no longer than the
	 * timeout passed in <tt>millisRemaining</tt>, or your AI will lose!  The
	 * move returned must also be legal.
	 * <p>
	 * You will be disqualified if {@link #doMove} throws any exceptions.
	 *
	 * @param opponentsMove A {@link Move} object containing the opponent's
	 * move, or <code>null</code> if this is the first move or the opponent
	 * passed.
	 * @param millisLeft The number of milliseconds remaining for your side in
	 * the game.
	 *
	 * @return a {@link Move} object containing your move, or <code>null</code>
	 * if you have no valid moves.
	 **/
	public Move doMove(Move opponentsMove, long millisLeft)
	{
		// If it is the other player's turn, have them move
		if (opponentsMove != null) {board.move(opponentsMove, mySide.opposite());}
		// If there are no valid board spaces left, then return null
		if (board.isDone()){return null;}
		// If there are no valid moves for this player, then return null.
		if (!(board.hasMoves(mySide))){return null;}
		
		List<Move> validMoves = new ArrayList<Move>();
		
		boolean foundMove = false;
		Move m = null;

		// Go through all valid moves and add them to valid moves list
		for (int x = 0; x < dim; x++){
			for (int y = 0; y < dim; y++){
				m = new Move(x,y);
				foundMove = board.checkMove(m, mySide);
				if (foundMove){ 
					
					validMoves.add(m);
				}
			}
		}
		
		m = validMoves.get(0);
		double highestScore = board.moveScore(m, mySide);
		
		for (Iterator<Move> i = validMoves.iterator(); i.hasNext(); ) {
			Move current = i.next();
			
			if (board.moveScore(current, mySide) >= highestScore) {
				highestScore = board.moveScore(current, mySide);
				m = current;
			}
		}
		
		//System.out.println("Highest Score: " + highestScore);
		
		board.move(m, mySide);
		return m;
	}
}