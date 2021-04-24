import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Map {
	private int[] shape_lx;
	private int[] shape_ly;
	private int shape_n;
	private int width;
	private int n_block;
	private int correction;
	private HashMap<Integer, Node> graph = new HashMap<Integer, Node>();
	private HashMap<Integer, Node> moves = new HashMap<Integer, Node>();
	private List<Edge> all_visible = new ArrayList<>();
	private int start;
	private boolean can_paint = false;

	public Map(int n_corner, int width, int n_block) {
		shape_n = n_corner;
		this.width = width;
		this.n_block = n_block;
		correction = 500 - (n_block / 2 * width);
		generateShape();
		generateInside();
		generateRandomGraph();
		moves = generateMinSpanningTree();
		all_visible = makeVisibleEdges();
	}

	public void setPermission(boolean per) {
		can_paint = per;
	}
	
	public int getStart() {
		return start;
	}

	public HashMap<Integer, HashSet<Integer>> getAllMoves(){
		HashMap<Integer, HashSet<Integer>> all_moves = new HashMap<Integer, HashSet<Integer>>();
		for(Integer key: moves.keySet()) {
			HashSet<Integer> set = new HashSet<Integer>();
			all_moves.put(key, set);
		}
		for(Integer key: moves.keySet()) {
			for(Edge e: moves.get(key).getEdges()) {
				HashSet<Integer> set = all_moves.get(e.getA());
				set.add(e.getB());
				all_moves.put(e.getA(), set);
				set = all_moves.get(e.getB());
				set.add(e.getA());
				all_moves.put(e.getB(), set);
			}
		}
		return all_moves;
	}

	public HashSet<Integer> getPossibleMoves(int index) {
		HashSet<Integer> result = new HashSet<>();
		if (moves.get(index) != null) {
			for (Edge e : moves.get(index).getEdges()) {
				result.add(e.getA());
				result.add(e.getB());
			}
		}else {
			System.out.println("Index = " + index);
		}
		return result;
	}
	
	private List<Edge> makeVisibleEdges() {
		List<Edge> edges = new ArrayList<>();
		for (Integer N_i : moves.keySet()) {
			Node N = moves.get(N_i);
			boolean[] visible_edges = { true, true, true, true };
			for (Edge e : N.getEdges()) {
				int dif = e.getA() - e.getB();
				if(N_i!=e.getA()) {
					dif = e.getB() - e.getA();
				}
				if (dif == -1) {
					visible_edges[2] = false;
				} else if (dif == 1) {
					visible_edges[0] = false;
				} else if (dif == -n_block) {
					visible_edges[3] = false;
				} else if (dif == n_block){
					visible_edges[1] = false;
				}
			}
			int con_Ni = N_i+(N_i/n_block);
			if (visible_edges[0]) {
				edges.add(new Edge(con_Ni,con_Ni+n_block+1, 0));
			}
			if (visible_edges[1]) {
				edges.add(new Edge(con_Ni,con_Ni+1, 0));
			}
			if (visible_edges[2]) {
				edges.add(new Edge(con_Ni+1,con_Ni+2+n_block, 0));
			}
			if (visible_edges[3]) {
				edges.add(new Edge(con_Ni+n_block+1,con_Ni+2+n_block, 0));
			}

		}
		return edges;
	}

	private void generateShape() {
		shape_lx = new int[shape_n];
		shape_ly = new int[shape_n];
		int radius = n_block / 2 * width;
		double angle = Math.PI * 2 / shape_n;
		double sum_angle = Math.PI / 2;
		for (int i = 0; i < shape_n; i++) {
			double xn = Math.cos(sum_angle) * radius;
			double yn = Math.sin(sum_angle) * radius;
			shape_lx[i] = (int) Math.round(xn) + 500;
			shape_ly[i] = (int) Math.round(yn) + 500;
			sum_angle += angle;
		}
	}

	private void generateInside() {
		graph = new HashMap<>(); // Coordinates, Node index
		Random rnd = new Random();
		for (int i = 0; i < n_block; i++) {
			int topY = findTopY(i);
			int bottomY = findBottomY(i);
			if (topY >= 0 && bottomY >= 0) {
				graph.put(i * n_block + topY, new Node(i, topY, rnd.nextInt(100)));
				for (int j = topY + 1; j < bottomY; j++) {
					graph.put(i * n_block + j, new Node(i, j, rnd.nextInt(100)));
				}
				graph.put(i * n_block + bottomY, new Node(i, bottomY, rnd.nextInt(100)));
			}
		}
	}

	private void generateRandomGraph() {
		for (Integer current : graph.keySet()) {
			int rindx = (current / n_block + 1) * n_block + current % n_block;
			Node r = graph.get(rindx);
			int dindx = current / n_block * n_block + current % n_block + 1;
			Node d = graph.get(dindx);
			if (r != null && (current / n_block + 1) < n_block) {
				r.getEdges().add(new Edge(rindx, current, r.getValue()));
				graph.get(current).getEdges().add(new Edge(current, rindx, graph.get(current).getValue()));
			}
			if (d != null && current % n_block + 1 < n_block) {
				d.getEdges().add(new Edge(dindx, current, d.getValue()));
				graph.get(current).getEdges().add(new Edge(current, dindx, graph.get(current).getValue()));
			}
		}
	}

	private HashMap<Integer, Node> generateMinSpanningTree() {
		Random rnd = new Random();
		HashMap<Integer, Node> g = new HashMap<>();
		int skey = (int) graph.keySet().toArray()[rnd.nextInt(graph.size())]; // random key from graph
		start = skey;
		Node start = graph.get(skey);
		g.put(skey, start);
		PriorityQueue<Edge> q = new PriorityQueue<>();
		q.addAll(g.get(skey).getEdges());
		g.get(skey).getEdges().clear();
		while (!q.isEmpty()) {
			Edge k = q.poll();
			int A = k.getA();
			int B = k.getB();
			if (g.get(A) == null || g.get(B) == null) {
				Edge nE = new Edge(B, A, k.getLength());
				if (g.get(A) == null) {
					Node nN = new Node(A / n_block, A % n_block, 0);
					nN.addEdge(k);
					g.get(B).addEdge(nE);
					g.put(A, nN);
					q.addAll(graph.get(A).getEdges());
				} else if (g.get(B) == null){
					Node nN = new Node(B / n_block, B % n_block, 0);
					nN.addEdge(nE);
					g.get(A).addEdge(k);
					g.put(B, nN);
					q.addAll(graph.get(B).getEdges());
				}
			}
		}
		return g;
	}

	private int findTopY(int i) {
		Rectangle r = new Rectangle();
		int skok = width;
		r.setSize(skok, skok);
		for (int j = 0; j < n_block; j++) {
			r.setLocation(i * skok + correction, j * skok + correction);
			for (int k = 0; k < shape_n - 1; k++) {
				if (r.intersectsLine(shape_lx[k], shape_ly[k], shape_lx[k + 1], shape_ly[k + 1])) {
					return j;
				}
			}
			if (r.intersectsLine(shape_lx[0], shape_ly[0], shape_lx[shape_n - 1], shape_ly[shape_n - 1])) {
				return j;
			}
		}
		return -1;
	}

	private int findBottomY(int i) {
		Rectangle r = new Rectangle();
		int skok = width;
		r.setSize(skok, skok);
		for (int j = 39; j >= 0; j--) {
			r.setLocation(i * skok + correction, j * skok + correction);
			for (int k = 0; k < shape_n - 1; k++) {
				if (r.intersectsLine(shape_lx[k], shape_ly[k], shape_lx[k + 1], shape_ly[k + 1])) {
					return j;
				}
			}
			if (r.intersectsLine(shape_lx[0], shape_ly[0], shape_lx[shape_n - 1], shape_ly[shape_n - 1])) {
				return j;
			}
		}
		return -1;
	}

	public void draw(Graphics g) {
		if(can_paint) {
			Graphics2D gn = (Graphics2D) g;
			gn.setColor(Color.green);
			gn.drawPolygon(shape_lx, shape_ly, shape_n);
			for (Edge l : all_visible) {
				int x1 = l.getA() / (n_block+1) * width + correction;
				int x2 = l.getB() / (n_block+1) * width + correction;
				int y1 = l.getA() % (n_block+1) * width + correction;
				int y2 = l.getB() % (n_block+1) * width + correction;
				gn.setColor(Color.MAGENTA);
				gn.drawLine(x1, y1, x2, y2);
			}
		}
		
		/*
		for (Integer cur : graph.keySet()) {
			// System.out.println("size " + graph.get(cur).getEdges().size());
			for (Edge l : graph.get(cur).getEdges()) {
				int x1 = l.getA() / n_block * width + correction+10;
				int x2 = l.getB() / n_block * width + correction+10;
				int y1 = l.getA() % n_block * width + correction+10;
				int y2 = l.getB() % n_block * width + correction+10;
				gn.setColor(Color.GREEN);
				gn.drawLine(x1, y1, x2, y2);
				

			}
		}
		for (Integer cur : moves.keySet()) {
			// System.out.println("size " + graph.get(cur).getEdges().size());
			if (moves.get(cur).getEdges().size() == 1) {
				gn.setColor(Color.GREEN);
				// System.out.println("AAAAAAA " + (graph.get(cur).getX()*20+100) + " " +
				// (graph.get(cur).getY()*20+100));
				gn.drawOval(moves.get(cur).getX() * width + correction, moves.get(cur).getY() * width + correction, 5, 5);
			}
			for (Edge l : moves.get(cur).getEdges()) {
				int x1 = l.getA() / n_block * width + correction+10;
				int x2 = l.getB() / n_block * width + correction+10;
				int y1 = l.getA() % n_block * width + correction+10;
				int y2 = l.getB() % n_block * width + correction+10;
				gn.setColor(Color.red);
				gn.drawLine(x1, y1, x2, y2);
				gn.fillOval(x2,y2,3,3);
			}
		}
		
		//System.out.println(all_visible);
		*/
	}

}
