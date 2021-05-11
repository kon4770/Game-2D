import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Enemy implements Runnable {

	// https://github.com/OneLoneCoder/videos/blob/master/OneLoneCoder_PathFinding_AStar.cpp
	private Thread t;
	private Node_As me;
	private Node_As target;
	private Color color;
	private boolean can_paint = false;
	private boolean can_explode = false;
	private double explosion_diam = 0;
	private HashMap<Integer, Node_As> init;
	private int[] path_x;
	private int[] path_y;
	private int b_width;
	private int correction;

	public Enemy(HashMap<Integer, HashSet<Integer>> map, int width, int n_block, int target_id) {

		b_width = width;
		correction = 500 - (n_block / 2 * width);
		
		Random rand = new Random();
		color = new Color(rand.nextInt(180),rand.nextInt(180),rand.nextInt(255));
		color = color.brighter();

		init = new HashMap<>();
		for (Integer key : map.keySet()) {
			Node_As curr = new Node_As();
			curr.index = key;
			curr.x = key / n_block * width + correction + width / 2;
			curr.y = key % n_block * width + correction + width / 2;
			init.put(key, curr);
		}
		for (Integer key : init.keySet()) {
			for (Integer n_key : map.get(key)) {
				Node_As curr = init.get(key);
				curr.neighb.add(init.get(n_key));
			}
		}
		target = init.get(target_id);

		Random rnd = new Random();
		int start = (int) map.keySet().toArray()[rnd.nextInt(map.size())];
		while (start == target.index) {
			start = (int) map.keySet().toArray()[rnd.nextInt(map.size())];
		}

		me = init.get(start);
	}

	public void run() {
		Node_As curr = me;
		curr.dist_loc = 0;
		curr.dist_glob = lengthManh(me, target);
		Queue<Node_As> not_tested = new PriorityQueue<>();
		not_tested.add(curr);
		while (!not_tested.isEmpty() && curr.index != target.index) {
			while (!not_tested.isEmpty() && not_tested.peek().been_v) {
				not_tested.remove();
			}
			if (not_tested.isEmpty()) {
				break;

			}
			curr = not_tested.poll();
			curr.been_v = true;
			for (Node_As neighbour : curr.neighb) {
				if (!neighbour.been_v) {
					not_tested.add(neighbour);
				}
				double poss_lower_goal = curr.dist_loc + length(curr, target);
				if (poss_lower_goal < neighbour.dist_loc) {
					// lastIndx = neighbour.index;
					neighbour.parent = curr;
					neighbour.dist_loc = poss_lower_goal;
					neighbour.dist_glob = neighbour.dist_loc + lengthManh(neighbour, target);
				}
			}
		}
		makePathAndMove(target);
		can_paint = true;
	}

	public void start(int new_target_id) {
		if (!can_explode) {
			for (Node_As node : init.values()) {
				node.resetValues();
			}
			target = init.get(new_target_id);
			target.index = new_target_id;
			t = new Thread(this);
			t.start();
		}
	}

	public boolean willKill(int new_target_x, int new_target_y) {
		if (explosion_diam == 0) {
			return false;
		}
		boolean rezult = false;
		double distance = Math.sqrt((new_target_x - target.x) * (new_target_x - target.x)
				+ (new_target_y - target.y) * (new_target_y - target.y));
		double explosion_radius = explosion_diam / 2;
		if (distance < explosion_radius && explosion_radius > 7) {
			rezult = true;
		}
		return rezult;
	}

	private int lengthManh(Node_As start, Node_As end) {
		int dif = Math.abs(start.x - end.x);
		dif += Math.abs(start.y - end.y);
		return dif;
	}

	private double length(Node_As str, Node_As end) {
		double dif = Math.sqrt((str.x - end.x) * (str.x - end.x) + (str.y - end.y) * (str.y - end.y));
		return dif;
	}

	private void makePathAndMove(Node_As last) {
		int[] pot_path_x = new int[1000];
		int[] pot_path_y = new int[1000];
		int i = 0;
		Node_As curr = last.copyInherit();
		Node_As child = last.copyInherit();
		while (curr.parent != null && i < 1000) {
			pot_path_x[i] = curr.x;
			pot_path_y[i] = curr.y;
			i++;
			child = curr.copyInherit();
			curr = curr.parent.copyInherit();
		}

		me = init.get(child.index);

		if (me.index == target.index) {
			can_explode = true;
		}

		path_x = new int[i];
		path_y = new int[i];
		path_x = Arrays.copyOf(pot_path_x, i);
		path_y = Arrays.copyOf(pot_path_y, i);

	}
	
	public int getCurrentPossision() {
		int poss = me.index;
		return poss;
	}

	public void draw(Graphics g) {
		if (can_paint) {
			Graphics2D gn = (Graphics2D) g;
			gn.setColor(color);
			gn.drawRect(me.x - b_width / 4, me.y - b_width / 4, 10, 10);
			gn.setColor(new Color(40, 40, 40));
			gn.drawPolyline(path_x, path_y, path_x.length);
			if (can_explode) {
				gn.setColor(new Color(255-(int)explosion_diam, 20, 20));
				gn.drawOval(target.x - (int) explosion_diam / 2, target.y - (int) explosion_diam / 2,
						(int) explosion_diam, (int) explosion_diam);
				explosion_diam += 0.5;
				if(explosion_diam>255) {
					can_explode=false;
					explosion_diam = 0;
				}
			}
		}
	}

}
