import java.util.HashSet;

public class Node_As implements Comparable<Node_As> {

	int index;
	boolean been_v = false;
	double dist_glob = Integer.MAX_VALUE;
	double dist_loc = Integer.MAX_VALUE;
	int x;
	int y;
	HashSet<Node_As> neighb = new HashSet<>();
	Node_As parent;

	@Override
	public int compareTo(Node_As o) {
		return o.dist_glob >= this.dist_glob ? 1 : -1;
	}

	@Override
	public String toString() {
		return "index: " + this.index + ", dist_glob: " + this.dist_glob + ", dist_loc: " + dist_loc + ", neigh_size: "
				+ neighb.size();
	}

	public void resetValues() {
		been_v = false;
		dist_glob = Integer.MAX_VALUE;
		dist_loc = Integer.MAX_VALUE;
		parent = null;
	}

	public Node_As copyInherit() {
		Node_As node = new Node_As();
		node.index = this.index;
		node.x = this.x;
		node.y = this.y;
		node.parent = this.parent;
		return node;
	}
}
