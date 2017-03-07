import java.util.*;



class MinimaxPlayer implements OthelloPlayer {
	
	private OthelloBoard board;
	private int dim = 8;
	private OthelloSide mySide;
	private List<TreeNode> leafList;
	
	/**
	 * Initialize the AI.
	 * @param side Set to either OthelloSide.BLACK or
	 * OthelloSide.WHITE.
	 **/
	public void init(OthelloSide side){
		board = new OthelloBoard();
		mySide = side;
		leafList = new ArrayList<TreeNode>();
		
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
	public Move doMove(Move opponentsMove, long millisLeft){
		
		if (opponentsMove != null) {board.move(opponentsMove, mySide.opposite());}
		if (board.isDone()){return null;}
		if (!(board.hasMoves(mySide))){return null;}
		TreeNode root = new TreeNode(null, opponentsMove, mySide.opposite(), 0, board);
		leafList.clear();
		minimax(2, root, mySide.opposite());
		TreeNode bestNode = leafList.get(0);
		double bestScore = bestNode.getScore();
		for (TreeNode node : leafList){ //finds leaf node with largest score
			if (node.getScore() > bestScore){
				bestScore = node.getScore();
				bestNode = node;
			}
		}
		while (bestNode.getParent() != root) { // moving back up to the original move that led to this highest score
			bestNode = bestNode.getParent();
		}

		board.move(bestNode.getMove(), mySide);

		return bestNode.getMove();
		
	}
	
	//arguments: number of tree levels left to complete, current node, side of current node, current board state 
	//current node is defined as the node that has just made a move, we are now looking to fill in it's children, i.e. moves to make after this move
	//generates tree of nodes starting at root from doMove.
	private void minimax(int depth, TreeNode currNode, OthelloSide currNodeSide){ 
		depth -= 1;
		if (currNode.getBoard().isDone() || !currNode.getBoard().hasMoves(currNodeSide.opposite())){
			TreeNode dummy;
			double score;
			if (currNodeSide == mySide) { score = currNode.getScore() + 15; } //if opponent can't move after my move
			else { score = currNode.getScore() - 15;}//if I can't move after my opponent's move
			dummy = new TreeNode(currNode, null, currNodeSide.opposite(), score, currNode.getBoard().copy());
			currNode.addKid(dummy);
			if (depth > 0){ minimax(depth, dummy, currNodeSide.opposite());}
			else{leafList.add(dummy);}
		}
		else{
			Move m;
			for (int x = 0; x < dim; x++){
				for (int y = 0; y < dim; y++){
					m = new Move(x, y);
					if (currNode.getBoard().checkMove(m, currNodeSide.opposite())){ //valid move
						OthelloBoard boardCpy = currNode.getBoard().copy();
						double heuristic = boardCpy.moveScore(m, currNodeSide.opposite()); //calculated before move is made on copied board
						boardCpy.move(m, currNodeSide.opposite());
						TreeNode kid = new TreeNode(currNode, m, currNodeSide.opposite(), heuristic + currNode.getScore(), boardCpy);
						currNode.addKid(kid); //have added valid move as child of currNode
						if (depth > 0){ //if more layers to fill in:
							minimax(depth, kid, currNodeSide.opposite()); //recursive step
						}
						else{ //must be a leaf of our tree, add it to leafList for easy access later
							leafList.add(kid);
						}
					}
				}
			}
		}
	}
}








