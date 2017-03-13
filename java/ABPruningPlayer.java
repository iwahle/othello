import java.util.*;

//import javax.swing.tree.OthelloTreeNode;



class ABPruningPlayer implements OthelloPlayer {
	
	private OthelloBoard board;
	private int dim = 8;
	private OthelloSide mySide;
	private List<OthelloTreeNode> leafList;
	private int TotalDepth = 4;
	
	/**
	 * Initialize the AI.
	 * @param side Set to either OthelloSide.BLACK or
	 * OthelloSide.WHITE.
	 **/
	public void init(OthelloSide side){
		board = new OthelloBoard();
		mySide = side;
		leafList = new ArrayList<OthelloTreeNode>();
		
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
		
		double alpha = -Double.MIN_VALUE;
		double beta = Double.MAX_VALUE;
		
		// Check for some basic pre-conditions
		if (opponentsMove != null) {board.move(opponentsMove, mySide.opposite());}
		if (board.isDone()){return null;}
		if (!(board.hasMoves(mySide))){return null;}
		
		// Create the tree's root node
		OthelloTreeNode root = new OthelloTreeNode(null, opponentsMove, mySide.opposite(), 0, board, 0);
		
		// Clear the leaflist
		leafList.clear();
		
		// Call minimax, which will create the entire tree with scores
		minimax(TotalDepth, root, mySide.opposite());
		
		// Find leaf node with largest score using alpha-beta pruning
		
		//System.out.println("Root node " + (root.getDepth()) + " with color: " + root.getSide() + " and score: " + root.getScore());
		
		OthelloTreeNode bestNode = getBestNode(root);
		
		OthelloTreeNode bestPrunedNode = getBestNodeByPruning(root, new ArrayList<OthelloTreeNode>(Arrays.asList(root)), Double.MIN_VALUE, Double.MAX_VALUE);
		
		// Traverse through the list of nodes by starting from root node and going to bottom of each branch
		
		board.move(bestNode.getMove(), mySide);
		
		System.out.println("Board's score: " + board.boardScore(mySide));

		return bestNode.getMove();
	}
	
	//arguments: number of tree levels left to complete, current node, side of current node, current board state 
	//current node is defined as the node that has just made a move, we are now looking to fill in it's children, i.e. moves to make after this move
	//generates tree of nodes starting at root from doMove.
	private void minimax(int depth, OthelloTreeNode currNode, OthelloSide currNodeSide){ 
		
		depth -= 1;
		
		// Iman's Exception Handling Code 
		
		if (currNode.getBoard().isDone() || !currNode.getBoard().hasMoves(currNodeSide.opposite())){
			OthelloTreeNode dummy;
			double score;
			if (currNodeSide == mySide) { score = currNode.getScore() + 15; } //if opponent can't move after my move
			else { score = currNode.getScore() - 15;}//if I can't move after my opponent's move
			dummy = new OthelloTreeNode(currNode, null, currNodeSide.opposite(), score, currNode.getBoard().copy(), TotalDepth - depth);
			currNode.addKid(dummy);
			if (depth > 0){ minimax(depth, dummy, currNodeSide.opposite());}
			else{leafList.add(dummy);}
		}
		else{
			// Have an empty Move object and iterate through all the valid moves on the board
			Move m;
			for (int x = 0; x < dim; x++){
				for (int y = 0; y < dim; y++){
					m = new Move(x, y);
					if (currNode.getBoard().checkMove(m, currNodeSide.opposite())){ //valid move
						
						// Take a new copy of the board and apply the heuristic to the current move
						OthelloBoard boardCpy = currNode.getBoard().copy();
						double heuristic = boardCpy.moveScore(m, currNodeSide.opposite()); //calculated before move is made on copied board
						boardCpy.move(m, currNodeSide.opposite());
						
						// Extend the node by adding a child node using the current move
						OthelloTreeNode kid = new OthelloTreeNode(currNode, m, currNodeSide.opposite(), heuristic + currNode.getScore(), boardCpy, TotalDepth - depth);
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
	
	private OthelloTreeNode getBestNode(OthelloTreeNode root) {
		
		OthelloTreeNode bestNode = leafList.get(0);
		double bestScore = bestNode.getScore();
		
		// Find node with largest score by looking through leafnode
		for (OthelloTreeNode node : leafList){ 
			if (node.getScore() > bestScore){
				bestScore = node.getScore();
				bestNode = node;
			}
		}
		
		// moving back up to the original move that led to this highest score
		
		while (bestNode.getParent() != root) { 
			bestNode = bestNode.getParent();
		}
		
		return bestNode;
	}
	
	private OthelloTreeNode getBestNodeByPruning(OthelloTreeNode root, List<OthelloTreeNode> nodes, double alpha, double beta) {
		
		List<OthelloTreeNode> nextNodes = new ArrayList<OthelloTreeNode>();
		
		OthelloTreeNode bestNode = leafList.get(0);
		double bestScore = bestNode.getScore();
		
		for (OthelloTreeNode node: nodes) {
			
			for (OthelloTreeNode subnode : node.getKids()) {
				//System.out.println("Subnode " + (subnode.getDepth()) + " with color: " + subnode.getSide() + " and score: " + subnode.getScore());
				nextNodes.add(subnode);
			}
		}
		
		// If we are at the end level of the nod
		if (nextNodes.size() == 0) {
			return leafList.get(0);
		}
		
		else {
			bestNode = getBestNodeByPruning(root, nextNodes, alpha, beta);
		}
		
		return leafList.get(0);
	}
}








