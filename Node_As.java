import java.util.HashSet;

public class Node_As implements Comparable<Node_As> {

	int index;
	boolean been_v = false;
	double dist_glob = Integer.MAX_VALUE;
	double dist_loc = 0;
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
        return "index: " + this.index + ", dist_glob: " + this.dist_glob + ", dist_loc: " + dist_loc + ", neigh_size: " + neighb.size();
    }
    
    public Node_As clone(){  
    	Node_As new_node = new Node_As();
    	new_node.index=this.index;
    	new_node.neighb=this.neighb;
    	new_node.x=this.x;
    	new_node.y=this.y;
        return new_node;  
     }
}
