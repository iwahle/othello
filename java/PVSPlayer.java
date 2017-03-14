
class PVSPlayer implements OthelloPlayer {

	private OthelloBoard board;
	private int dim = 8;
	private OthelloSide mySide;
	private int DEPTH = 3;


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
	public Move doMove(Move opponentsMove, long millisLeft){
		//register opponents move on our board
		if (opponentsMove != null) {board.move(opponentsMove, mySide.opposite());}
		//check if games over or can't move
		if (board.isDone()){return null;}
		if (!(board.hasMoves(mySide))){return null;}
		//make node with opps move
		OthelloTreeNode root = new OthelloTreeNode(null, opponentsMove, mySide.opposite(), board.boardScore(mySide.opposite()), board, 0); //left depth as 0 for now
		//create tree of given depth with opps move node as root
		generateTree(DEPTH, root);
		//traverse tree in post order updating scores
		double theChosenOne = pvs(root, DEPTH -1, Double.MIN_VALUE, Double.MAX_VALUE, mySide);
		//find child from root with best score to return move of
		Move toRet = null;
		for (OthelloTreeNode kid : root.getKids()){
			System.out.println(kid.getScore());
			if (kid.getScore() == theChosenOne){toRet = kid.getMove();}
		}
		//register chosen move on our board
		board.move(toRet, mySide);
		return toRet; //go through the roots children and find the one with the optimal score to return
	}
	
	
	public double pvs(OthelloTreeNode node, int depth, double alpha, double beta, OthelloSide side){
		if (node.getDepth() == 0 || node.getKids().size() == 0){
			return node.getScore();
		}
		for (OthelloTreeNode kid : node.getKids()){
			double score;
			if (kid != node.getKids().get(0)){
				score = -1 * pvs(kid, depth -1, -1 * alpha - 1, -1 * alpha, side.opposite());
				if (alpha < score && score < beta){
					score = -1 * pvs(kid, depth -1, -1 * beta, -1 * score, side.opposite());
				}
			}
			else{
				score = -1 * pvs(kid, depth-1, -1 * beta, -1 * alpha, side.opposite());
			}
			alpha = java.lang.Math.max(alpha, score);
			if (alpha >= beta){break;}
		}
		return alpha;
	}


	

	//arguments: number of tree levels left to complete, current node
	//current node is defined as the node that has just made a move, we are now looking to fill in it's children, i.e. moves to make after this move
	//generates tree of nodes starting at root from doMove.
	/**
	    * Returns nothing.
	    * Recursively generates tree of given depth of possible moves from the node given.
	    * @param int depth: how many levels of tree to create
	    * @param OthelloTreeNode currNode: node to populate children of
	    **/
	private void generateTree(int depth, OthelloTreeNode currNode) { 
		depth -= 1; //decrement to keep track of how many levels left to do
		//checking if game is over or if there are no moves to make
		if (currNode.getBoard().isDone() || !currNode.getBoard().hasMoves(currNode.getSide().opposite())){
			//if this is the case, we make a placeholder node with a null move value.
			//if
			OthelloTreeNode dummy;
			double score;
	//		if (currNode.getSide() == mySide) { score = currNode.getScore() + 15; } //if opponent can't move after my move
	//		else { score = currNode.getScore() - 15;}//if I can't move after my opponent's move
			dummy = new OthelloTreeNode(currNode, null, currNode.getSide().opposite(), currNode.getScore(), currNode.getBoard().copy(), depth);
			currNode.addKid(dummy);
			if (depth > 0){ generateTree(depth, dummy);} //recursive step
		}
		
		else{ 
			Move m;
			//loop through every position in board
			for (int x = 0; x < dim; x++){
				for (int y = 0; y < dim; y++){
					m = new Move(x, y);
					if (currNode.getBoard().checkMove(m, currNode.getSide().opposite())){ //valid move
						//if valid move found, make copy of board and make the move on it
						OthelloBoard boardCpy = currNode.getBoard().copy();
						boardCpy.move(m, currNode.getSide().opposite());
						double heuristic = boardCpy.boardScore(currNode.getSide().opposite()); //heuristic if this move is made
						//add node with this new move/board/heuristic as a child of currNode
						OthelloTreeNode kid = new OthelloTreeNode(currNode, m, currNode.getSide().opposite(), heuristic, boardCpy, depth); //leaving depth as 0 for now
						currNode.addKid(kid);
						if (depth > 0){ //if more layers to fill in:
							generateTree(depth, kid); //recursive step
						}
					}
				}
			}
		}
	}
}








