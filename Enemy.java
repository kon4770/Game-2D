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
	private boolean can_shoot = true;
	private HashMap<Integer, HashSet<Integer>> map;
	private HashMap<Integer,Node_As> init;
	private int[] path_x;
	private int[] path_y;
	private int b_width;
	private int n_block;
	private int correction;

	public Enemy(HashMap<Integer, HashSet<Integer>> map, int width, int n_block, int target_id) {
		this.map = map;

		b_width = width;
		this.n_block = n_block;
		correction = 500 - (n_block / 2 * width);
		
		color = new Color(220,220,220);
		
		init = new HashMap<>();
		for(Integer key: map.keySet()) {
			Node_As curr = new Node_As();
			curr.index = key;
			curr.x = key / n_block * width + correction + width / 2;
			curr.y = key % n_block * width + correction + width / 2;
			init.put(key,curr);
		}
		for(Integer key: init.keySet()) {
			for(Integer n_key: map.get(key)) {
				Node_As curr = init.get(key);
				curr.neighb.add(init.get(n_key));
			}
		}
		target = init.get(target_id);
		target.x = target_id / n_block * width + correction + width / 2;
		target.y = target_id % n_block * width + correction + width / 2;

		Random rnd = new Random();
		int start = (int) map.keySet().toArray()[rnd.nextInt(map.size())];
		while (canSeeEnemy(start)) {
			start = (int) map.keySet().toArray()[rnd.nextInt(map.size())];
		}
		
		me = init.get(start);
		me.x = start / n_block * width + correction + width / 2;
		me.y = start % n_block * width + correction + width / 2;
		me.index = start;
	}

	public void run() {
		int lastIndx = 0;
		Node_As curr = me;
		curr.dist_loc = 0;
		curr.dist_glob = lengthManh(me, target);
		Queue <Node_As> not_tested = new PriorityQueue<>();
		not_tested.add(curr);
		while(!not_tested.isEmpty() && !canSeeEnemy(curr.index)) {
			while(!not_tested.isEmpty() && not_tested.peek().been_v) {
				System.out.println("REM: " + not_tested.peek());
				not_tested.remove();
			}
			if(not_tested.isEmpty()) {
				System.out.println("BREAKKKKKKKKKKKKK");
				break;
				
			}
			curr = not_tested.poll();
			curr.been_v=true;
			System.out.println("bef loop curr "  + curr);
			for(Node_As neighbour : curr.neighb) {
				if(!neighbour.been_v) {
					not_tested.add(neighbour);
				}
				double poss_lower_goal = curr.dist_loc+length(curr,target);
				if(poss_lower_goal < neighbour.dist_glob) {
					lastIndx = neighbour.index;
					neighbour.parent = curr;
					neighbour.dist_loc = poss_lower_goal;
					neighbour.dist_glob = neighbour.dist_loc + lengthManh(neighbour,target);
				}
				else {
					System.out.println("neib " + neighbour);
					System.out.println("VS   " + poss_lower_goal);
				}
			}
		}
		move();
		System.out.println("last Index " + lastIndx);
		makePath(target);
		System.out.println("End");
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		can_paint=true;
		//can_shoot=false;
	}

	public void start(int new_target_x, int new_target_y) {
		int new_target_id = (new_target_x-correction)/ b_width  * n_block + (new_target_y-correction)/ b_width;
		
		if (can_shoot) { // new_target_id != target.index
			target.x = (target.index/n_block)*b_width+correction+b_width/2;
			target.y = (target.index%n_block)*b_width+correction+b_width/2;
			System.out.println("Starting");
			target = init.get(new_target_id);
			target.x = new_target_x;
			target.y = new_target_y;
			target.index = new_target_id;
			t = new Thread(this);
			t.start();
		} else {
			move();
			System.out.println("MOVE bo else");
		}
		/*
		if (can_shoot) { // !=
			System.out.println("Starting");
			t = new Thread(this);
			t.start();
		} else {
			move();
			System.out.println("MOVE bo else");
		}*/
	}
	
	private void move() {
		// TODO
	}

	private boolean canSeeEnemy(int indx) {
		// TODO
		return false;
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

	private void makePath(Node_As last) {
		int [] pot_path_x = new int[1000];
		int [] pot_path_y = new int[1000];
		int i = 0;
		Node_As curr = last;
		System.out.println("prnt " + curr.parent );
		while (curr.parent != null&&i<1000) {
			pot_path_x[i] = curr.x;
			pot_path_y[i] = curr.y;
			i++;
			curr = curr.parent;
		}
		System.out.println("i    " + i );
		System.out.println("targ " + target  );
		System.out.println("targ2" + init.get(target.index)  );
		System.out.println("tngbr" + target.neighb  );
		System.out.println("tngb2" + init.get(target.index).neighb );
		System.out.println("map  " + map.get(target.index)  );
		path_x = new int[i];
		path_y = new int[i];
		path_x = Arrays.copyOf(pot_path_x, i);
		path_y = Arrays.copyOf(pot_path_y, i);
	}

	public void draw(Graphics g) {
		if (can_paint) {
			Graphics2D gn = (Graphics2D) g;
			gn.setColor(color);
			gn.drawRect(me.x-b_width/4, me.y-b_width/4, 10, 10);
			//System.out.println("length " + path_x.length);
			gn.setColor(new Color(40,40,40));
			gn.drawPolyline(path_x, path_y, path_x.length);
			//can_paint = false;
			if (can_shoot) {
				
			}
		}
	}

}
