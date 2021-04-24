import java.util.ArrayList;
import java.util.List;

public class Node {
	private int x;
	private int y;
	private List<Edge> edge = new ArrayList<>();
	private int value;
	
	public Node(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public void addEdge(Edge e) {
		edge.add(e);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getValue() {
		return value;
	}
	
	public List<Edge> getEdges() {
		return edge;
	}
	
}
