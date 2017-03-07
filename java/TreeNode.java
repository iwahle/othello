
import java.util.*; 

class TreeNode {
	private TreeNode parent;
	private List<TreeNode> kids;
	private Move move;
	private OthelloSide side;
	private double score;
	private OthelloBoard board;
	
	//arguments: parent node, move made from parent to make this node, side that made said move, cumulative heuristic score
	public TreeNode(TreeNode p, Move m, OthelloSide si, double s, OthelloBoard b){
		parent = p;
		move = m;
		side = si;
		score = s;
		board = b;
		kids = new ArrayList<TreeNode>();
	}
	public void addKid(TreeNode kid){
		kids.add(kid);
	}
	
	public TreeNode getParent(){ return parent;}
	
	public Move getMove(){return move;}
	
	public double getScore(){return score;}
	
	public OthelloBoard getBoard(){return board;}
	
	public void incScore(int increment){ //increments score by increment
		score += increment;
	}
}