import java.util.*; 

class OthelloTreeNode {
	private OthelloTreeNode parent;
	private List<OthelloTreeNode> kids;
	private Move move;
	private OthelloSide side;
	private double score;
	private OthelloBoard board;
	private int depth;
	
	//arguments: parent node, move made from parent to make this node, side that made said move, cumulative heuristic score
	public OthelloTreeNode(OthelloTreeNode p, Move m, OthelloSide si, double s, OthelloBoard b, int d){
		parent = p;
		move = m;
		side = si;
		score = s;
		board = b;
		kids = new ArrayList<OthelloTreeNode>();
		depth = d;
	}
	public void addKid(OthelloTreeNode kid){
		kids.add(kid);
	}
	
	public OthelloTreeNode getParent(){ return parent;}
	
	public Move getMove(){return move;}
	
	public double getScore(){return score;}
	
	public OthelloBoard getBoard(){return board;}
	
	public void setScore(double newScore){
		score = newScore;
	}
	
	public OthelloSide getSide() {return side;}
	
	public List<OthelloTreeNode> getKids() {return kids;}
	
	public int getDepth() {return depth;}
		
}